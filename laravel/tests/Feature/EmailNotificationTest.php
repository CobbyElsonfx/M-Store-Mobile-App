<?php

namespace Tests\Feature;

use App\Enums\SubscriptionStatus;
use App\Enums\UserRole;
use App\Events\AccountInformationUpdated;
use App\Mail\MoMoKotleMail;
use App\Mail\RegistrationOtpMail;
use App\Models\AgentApplication;
use App\Models\BusinessSetting;
use App\Models\EmailNotificationLog;
use App\Models\Subscription;
use App\Models\SubscriptionPlan;
use App\Models\User;
use App\Services\EmailNotificationService;
use App\Support\EmailNotificationTypes;
use App\Support\PaystackMoney;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Illuminate\Http\UploadedFile;
use Illuminate\Support\Facades\Http;
use Illuminate\Support\Facades\Mail;
use Illuminate\Support\Facades\Storage;
use Laravel\Sanctum\Sanctum;
use Tests\TestCase;

class EmailNotificationTest extends TestCase
{
    use RefreshDatabase;

    protected function setUp(): void
    {
        parent::setUp();
        $this->seed();
        Storage::fake('agent_documents');
        Mail::fake();
    }

    public function test_new_account_sends_one_welcome_email(): void
    {
        $this->registerVerifiedAgent();

        Mail::assertSent(MoMoKotleMail::class, fn (MoMoKotleMail $mail) => $mail->subjectLine === 'Welcome to MoMo Kotle');
        $this->assertSame(1, EmailNotificationLog::query()->where('notification_type', EmailNotificationTypes::ACCOUNT_WELCOME)->count());
    }

    public function test_account_update_event_sends_security_email(): void
    {
        $agent = User::factory()->create(['first_name' => 'Ama', 'last_name' => 'Mensah']);

        AccountInformationUpdated::dispatch($agent, 'Phone number', now()->toDateTimeString());

        Mail::assertSent(MoMoKotleMail::class, function (MoMoKotleMail $mail) {
            return $mail->subjectLine === 'Your MoMo Kotle account was updated'
                && $mail->rows['Change'] === 'Phone number';
        });
    }

    public function test_successful_paystack_payment_sends_payment_and_activation_emails_once(): void
    {
        $agent = User::factory()->create();
        Sanctum::actingAs($agent);
        $plan = $this->monthPlan();
        $this->fakePaystack($plan);

        $reference = $this->postJson('/api/subscriptions/pay', [
            'subscription_plan_id' => $plan->id,
        ])->json('reference');

        Mail::assertNotSent(MoMoKotleMail::class, fn (MoMoKotleMail $mail) => str_contains($mail->subjectLine, 'Payment successful'));

        $this->getJson("/api/subscriptions/payment/{$reference}")->assertOk();
        $this->getJson("/api/subscriptions/payment/{$reference}")->assertOk();

        $this->assertSame(1, EmailNotificationLog::query()->where('notification_type', EmailNotificationTypes::PAYMENT_SUCCESSFUL)->count());
        $this->assertSame(1, EmailNotificationLog::query()->where('notification_type', EmailNotificationTypes::SUBSCRIPTION_ACTIVATED)->count());
        Mail::assertSent(MoMoKotleMail::class, fn (MoMoKotleMail $mail) => $mail->subjectLine === 'Payment successful — MoMo Kotle');
        Mail::assertSent(MoMoKotleMail::class, fn (MoMoKotleMail $mail) => $mail->subjectLine === 'Your MoMo Kotle subscription is active');
    }

    public function test_admin_plan_change_while_active_sends_renewal_email(): void
    {
        $admin = $this->admin();
        $agent = User::factory()->create(['role' => UserRole::Agent]);
        $month = $this->monthPlan();
        $quarter = SubscriptionPlan::query()
            ->where('status', 'Active')
            ->where('id', '!=', $month->id)
            ->orderBy('duration_days')
            ->firstOrFail();

        $this->actingAs($admin)->patch("/admin/users/{$agent->id}/subscription", [
            'action' => 'grant',
            'plan_id' => $month->id,
        ])->assertRedirect();

        $this->actingAs($admin)->patch("/admin/users/{$agent->id}/subscription", [
            'action' => 'grant',
            'plan_id' => $quarter->id,
        ])->assertRedirect();

        $this->assertSame(1, EmailNotificationLog::query()->where('notification_type', EmailNotificationTypes::SUBSCRIPTION_ACTIVATED)->count());
        $this->assertSame(1, EmailNotificationLog::query()->where('notification_type', EmailNotificationTypes::SUBSCRIPTION_RENEWED)->count());
        Mail::assertSent(MoMoKotleMail::class, fn (MoMoKotleMail $mail) => $mail->subjectLine === 'Your MoMo Kotle subscription was renewed');
    }

