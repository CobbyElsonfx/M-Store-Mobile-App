<?php

namespace App\Services;

use App\Mail\MoMoKotleMail;
use App\Models\EmailNotificationLog;
use App\Models\User;
use App\Support\EmailNotificationTypes;
use Illuminate\Database\UniqueConstraintViolationException;
use Illuminate\Support\Facades\Mail;
use Throwable;

class EmailNotificationService
{
    public function __construct(
        private readonly BusinessSettingsService $businessSettings,
    ) {}

    /**
     * @param  array<string, string|null>  $rows
     */
    public function sendOnce(
        string $eventKey,
        string $type,
        string $category,
        string $recipientEmail,
        string $subject,
        string $title,
        string $greeting,
        string $intro,
        array $rows = [],
        ?string $nextSteps = null,
        ?string $note = null,
        ?User $user = null,
        ?string $referenceType = null,
        ?string $referenceId = null,
    ): ?EmailNotificationLog {
        if ($recipientEmail === '' || ! $this->shouldSend($type, $category)) {
            return null;
        }

        try {
            $log = EmailNotificationLog::query()->create([
                'event_key' => $eventKey,
                'notification_type' => $type,
                'category' => $category,
                'user_id' => $user?->id,
                'recipient_email' => $recipientEmail,
                'subject' => $subject,
                'status' => 'queued',
                'reference_type' => $referenceType,
                'reference_id' => $referenceId,
            ]);
        } catch (UniqueConstraintViolationException) {
            return EmailNotificationLog::query()->where('event_key', $eventKey)->first();
        }

        if ($log->isSent()) {
            return $log;
        }

        try {
            Mail::to($recipientEmail)->send(new MoMoKotleMail(
                subjectLine: $subject,
                title: $title,
                greeting: $greeting,
                intro: $intro,
                rows: $rows,
                nextSteps: $nextSteps,
                note: $note,
                supportContact: $this->supportContact(),
            ));

            $log->update([
                'status' => 'sent',
                'sent_at' => now(),
                'failure_reason' => null,
            ]);
        } catch (Throwable $exception) {
            $log->update([
                'status' => 'failed',
                'failure_reason' => $exception->getMessage(),
            ]);

            report($exception);
        }

        return $log->fresh();
    }

    /** @return list<string> */
    public function adminRecipients(): array
    {
        $raw = (string) $this->businessSettings->get('admin_notification_emails', '');
        $emails = preg_split('/[\s,;]+/', $raw, -1, PREG_SPLIT_NO_EMPTY) ?: [];

        return array_values(array_unique(array_filter($emails, fn (string $email) => filter_var($email, FILTER_VALIDATE_EMAIL))));
    }

    /** @return list<int> */
    public function reminderDays(): array
    {
        $raw = (string) $this->businessSettings->get('subscription_reminder_days', '7,3,1');
        $days = array_map('intval', preg_split('/[\s,;]+/', $raw, -1, PREG_SPLIT_NO_EMPTY) ?: []);

        return array_values(array_unique(array_filter($days, fn (int $day) => $day > 0)));
    }

    public function supportContact(): string
    {
        return trim((string) $this->businessSettings->get('support_contact', ''));
    }

    public function greetingFor(?User $user, string $fallback = 'there'): string
    {
        $name = trim(($user?->first_name ?: '').' '.($user?->last_name ?: ''));
        if ($name === '') {
            $name = trim((string) ($user?->name ?: $fallback));
        }

        return 'Hello '.($name !== '' ? $name : $fallback).',';
    }

    private function shouldSend(string $type, string $category): bool
    {
        if (! in_array($type, EmailNotificationTypes::OPTIONAL_TYPES, true)) {
            return true;
        }

        if ($type === EmailNotificationTypes::SUBSCRIPTION_EXPIRING
            && ! filter_var($this->businessSettings->get('email_reminders_enabled', true), FILTER_VALIDATE_BOOLEAN)
        ) {
            return false;
        }

        if ($category === EmailNotificationTypes::CATEGORY_ADMIN
            && ! filter_var($this->businessSettings->get('email_admin_alerts_enabled', true), FILTER_VALIDATE_BOOLEAN)
        ) {
            return false;
        }

        return true;
    }
}
