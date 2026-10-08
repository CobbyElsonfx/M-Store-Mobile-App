<?php

namespace App\Listeners;

use App\Events\AccountInformationUpdated;
use App\Events\AccountSuspended;
use App\Events\ApplicationStatusChanged;
use App\Events\ApplicationSubmitted;
use App\Events\PaymentAbandoned;
use App\Events\PaymentFailed;
use App\Events\PaymentSuccessful;
use App\Events\SubscriptionActivated;
use App\Events\SubscriptionExpired;
use App\Events\SubscriptionExpiringSoon;
use App\Events\SubscriptionRenewed;
use App\Events\UserRegistered;
use App\Models\Payment;
use App\Services\EmailNotificationService;
use App\Support\EmailNotificationTypes;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Queue\InteractsWithQueue;

class SendDomainEmailNotifications implements ShouldQueue
{
    use InteractsWithQueue;

    public bool $afterCommit = true;

    public function __construct(
        private readonly EmailNotificationService $emails,
    ) {}

    public function handle(object $event): void
    {
        match (true) {
            $event instanceof UserRegistered => $this->userRegistered($event),
            $event instanceof AccountInformationUpdated => $this->accountUpdated($event),
            $event instanceof AccountSuspended => $this->accountSuspended($event),
            $event instanceof SubscriptionActivated => $this->subscriptionActivated($event),
            $event instanceof SubscriptionRenewed => $this->subscriptionRenewed($event),
            $event instanceof SubscriptionExpired => $this->subscriptionExpired($event),
            $event instanceof SubscriptionExpiringSoon => $this->subscriptionExpiring($event),
            $event instanceof PaymentSuccessful => $this->paymentSuccessful($event),
            $event instanceof PaymentFailed => $this->paymentFailed($event),
            $event instanceof PaymentAbandoned => $this->paymentAbandoned($event),
            $event instanceof ApplicationSubmitted => $this->applicationSubmitted($event),
            $event instanceof ApplicationStatusChanged => $this->applicationStatusChanged($event),
            default => null,
        };
    }

    private function userRegistered(UserRegistered $event): void
    {
        $user = $event->user;
        $this->emails->sendOnce(
            eventKey: 'account.registered.'.$user->id,
            type: EmailNotificationTypes::ACCOUNT_WELCOME,
            category: EmailNotificationTypes::CATEGORY_ACCOUNT,
            recipientEmail: (string) $user->email,
            subject: 'Welcome to MoMo Kotle',
            title: 'Welcome to MoMo Kotle',
            greeting: $this->emails->greetingFor($user),
            intro: 'Your MoMo Kotle account was created successfully. You can now sign in and complete your first service.',
            rows: [
                'Name' => trim(($user->first_name ?? '').' '.($user->last_name ?? '')) ?: $user->name,
                'Email' => $user->email,
                'Account status' => $user->account_status?->value ?? 'Active',
            ],
            nextSteps: 'Open the app, turn on Accessibility if you use USSD automation, then explore Services and complete your profile.',
            user: $user,
            referenceType: 'user',
            referenceId: (string) $user->id,
        );

        $this->notifyAdmins(
            type: EmailNotificationTypes::ADMIN_USER_REGISTERED,
            eventKey: 'admin.user.registered.'.$user->id,
            subject: 'New MoMo Kotle agent registered',
            title: 'New agent registered',
            intro: 'A new agent account was created.',
            rows: [
                'Name' => $user->name,
                'Email' => $user->email,
            ],
            referenceId: (string) $user->id,
        );
    }

    private function accountUpdated(AccountInformationUpdated $event): void
    {
        $user = $event->user;
        $this->emails->sendOnce(
            eventKey: 'account.updated.'.$user->id.'.'.$event->category.'.'.($event->occurredAt ?? now()->timestamp),
            type: EmailNotificationTypes::ACCOUNT_UPDATED,
            category: EmailNotificationTypes::CATEGORY_ACCOUNT,
            recipientEmail: (string) $user->email,
            subject: 'Your MoMo Kotle account was updated',
            title: 'Account information updated',
            greeting: $this->emails->greetingFor($user),
            intro: 'Important information on your MoMo Kotle account was updated.',
            rows: [
                'Change' => $event->category,
                'Date' => ($event->occurredAt ?? now()->toDateTimeString()),
            ],
            nextSteps: 'If you did not make this change, contact support immediately.',
            note: 'This message does not include passwords, PINs, or other secrets.',
            user: $user,
            referenceType: 'user',
            referenceId: (string) $user->id,
        );
    }