    public function test_lifecycle_command_sends_expiry_and_reminder_without_duplicates(): void
    {
        $expiring = User::factory()->create([
            'subscription_status' => SubscriptionStatus::Active,
            'subscription_expiry_date' => now()->addDays(3),
        ]);
        $expired = User::factory()->create([
            'subscription_status' => SubscriptionStatus::Active,
            'subscription_expiry_date' => now()->subHour(),
        ]);

        $this->artisan('subscriptions:process-lifecycle')->assertSuccessful();
        $this->artisan('subscriptions:process-lifecycle')->assertSuccessful();

        $this->assertSame(1, EmailNotificationLog::query()->where('notification_type', EmailNotificationTypes::SUBSCRIPTION_EXPIRED)->count());
        $this->assertSame(1, EmailNotificationLog::query()->where('notification_type', EmailNotificationTypes::SUBSCRIPTION_EXPIRING)->count());
        $this->assertSame('Expired', $expired->fresh()->subscription_status->value);
        $this->assertSame('Active', $expiring->fresh()->subscription_status->value);
    }

    public function test_failed_payment_sends_one_failure_email(): void
    {
        $agent = User::factory()->create();
        Sanctum::actingAs($agent);
        $plan = $this->monthPlan();
        $this->fakePaystack($plan, verifiedStatus: 'failed');

        $reference = $this->postJson('/api/subscriptions/pay', [
            'subscription_plan_id' => $plan->id,
        ])->json('reference');
        $this->getJson("/api/subscriptions/payment/{$reference}")
            ->assertOk()
            ->assertJsonPath('data.status', 'Failed');
        $this->getJson("/api/subscriptions/payment/{$reference}")->assertOk();

        Mail::assertSent(MoMoKotleMail::class, fn (MoMoKotleMail $mail) => $mail->subjectLine === 'MoMo Kotle payment failed');
        $this->assertSame(1, EmailNotificationLog::query()->where('notification_type', EmailNotificationTypes::PAYMENT_FAILED)->count());
        $this->assertSame(0, EmailNotificationLog::query()->where('notification_type', EmailNotificationTypes::SUBSCRIPTION_ACTIVATED)->count());
    }

    public function test_abandoned_payment_sends_one_abandoned_email(): void
    {
        $agent = User::factory()->create();
        Sanctum::actingAs($agent);
        $plan = $this->monthPlan();
        $this->fakePaystack($plan, verifiedStatus: 'abandoned');

        $reference = $this->postJson('/api/subscriptions/pay', [
            'subscription_plan_id' => $plan->id,
        ])->json('reference');
        $this->getJson("/api/subscriptions/payment/{$reference}")
            ->assertOk()
            ->assertJsonPath('data.status', 'Abandoned');

        Mail::assertSent(MoMoKotleMail::class, fn (MoMoKotleMail $mail) => $mail->subjectLine === 'MoMo Kotle payment was not completed');
        $this->assertSame(1, EmailNotificationLog::query()->where('notification_type', EmailNotificationTypes::PAYMENT_ABANDONED)->count());
        $this->assertSame(0, EmailNotificationLog::query()->where('notification_type', EmailNotificationTypes::SUBSCRIPTION_ACTIVATED)->count());
    }

    public function test_invalid_webhook_signature_sends_no_email(): void
    {
        $raw = json_encode([
            'event' => 'charge.success',
            'data' => ['reference' => 'unknown', 'status' => 'success'],
        ], JSON_THROW_ON_ERROR);

        $this->call(
            'POST',
            '/api/paystack/webhook',
            [],
            [],
            [],
            [
                'CONTENT_TYPE' => 'application/json',
                'HTTP_X_PAYSTACK_SIGNATURE' => hash_hmac('sha512', $raw, 'wrong-secret'),
            ],
            $raw
        )->assertUnauthorized();

        Mail::assertNotSent(MoMoKotleMail::class);
        $this->assertSame(0, EmailNotificationLog::query()->count());
    }

    public function test_unverified_checkout_does_not_send_success_email(): void
    {
        $agent = User::factory()->create();
        Sanctum::actingAs($agent);
        $plan = $this->monthPlan();
        $this->fakePaystack($plan);

        $this->postJson('/api/subscriptions/pay', [
            'subscription_plan_id' => $plan->id,
        ])->assertOk();

        $this->assertNull($agent->fresh()->active_subscription_id);
        Mail::assertNotSent(MoMoKotleMail::class, fn (MoMoKotleMail $mail) => str_contains($mail->subjectLine, 'Payment successful'));
        Mail::assertNotSent(MoMoKotleMail::class, fn (MoMoKotleMail $mail) => str_contains($mail->subjectLine, 'subscription is active'));
    }

