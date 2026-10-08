<?php

namespace App\Services;

use App\Enums\AccountStatus;
use App\Enums\SubscriptionStatus;
use App\Enums\UserRole;
use App\Models\User;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Str;

class UserRegistrationService
{
    public function __construct(
        private readonly BusinessSettingsService $businessSettings,
    ) {}

    /**
     * @param  array{
     *     first_name: string,
     *     last_name: string,
     *     email: string,
     *     phone_number: string,
     *     location: string,
     *     password: string,
     *     referral_code?: string|null
     * }  $validated
     */
    public function createAgent(array $validated): User
    {
        return DB::transaction(function () use ($validated) {
            $trialDays = $this->businessSettings->getInt('trial_days', 7);
            $now = now();

            $referrer = null;
            if (! empty($validated['referral_code'])) {
                $referrer = User::query()
                    ->where('referral_code', strtoupper($validated['referral_code']))
                    ->first();

                if (! $referrer) {
                    abort(422, 'Referral code not found.');
                }
            }

            $user = User::query()->create([
                'name' => trim($validated['first_name'].' '.$validated['last_name']),
                'email' => $validated['email'],
                'password' => $validated['password'],
                'role' => UserRole::Agent,
                'first_name' => $validated['first_name'],
                'last_name' => $validated['last_name'],
                'phone_number' => $validated['phone_number'],
                'location' => $validated['location'],
                'account_status' => AccountStatus::Active,
                'referral_code' => $this->generateUniqueReferralCode(),
                'referred_by_code' => $referrer?->referral_code,
                'referred_by_user_id' => $referrer?->id,
                'referral_date' => $referrer ? $now : null,
                'trial_start_date' => $now,
                'trial_end_date' => $now->copy()->addDays($trialDays),
                'is_trial_used' => true,
                'subscription_status' => SubscriptionStatus::Trial,
                'email_verified_at' => $now,
            ]);

            $user->wallet()->create([
                'available_balance' => 0,
                'pending_balance' => 0,
                'withdrawn_balance' => 0,
                'lifetime_earnings' => 0,
            ]);

            if ($referrer) {
                \App\Models\Referral::query()->create([
                    'referrer_user_id' => $referrer->id,
                    'referrer_code' => $referrer->referral_code,
                    'referred_user_id' => $user->id,
                    'referred_user_name' => $user->name,
                    'registration_date' => $now,
                ]);
            }

            return $user;
        });
    }

    private function generateUniqueReferralCode(): string
    {
        do {
            $code = strtoupper(Str::random(8));
        } while (User::query()->where('referral_code', $code)->exists());

        return $code;
    }
}