    private function accountSuspended(AccountSuspended $event): void
    {
        $user = $event->user;
        $this->emails->sendOnce(
            eventKey: 'account.suspended.'.$user->id.'.'.($user->updated_at?->timestamp ?? now()->timestamp),
            type: EmailNotificationTypes::ACCOUNT_SUSPENDED,
            category: EmailNotificationTypes::CATEGORY_ACCOUNT,
            recipientEmail: (string) $user->email,
            subject: 'Your MoMo Kotle account was updated',
            title: 'Account suspended',
            greeting: $this->emails->greetingFor($user),
            intro: 'Your MoMo Kotle account has been suspended.',
            rows: [
                'Event' => 'Account suspended',
                'Date' => now()->toDateTimeString(),
            ],
            nextSteps: 'If you did not expect this, contact support.',
            user: $user,
            referenceType: 'user',
            referenceId: (string) $user->id,
        );
    }

    private function subscriptionActivated(SubscriptionActivated $event): void
    {
        $subscription = $event->subscription;
        $payment = $event->payment;
        $this->emails->sendOnce(
            eventKey: 'subscription.activated.'.$subscription->id,
            type: EmailNotificationTypes::SUBSCRIPTION_ACTIVATED,
            category: EmailNotificationTypes::CATEGORY_SUBSCRIPTION,
            recipientEmail: (string) $event->user->email,
            subject: 'Your MoMo Kotle subscription is active',
            title: 'Subscription active',
            greeting: $this->emails->greetingFor($event->user),
            intro: 'Your subscription is now active.',
            rows: [
                'Plan' => $subscription->plan_name,
                'Amount paid' => $this->money($payment?->amount ?? $subscription->price, $payment?->currency),
                'Start date' => $subscription->start_date?->format('Y-m-d H:i'),
                'Expiry date' => $subscription->end_date?->format('Y-m-d H:i'),
                'Reference' => $subscription->payment_reference ?: $payment?->reference,
                'Status' => 'Active',
            ],
            nextSteps: 'Open the app to continue using full MoMo Kotle access.',
            user: $event->user,
            referenceType: 'subscription',
            referenceId: $subscription->id,
        );
    }

    private function subscriptionRenewed(SubscriptionRenewed $event): void
    {
        $subscription = $event->subscription;
        $payment = $event->payment;
        $this->emails->sendOnce(
            eventKey: 'subscription.renewed.'.$subscription->id,
            type: EmailNotificationTypes::SUBSCRIPTION_RENEWED,
            category: EmailNotificationTypes::CATEGORY_SUBSCRIPTION,
            recipientEmail: (string) $event->user->email,
            subject: 'Your MoMo Kotle subscription was renewed',
            title: 'Subscription renewed',
            greeting: $this->emails->greetingFor($event->user),
            intro: 'Your subscription was extended.',
            rows: [
                'Plan' => $subscription->plan_name,
                'Amount' => $this->money($payment?->amount ?? $subscription->price, $payment?->currency),
                'Previous expiry' => $event->previousExpiry?->format('Y-m-d H:i') ?: '—',
                'New expiry' => $subscription->end_date?->format('Y-m-d H:i'),
                'Reference' => $subscription->payment_reference ?: $payment?->reference,
            ],
            user: $event->user,
            referenceType: 'subscription',
            referenceId: $subscription->id,
        );
    }

    private function subscriptionExpired(SubscriptionExpired $event): void
    {
        $user = $event->user;
        $expiry = $event->expiryDate ?? $user->subscription_expiry_date;
        $this->emails->sendOnce(
            eventKey: 'subscription.expired.'.$user->id.'.'.($expiry?->toDateString() ?? 'none'),
            type: EmailNotificationTypes::SUBSCRIPTION_EXPIRED,
            category: EmailNotificationTypes::CATEGORY_SUBSCRIPTION,
            recipientEmail: (string) $user->email,
            subject: 'Your MoMo Kotle subscription has expired',
            title: 'Subscription expired',
            greeting: $this->emails->greetingFor($user),
            intro: $event->source === 'admin'
                ? 'Your subscription has been ended by an administrator.'
                : 'Your MoMo Kotle subscription has expired.',
            rows: [
                'Expiry date' => $expiry?->format('Y-m-d H:i') ?: '—',
                'Access' => 'Subscription required',
            ],
            nextSteps: 'Open Subscriptions in the app to renew and restore full access.',
            user: $user,
            referenceType: 'user',
            referenceId: (string) $user->id,
        );
    }