    public function test_application_submit_and_status_change_emails(): void
    {
        $admin = $this->admin();
        $agent = User::factory()->create();
        $id = $this->submitApplication($agent);

        Mail::assertSent(MoMoKotleMail::class, fn (MoMoKotleMail $mail) => $mail->subjectLine === 'Application submitted — Agent Card Request');
        $this->assertSame(1, EmailNotificationLog::query()->where('notification_type', EmailNotificationTypes::APPLICATION_SUBMITTED)->count());

        $this->actingAs($admin)->patch("/admin/submissions/{$id}/status", [
            'status' => 'Pending',
            'admin_note' => 'Still reviewing',
        ])->assertRedirect();

        $this->assertSame(0, EmailNotificationLog::query()->where('notification_type', EmailNotificationTypes::APPLICATION_STATUS_CHANGED)->count());

        $this->actingAs($admin)->patch("/admin/submissions/{$id}/status", [
            'status' => 'Approved',
        ])->assertRedirect();

        Mail::assertSent(MoMoKotleMail::class, fn (MoMoKotleMail $mail) => $mail->subjectLine === 'Your Agent Card Request status has been updated');
        $this->assertSame(1, EmailNotificationLog::query()->where('notification_type', EmailNotificationTypes::APPLICATION_STATUS_CHANGED)->count());
    }

    public function test_rejected_application_email_includes_reason(): void
    {
        $admin = $this->admin();
        $agent = User::factory()->create();
        $id = $this->submitApplication($agent);

        $this->actingAs($admin)->patch("/admin/submissions/{$id}/status", [
            'status' => 'Rejected',
            'admin_note' => 'Please upload a clearer copy of the Ghana Card.',
        ])->assertRedirect();

        Mail::assertSent(MoMoKotleMail::class, function (MoMoKotleMail $mail) {
            return $mail->subjectLine === 'Your Agent Card Request status has been updated'
                && ($mail->rows['Reason'] ?? '') === 'Please upload a clearer copy of the Ghana Card.'
                && ($mail->rows['New status'] ?? '') === 'Rejected';
        });
    }

    public function test_failed_application_submit_sends_no_email(): void
    {
        $agent = User::factory()->create();
        Sanctum::actingAs($agent);
        $id = $this->getJson('/api/applications/next-id')->json('data.id');

        $this->postJson("/api/applications/{$id}/submit", [
            'form_type' => 'Agent Card Request',
            'form_data' => [
                'fullName' => 'Jane Agent',
                'phoneNumber' => '0244123456',
            ],
        ])->assertUnprocessable();

        Mail::assertNotSent(MoMoKotleMail::class, fn (MoMoKotleMail $mail) => str_contains($mail->subjectLine, 'Application submitted'));
        $this->assertSame(0, EmailNotificationLog::query()->where('notification_type', EmailNotificationTypes::APPLICATION_SUBMITTED)->count());
        $this->assertSame('Draft', AgentApplication::query()->findOrFail($id)->status);
    }

    public function test_email_failure_does_not_roll_back_application_and_is_logged(): void
    {
        Mail::swap(new class
        {
            public function to(mixed $users): self
            {
                return $this;
            }

            public function send(mixed $view): never
            {
                throw new \RuntimeException('SMTP unavailable');
            }
        });

        $agent = User::factory()->create();
        $id = $this->submitApplication($agent);

        $this->assertSame('Pending', AgentApplication::query()->findOrFail($id)->status);
        $this->assertSame('failed', EmailNotificationLog::query()->where('event_key', 'application.submitted.'.$id)->value('status'));
        $this->assertStringContainsString('SMTP unavailable', (string) EmailNotificationLog::query()->where('event_key', 'application.submitted.'.$id)->value('failure_reason'));
    }

    public function test_admin_can_view_email_log_page(): void
    {
        $this->actingAs($this->admin())
            ->get('/admin/email-notifications')
            ->assertOk()
            ->assertSee('Email Log');
    }

    public function test_admin_alert_uses_configured_recipients(): void
    {
        BusinessSetting::query()->updateOrCreate(
            ['key' => 'admin_notification_emails'],
            ['value' => 'ops@momokotle.test']
        );
        app(\App\Services\BusinessSettingsService::class)->forgetCache();

        $this->registerVerifiedAgent();

        Mail::assertSent(MoMoKotleMail::class, fn (MoMoKotleMail $mail) => $mail->subjectLine === 'New MoMo Kotle agent registered');
        $this->assertTrue(
            EmailNotificationLog::query()
                ->where('notification_type', EmailNotificationTypes::ADMIN_USER_REGISTERED)
                ->where('recipient_email', 'ops@momokotle.test')
                ->exists()
        );
    }

