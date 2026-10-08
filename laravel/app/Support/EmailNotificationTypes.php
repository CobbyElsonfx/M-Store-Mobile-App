<?php

namespace App\Support;

class EmailNotificationTypes
{
    public const ACCOUNT_WELCOME = 'account.welcome';

    public const ACCOUNT_UPDATED = 'account.updated';

    public const ACCOUNT_SUSPENDED = 'account.suspended';

    public const SUBSCRIPTION_ACTIVATED = 'subscription.activated';

    public const SUBSCRIPTION_RENEWED = 'subscription.renewed';

    public const SUBSCRIPTION_EXPIRED = 'subscription.expired';

    public const SUBSCRIPTION_EXPIRING = 'subscription.expiring';

    public const PAYMENT_SUCCESSFUL = 'payment.successful';

    public const PAYMENT_FAILED = 'payment.failed';

    public const PAYMENT_ABANDONED = 'payment.abandoned';

    public const APPLICATION_SUBMITTED = 'application.submitted';

    public const APPLICATION_STATUS_CHANGED = 'application.status_changed';

    public const ADMIN_USER_REGISTERED = 'admin.user_registered';

    public const ADMIN_APPLICATION_SUBMITTED = 'admin.application_submitted';

    public const ADMIN_PAYMENT_SUCCESSFUL = 'admin.payment_successful';

    public const ADMIN_PAYMENT_FAILED = 'admin.payment_failed';

    public const CATEGORY_ACCOUNT = 'account';

    public const CATEGORY_SUBSCRIPTION = 'subscription';

    public const CATEGORY_PAYMENT = 'payment';

    public const CATEGORY_APPLICATION = 'application';

    public const CATEGORY_ADMIN = 'admin';

    /** Non-critical types that can be disabled from admin settings. */
    public const OPTIONAL_TYPES = [
        self::SUBSCRIPTION_EXPIRING,
        self::ADMIN_USER_REGISTERED,
        self::ADMIN_APPLICATION_SUBMITTED,
        self::ADMIN_PAYMENT_SUCCESSFUL,
        self::ADMIN_PAYMENT_FAILED,
    ];
}
