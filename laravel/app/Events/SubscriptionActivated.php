<?php

namespace App\Events;

use App\Models\Payment;
use App\Models\Subscription;
use App\Models\User;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class SubscriptionActivated
{
    use Dispatchable, SerializesModels;

    public function __construct(
        public readonly User $user,
        public readonly Subscription $subscription,
        public readonly ?Payment $payment = null,
    ) {}
}