    private function subscriptionExpiring(SubscriptionExpiringSoon $event): void
    {
        $user = $event->user;
        $this->emails->sendOnce(
            eventKey: 'subscription.reminder.'.$user->id.'.'.$event->daysRemaining.'.'.($user->subscription_expiry_date?->toDateString() ?? 'none'),
            type: EmailNotificationTypes::SUBSCRIPTION_EXPIRING,
            category: EmailNotificationTypes::CATEGORY_SUBSCRIPTION,
            recipientEmail: (string) $user->email,
            subject: 'Your MoMo Kotle subscription expires soon',
            title: 'Subscription expiring soon',
            greeting: $this->emails->greetingFor($user),
            intro: 'Your subscription will expire soon. Renew to keep full access.',
            rows: [
                'Days remaining' => (string) $event->daysRemaining,
                'Expiry date' => $user->subscription_expiry_date?->format('Y-m-d H:i') ?: '—',
            ],
            nextSteps: 'Open Subscriptions in the app to renew before the expiry date.',
            user: $user,
            referenceType: 'user',
            referenceId: (string) $user->id,
        );
    }

    private function paymentSuccessful(PaymentSuccessful $event): void
    {
        $payment = $event->payment->loadMissing(['user', 'plan']);
        $user = $payment->user;
        $this->emails->sendOnce(
            eventKey: 'payment.successful.'.$payment->id,
            type: EmailNotificationTypes::PAYMENT_SUCCESSFUL,
            category: EmailNotificationTypes::CATEGORY_PAYMENT,
            recipientEmail: (string) $user?->email,
            subject: 'Payment successful — MoMo Kotle',
            title: 'Payment successful',
            greeting: $this->emails->greetingFor($user),
            intro: 'We confirmed your payment.',
            rows: [
                'Amount' => $this->money($payment->amount, $payment->currency),
                'Currency' => $payment->currency,
                'Reference' => $payment->reference,
                'Date' => $payment->updated_at?->format('Y-m-d H:i') ?: now()->format('Y-m-d H:i'),
                'Purpose' => $payment->purpose ?: 'Subscription',
                'Plan' => $payment->plan?->plan_name,
            ],
            user: $user,
            referenceType: 'payment',
            referenceId: $payment->id,
        );

        $this->notifyAdmins(
            type: EmailNotificationTypes::ADMIN_PAYMENT_SUCCESSFUL,
            eventKey: 'admin.payment.successful.'.$payment->id,
            subject: 'New MoMo Kotle payment confirmed',
            title: 'Payment confirmed',
            intro: 'A subscription payment was confirmed.',
            rows: [
                'Agent' => $user?->email,
                'Amount' => $this->money($payment->amount, $payment->currency),
                'Reference' => $payment->reference,
            ],
            referenceId: $payment->id,
        );
    }

    private function paymentFailed(PaymentFailed $event): void
    {
        $this->paymentUnsuccessful($event->payment, EmailNotificationTypes::PAYMENT_FAILED, 'failed');
    }

    private function paymentAbandoned(PaymentAbandoned $event): void
    {
        $this->paymentUnsuccessful($event->payment, EmailNotificationTypes::PAYMENT_ABANDONED, 'abandoned');
    }

    private function paymentUnsuccessful(Payment $payment, string $type, string $outcome): void
    {
        $payment->loadMissing(['user', 'plan']);
        $user = $payment->user;
        $subject = $outcome === 'abandoned'
            ? 'MoMo Kotle payment was not completed'
            : 'MoMo Kotle payment failed';

        $this->emails->sendOnce(
            eventKey: 'payment.'.$outcome.'.'.$payment->id,
            type: $type,
            category: EmailNotificationTypes::CATEGORY_PAYMENT,
            recipientEmail: (string) $user?->email,
            subject: $subject,
            title: $outcome === 'abandoned' ? 'Payment not completed' : 'Payment failed',
            greeting: $this->emails->greetingFor($user),
            intro: $outcome === 'abandoned'
                ? 'Your payment was abandoned or cancelled before it was completed.'
                : 'Your payment could not be completed.',
            rows: [
                'Amount' => $this->money($payment->amount, $payment->currency),
                'Reference' => $payment->reference,
                'Date' => $payment->updated_at?->format('Y-m-d H:i') ?: now()->format('Y-m-d H:i'),
                'Status' => $payment->status,
            ],
            nextSteps: 'Open Subscriptions in the app to try again. No access was granted for this payment.',
            user: $user,
            referenceType: 'payment',
            referenceId: $payment->id,
        );

        if ($type === EmailNotificationTypes::PAYMENT_FAILED) {
            $this->notifyAdmins(
                type: EmailNotificationTypes::ADMIN_PAYMENT_FAILED,
                eventKey: 'admin.payment.failed.'.$payment->id,
                subject: 'MoMo Kotle payment failed',
                title: 'Payment failed',
                intro: 'A subscription payment failed.',
                rows: [
                    'Agent' => $user?->email,
                    'Reference' => $payment->reference,
                    'Status' => $payment->status,
                ],
                referenceId: $payment->id,
            );
        }
    }

