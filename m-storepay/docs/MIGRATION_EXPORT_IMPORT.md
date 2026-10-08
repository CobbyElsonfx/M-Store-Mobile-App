# Firebase Export & Import — Phase 1

Read-only migration pipeline. **Firebase is not modified.** **No runtime fallback.**

## Overview

```
Firebase (legacy, read-only)
        │
        ▼  place JSON export on disk
JSON snapshot (local files)
        │
        ▼  php artisan migration:import-firebase
PostgreSQL (authoritative target)
        │
        ▼  php artisan migration:verify-firebase
Verification report
```

## Step 1 — Obtain export snapshot

Obtain a read-only Firebase/Firestore JSON export (with `manifest.json` and collection files) from your archive or export tooling, then copy it to:

`laravel/storage/app/migration/firebase-export/{timestamp}/`

**Do not** delete or edit live Firebase during migration.

## Step 2 — Import to PostgreSQL

Configure Laravel for PostgreSQL:

```env
DB_CONNECTION=pgsql
DB_HOST=127.0.0.1
DB_PORT=5432
DB_DATABASE=momo_kotle
DB_USERNAME=...
DB_PASSWORD=...
```

```bash
cd laravel
php artisan migrate
php artisan migration:import-firebase --latest
# or: php artisan migration:import-firebase --dir=/path/to/export
```

Import is **idempotent** (upsert by `firebase_uid`, document IDs, etc.).

Imported users receive random passwords — plan a password reset flow before cutover.

## Step 3 — Verify

```bash
php artisan migration:verify-firebase --latest
php artisan migration:verify-firebase --latest --report=storage/app/migration/verification-report.json
```

Review mismatches before any Kotlin module cutover.

## Step 4 — Sign-off (required before cutover)

- [ ] Export manifest archived
- [ ] Verification report passed or discrepancies explained
- [ ] Sample records manually spot-checked (users, orders, wallets)
- [ ] Stakeholder sign-off documented

Only after sign-off: proceed to Phase 2 module cutover (Kotlin → Laravel).

## Collections imported

| Firebase collection | PostgreSQL |
|---------------------|------------|
| users | users |
| admins | users (role=admin) |
| storeProducts | products |
| storeOrders | orders |
| wallets | wallets |
| wallet_transactions | wallet_transactions |
| subscription_plans | subscription_plans |
| subscriptions | subscriptions |
| referrals | referrals |
| form_submissions + formSubmissions | agent_applications + application_documents |
| business_settings | business_settings |
| appSettings | app_settings |

Additional collections (notifications, payments, withdrawals, commissions, audit_log) — extend importers in Phase 1b as needed.

## Tests

```bash
cd laravel
php artisan test --filter=FirebaseMigrationImport
```

Uses fixture at `tests/fixtures/firebase-export/sample/`.
