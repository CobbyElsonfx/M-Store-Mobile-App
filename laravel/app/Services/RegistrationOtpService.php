<?php

namespace App\Services;

use App\Mail\RegistrationOtpMail;
use App\Models\RegistrationOtp;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Mail;
use Illuminate\Validation\ValidationException;

class RegistrationOtpService
{
    private const OTP_LENGTH = 6;

    private const OTP_TTL_MINUTES = 10;

    private const MAX_ATTEMPTS = 5;

    private const RESEND_COOLDOWN_SECONDS = 60;

    /**
     * @param  array{
     *     first_name: string,
     *     last_name: string,
     *     email: string,
     *     phone_number: string,
     *     location: string,
     *     password: string,
     *     referral_code?: string|null
     * }  $payload
     */
    public function send(array $payload): RegistrationOtp
    {
        $email = strtolower(trim($payload['email']));
        $code = $this->generateCode();

        RegistrationOtp::query()
            ->where('email', $email)
            ->whereNull('verified_at')
            ->delete();

        $otp = RegistrationOtp::query()->create([
            'email' => $email,
            'code_hash' => Hash::make($code),
            'payload' => $payload,
            'expires_at' => now()->addMinutes(self::OTP_TTL_MINUTES),
        ]);

        Mail::to($email)->send(new RegistrationOtpMail(
            firstName: $payload['first_name'],
            otpCode: $code,
            expiresMinutes: self::OTP_TTL_MINUTES,
        ));

        return $otp;
    }

    public function resend(string $email): RegistrationOtp
    {
        $email = strtolower(trim($email));

        $existing = RegistrationOtp::query()
            ->where('email', $email)
            ->whereNull('verified_at')
            ->latest('id')
            ->first();

        if (! $existing) {
            throw ValidationException::withMessages([
                'email' => ['No pending registration found for this email.'],
            ]);
        }

        if ($existing->created_at->diffInSeconds(now()) < self::RESEND_COOLDOWN_SECONDS) {
            throw ValidationException::withMessages([
                'email' => ['Please wait before requesting another code.'],
            ]);
        }

        return $this->send($existing->payload);
    }

    /**
     * @return array{
     *     first_name: string,
     *     last_name: string,
     *     email: string,
     *     phone_number: string,
     *     location: string,
     *     password: string,
     *     referral_code?: string|null
     * }
     */
    public function verify(string $email, string $code): array
    {
        $email = strtolower(trim($email));
        $code = trim($code);

        $otp = RegistrationOtp::query()
            ->where('email', $email)
            ->whereNull('verified_at')
            ->latest('id')
            ->first();

        if (! $otp) {
            throw ValidationException::withMessages([
                'email' => ['No pending registration found for this email.'],
            ]);
        }

        if ($otp->isExpired()) {
            throw ValidationException::withMessages([
                'otp_code' => ['Verification code has expired. Please register again.'],
            ]);
        }

        if ($otp->attempts >= self::MAX_ATTEMPTS) {
            throw ValidationException::withMessages([
                'otp_code' => ['Too many failed attempts. Please register again.'],
            ]);
        }

        if (! Hash::check($code, $otp->code_hash)) {
            $otp->increment('attempts');

            throw ValidationException::withMessages([
                'otp_code' => ['Invalid verification code.'],
            ]);
        }

        $otp->update(['verified_at' => now()]);

        return $otp->payload;
    }

    private function generateCode(): string
    {
        return str_pad((string) random_int(0, 999999), self::OTP_LENGTH, '0', STR_PAD_LEFT);
    }
}