    private function applicationSubmitted(ApplicationSubmitted $event): void
    {
        $application = $event->application->loadMissing('user');
        $user = $application->user;
        $this->emails->sendOnce(
            eventKey: 'application.submitted.'.$application->id,
            type: EmailNotificationTypes::APPLICATION_SUBMITTED,
            category: EmailNotificationTypes::CATEGORY_APPLICATION,
            recipientEmail: (string) $user?->email,
            subject: 'Application submitted — '.$application->form_type,
            title: 'Application submitted',
            greeting: $this->emails->greetingFor($user),
            intro: 'We received your application. Open the app for the full details.',
            rows: [
                'Name' => $application->applicant_name ?: $user?->name,
                'Application' => $application->form_type,
                'Reference' => $application->id,
                'Submitted' => $application->updated_at?->format('Y-m-d H:i') ?: now()->format('Y-m-d H:i'),
                'Status' => $application->status,
            ],
            nextSteps: 'We will review your application and update you in the app and by email.',
            user: $user,
            referenceType: 'application',
            referenceId: $application->id,
        );

        $this->notifyAdmins(
            type: EmailNotificationTypes::ADMIN_APPLICATION_SUBMITTED,
            eventKey: 'admin.application.submitted.'.$application->id,
            subject: 'New application submitted — '.$application->form_type,
            title: 'Application awaiting review',
            intro: 'A new application is ready for review.',
            rows: [
                'Application' => $application->form_type,
                'Reference' => $application->id,
                'Applicant' => $application->applicant_name,
            ],
            referenceId: $application->id,
        );
    }

    private function applicationStatusChanged(ApplicationStatusChanged $event): void
    {
        $application = $event->application->loadMissing('user');
        $user = $application->user;
        $reason = $event->newStatus === 'Rejected' ? $event->adminNote : ($event->adminNote ?: null);

        $this->emails->sendOnce(
            eventKey: 'application.status.'.$application->id.'.'.$event->previousStatus.'.'.$event->newStatus,
            type: EmailNotificationTypes::APPLICATION_STATUS_CHANGED,
            category: EmailNotificationTypes::CATEGORY_APPLICATION,
            recipientEmail: (string) $user?->email,
            subject: 'Your '.$application->form_type.' status has been updated',
            title: 'Application status updated',
            greeting: $this->emails->greetingFor($user),
            intro: 'The status of your application has changed.',
            rows: [
                'Application' => $application->form_type,
                'Reference' => $application->id,
                'Previous status' => $event->previousStatus,
                'New status' => $event->newStatus,
                'Date' => now()->toDateTimeString(),
                'Reason' => $reason,
            ],
            nextSteps: $this->applicationNextStep($event->newStatus),
            user: $user,
            referenceType: 'application',
            referenceId: $application->id,
        );
    }

    private function applicationNextStep(string $status): string
    {
        return match ($status) {
            'Approved' => 'No further action is required unless the app asks you to collect or confirm something.',
            'Rejected' => 'Review the reason above, then open the app if you need to start a new application.',
            'ResubmissionRequired' => 'Open the app, update the requested information or documents, and resubmit.',
            default => 'Open My Submissions in the app for the latest details.',
        };
    }

    /**
     * @param  array<string, string|null>  $rows
     */
    private function notifyAdmins(
        string $type,
        string $eventKey,
        string $subject,
        string $title,
        string $intro,
        array $rows,
        string $referenceId,
    ): void {
        foreach ($this->emails->adminRecipients() as $email) {
            $this->emails->sendOnce(
                eventKey: $eventKey.'.'.md5($email),
                type: $type,
                category: EmailNotificationTypes::CATEGORY_ADMIN,
                recipientEmail: $email,
                subject: $subject,
                title: $title,
                greeting: 'Hello,',
                intro: $intro,
                rows: $rows,
                nextSteps: 'Review this in the MoMo Kotle admin console.',
                referenceType: 'admin',
                referenceId: $referenceId,
            );
        }
    }

    private function money(mixed $amount, ?string $currency): string
    {
        $code = $currency ?: 'GHS';

        return $code.' '.number_format((float) $amount, 2);
    }
}