    public function test_dispatcher_is_idempotent_for_the_same_event_key(): void
    {
        $agent = User::factory()->create();
        $service = app(EmailNotificationService::class);

        $first = $service->sendOnce(
            eventKey: 'test.duplicate.1',
            type: EmailNotificationTypes::ACCOUNT_WELCOME,
            category: EmailNotificationTypes::CATEGORY_ACCOUNT,
            recipientEmail: $agent->email,
            subject: 'Welcome to MoMo Kotle',
            title: 'Welcome',
            greeting: 'Hello,',
            intro: 'Created',
            user: $agent,
        );
        $second = $service->sendOnce(
            eventKey: 'test.duplicate.1',
            type: EmailNotificationTypes::ACCOUNT_WELCOME,
            category: EmailNotificationTypes::CATEGORY_ACCOUNT,
            recipientEmail: $agent->email,
            subject: 'Welcome to MoMo Kotle',
            title: 'Welcome',
            greeting: 'Hello,',
            intro: 'Created again',
            user: $agent,
        );

        $this->assertSame($first?->id, $second?->id);
        $this->assertSame(1, EmailNotificationLog::query()->where('event_key', 'test.duplicate.1')->count());
        Mail::assertSent(MoMoKotleMail::class, 1);
    }

    private function registerVerifiedAgent(): User
    {
        $payload = [
            'first_name' => 'Kofi',
            'last_name' => 'Mensah',
            'email' => 'kofi.emailtest@example.com',
            'phone_number' => '0244123456',
            'location' => 'Accra',
            'password' => 'secret123',
            'password_confirmation' => 'secret123',
        ];

        $this->postJson('/api/auth/register', $payload)->assertOk();

        $otpCode = null;
        Mail::assertSent(RegistrationOtpMail::class, function (RegistrationOtpMail $mail) use (&$otpCode) {
            $otpCode = $mail->otpCode;

            return true;
        });

        $this->postJson('/api/auth/register/verify', [
            'email' => $payload['email'],
            'otp_code' => $otpCode,
        ])->assertCreated();

        return User::query()->where('email', $payload['email'])->firstOrFail();
    }

    private function submitApplication(User $agent): string
    {
        Sanctum::actingAs($agent);
        $id = $this->getJson('/api/applications/next-id')->json('data.id');

        $this->postJson("/api/applications/{$id}/documents", [
            'slot_id' => 'ghanaCardFront',
            'file' => UploadedFile::fake()->image('front.jpg'),
        ])->assertOk();

        $this->postJson("/api/applications/{$id}/submit", [
            'form_type' => 'Agent Card Request',
            'form_data' => [
                'fullName' => 'Jane Agent',
                'phoneNumber' => '0244123456',
                'businessName' => 'Jane Trading',
            ],
        ])->assertOk();

        return $id;
    }

    private function admin(): User
    {
        return User::query()->where('email', 'admin@momokotle.test')->firstOrFail();
    }

    private function monthPlan(): SubscriptionPlan
    {
        return SubscriptionPlan::query()->where('status', 'Active')->orderBy('duration_days')->firstOrFail();
    }

    private function fakePaystack(SubscriptionPlan $plan, string $verifiedStatus = 'success'): void
    {
        $amount = PaystackMoney::toSubunits((float) $plan->price);

        Http::fake(function ($request) use ($verifiedStatus, $amount) {
            $url = $request->url();
            if (str_contains($url, '/transaction/initialize')) {
                $body = json_decode($request->body(), true) ?: [];
                $reference = (string) ($body['reference'] ?? 'ref');

                return Http::response([
                    'status' => true,
                    'data' => [
                        'authorization_url' => 'https://checkout.paystack.com/test',
                        'access_code' => 'acc_'.$reference,
                        'reference' => $reference,
                    ],
                ]);
            }

            if (str_contains($url, '/transaction/verify/')) {
                $reference = basename(parse_url($url, PHP_URL_PATH) ?: '');

                return Http::response([
                    'status' => true,
                    'data' => [
                        'id' => 12345,
                        'status' => $verifiedStatus,
                        'reference' => $reference,
                        'amount' => $amount,
                        'currency' => 'GHS',
                    ],
                ]);
            }

            return Http::response(['status' => false], 404);
        });
    }
}
