<?php

namespace App\Console\Commands;

use App\Enums\SubscriptionStatus;
use App\Events\SubscriptionExpired;
use App\Events\SubscriptionExpiringSoon;
use App\Models\Referral;
use App\Models\Subscription;
use App\Models\User;
use App\Services\EmailNotificationService;
use Illuminate\Console\Command;

class ProcessSubscriptionLifecycle extends Command
{
    protected $signature = 'subscriptions:process-lifecycle';

    protected $description = 'Expire due subscriptions and send configurable reminder emails';

    public function handle(EmailNotificationService $emails): int
    {
        $expired = 0;
        $reminded = 0;

        User::query()
            ->where('subscription_status', SubscriptionStatus::Active)
            ->whereNotNull('subscription_expiry_date')
            ->where('subscription_expiry_date', '<=', now())
            ->orderBy('id')
            ->each(function (User $user) use (&$expired) {
                $expiry = $user->subscription_expiry_date;

                Subscription::query()
                    ->where('user_id', $user->id)
                    ->where('status', 'Active')
                    ->update(['status' => 'Expired']);

                $user->update([
                    'subscription_status' => SubscriptionStatus::Expired,
                    'active_subscription_id' => null,
                ]);

                Referral::query()
                    ->where('referred_user_id', $user->id)
                    ->update(['has_active_subscription' => false]);

                SubscriptionExpired::dispatch($user->fresh(), $expiry, 'schedule');
                $expired++;
            });

        foreach ($emails->reminderDays() as $days) {
            $targetDate = now()->addDays($days)->toDateString();

            User::query()
                ->where('subscription_status', SubscriptionStatus::Active)
                ->whereNotNull('subscription_expiry_date')
                ->whereDate('subscription_expiry_date', $targetDate)
                ->orderBy('id')
                ->each(function (User $user) use ($days, &$reminded) {
                    SubscriptionExpiringSoon::dispatch($user, $days);
                    $reminded++;
                });
        }

        $this->info("Expired {$expired} subscription(s) and queued {$reminded} reminder(s).");

        return self::SUCCESS;
    }
}
