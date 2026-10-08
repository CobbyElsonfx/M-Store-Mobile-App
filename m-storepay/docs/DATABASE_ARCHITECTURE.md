# Database Architecture — MoMo Kotle Laravel Backend

**PostgreSQL** is the single authoritative production database for the target system.

Legacy data currently lives in Firebase Firestore. It will be **exported (read-only) → imported → verified** into PostgreSQL before each module cutover. Firebase is not a runtime database after migration.

---

## 1. Entity Relationship Diagram

```
users
 ├── subscriptions (user_id)
 ├── wallets (user_id PK)
 │     └── wallet_transactions (user_id)
 ├── orders (user_id)
 ├── agent_applications (user_id)
 │     └── application_documents (application_id)
 ├── notifications (user_id)
 ├── referrals (referrer_user_id, referred_user_id)
 ├── payments (user_id)
 └── withdrawal_requests (user_id)

subscription_plans
 └── subscriptions (plan_id)

products
 └── orders (product_id, nullable)

orders
 ├── commission_transactions (order_id)
 └── revenue_transactions (order_id)

users (role=admin)
 └── audit_logs (admin_user_id)

business_settings (key-value)
app_settings (key-value)
daily_counters (submission ID sequences)
```

**Note:** No `order_items` table — the existing Firestore model stores a single line item per order (`itemName`, `quantity`, `totalAmount`).

---

## 2. Tables

### `users` (extended Laravel default)

| Column | Type | Notes |
|--------|------|-------|
| id | bigint PK | Laravel default |
| name, email, password | | Standard auth |
| role | string | `agent` \| `admin` |
| first_name, last_name | string | Agent profile |
| phone_number, location | string | |
| account_status | string | `Active` \| `Suspended` |
| referral_code | string unique | Generated on registration |
| referred_by_user_id | FK users | Permanent referral link |
| trial_start_date, trial_end_date | timestamp | |
| subscription_status | string | `None`, `Trial`, `Active`, `Expired` |
| subscription_expiry_date | timestamp | |
| firebase_uid | string unique nullable | Import bridge |

### `subscription_plans`

| Column | Notes |
|--------|-------|
| id | UUID PK |
| plan_name, price, duration_days | Admin-managed |
| status, sort_order | |

**Seeded defaults:** 1 Month (6 GHS), 3 Months (15 GHS), 6 Months (30 GHS), 1 Year (65 GHS)

### `subscriptions`

Links user to plan with payment reference and date range.

### `wallets` / `wallet_transactions`

One wallet per user (`user_id` PK). Transactions log commission, bonuses, withdrawals.

### `products`

| Column | Notes |
|--------|-------|
| profit | Total product profit (GHS) |
| referral_percent, system_percent | Default 40/60; per-product override |

**Seeded defaults:** MTN Agent SIM (250 profit), Telecel/AirtelTigo/EVD (100 profit each)

### `orders`

Mirrors Firestore `storeOrders`. `commission_processed` flag matches existing admin logic.

### `referrals`

One row per referred user (`referred_user_id` unique). Permanent relationship.

### `commission_transactions` / `revenue_transactions`

Server-side audit of referral/system splits (replaces admin JS transaction).

### `payments` / `withdrawal_requests`

Mobile-initiated payments and withdrawal workflow.

### `agent_applications`

| Column | Notes |
|--------|-------|
| id | String PK `SUB202606210001` format |
| form_data | JSON |
| status | Pending, Approved, Rejected, etc. |

### `application_documents`

| Column | Notes |
|--------|-------|
| storage_path | Private disk path — **not** public URL |
| document_type | ghana_card_front, business_certificate, etc. |
| mime_type, file_size | Metadata only |

### `notifications`

Per-user notifications matching existing Firestore types.

### `business_settings` / `app_settings`

Key-value store replacing Firestore `business_settings/global` and `appSettings/global`.

| Key (business) | Default |
|----------------|---------|
| referral_commission_percent | 40 |
| system_commission_percent | 60 |
| monthly_subscription_bonus | 1 |
| minimum_withdrawal | 10 |
| trial_days | 7 |

### `audit_logs`

Admin action trail (replaces Firestore `audit_log`).

### `daily_counters`

Replaces Firestore `counters` for submission ID generation.

---

## 3. Firestore → SQL Mapping

| Firestore | SQL Table |
|-----------|-----------|
| `users` | `users` |
| `admins` | `users` where `role=admin` |
| `storeProducts` | `products` |
| `storeOrders` | `orders` |
| `form_submissions` | `agent_applications` + `application_documents` |
| `wallets` | `wallets` |
| `wallet_transactions` | `wallet_transactions` |
| `subscription_plans` | `subscription_plans` |
| `subscriptions` | `subscriptions` |
| `referrals` | `referrals` |
| `commission_transactions` | `commission_transactions` |
| `revenue_transactions` | `revenue_transactions` |
| `withdrawal_requests` | `withdrawal_requests` |
| `payments` | `payments` |
| `notifications` | `notifications` |
| `business_settings/global` | `business_settings` rows |
| `appSettings/global` | `app_settings` rows |
| `audit_log` | `audit_logs` |
| `counters` | `daily_counters` |

---

## 4. Local Android Storage (unchanged)

| Room Table | Migrates to Laravel? |
|------------|---------------------|
| `transactions` | **No** — USSD local history |
| `profit_rules` | **No** — MoMo commission tiers |
| `saved_contacts` | **No** — device contact book |
| `custom_flows` | **No** — USSD automation |
| `store_*`, `form_submissions` cache | **Yes** — source becomes Laravel API |

---

## 5. Migrations Location

```
laravel/database/migrations/
├── 2026_09_05_000001_extend_users_table_for_momo_agents.php
├── 2026_09_05_000002_create_subscription_plans_table.php
├── ...
└── 2026_09_05_000011_create_notifications_and_settings_tables.php
```

Run: `php artisan migrate`

Seed: `php artisan db:seed`

---

## 6. Indexes and Constraints

- Foreign keys on all user-owned records with `cascadeOnDelete` where appropriate
- Unique `referral_code`, `referred_user_id` on referrals
- Index on `orders(user_id, status)`, `notifications(user_id, is_read)`

---

*Schema reflects Phase 0 foundation. Import scripts and additional columns may be added during migration phases.*
