<?php

namespace App\Events;

use App\Models\Payment;
use App\Models\Subscription;
use App\Models\User;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;
use Illuminate\Support\Carbon;

class SubscriptionRenewed
{
    use Dispatchable, SerializesModels;

    public function __construct(
        public readonly User $user,
        public readonly Subscription $subscription,
        public readonly ?Carbon $previousExpiry = null,
        public readonly ?Payment $payment = null,
    ) {}
}
