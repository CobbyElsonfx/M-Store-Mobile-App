# API Architecture — MoMo Kotle Laravel Backend

This document describes the **Laravel REST API** — the **single authoritative backend** for the MoMo Kotle system.

**Target runtime:** Kotlin → Laravel API → PostgreSQL. Firebase is **not** a runtime fallback. Legacy Firebase data is exported once during migration (see [LARAVEL_MIGRATION_PLAN.md](./LARAVEL_MIGRATION_PLAN.md)).

---

## 1. Overview

```
Kotlin App  ──HTTPS──►  Laravel /api/*  ──►  Database
                         │
                         ├── Sanctum (mobile bearer tokens)
                         └── Form Requests + API Resources
```

**Rules:**

- Kotlin must **never** connect directly to PostgreSQL
- Kotlin must **never** use Firebase as a fallback when Laravel is the cutover backend for a module
- All financial calculations must be server-side in Laravel
- USSD/local transaction data stays on device (no API upload required)

---

## 2. Base URL

| Environment | URL |
|-------------|-----|
| Local | `http://localhost:8000/api` |
| Production | `https://{your-domain}/api` |

Configure in Kotlin via existing `BuildConfig.API_BASE_URL` (currently points to legacy `api.mstorepay.com` — update when switching).

---

## 3. Authentication

**Mechanism:** [Laravel Sanctum](https://laravel.com/docs/sanctum) personal access tokens

| Endpoint | Method | Auth | Description |
|----------|--------|------|-------------|
| `/api/health` | GET | None | Service health check |
| `/api/auth/register` | POST | None | Agent registration |
| `/api/auth/login` | POST | None | Agent login (non-admin only) |
| `/api/auth/logout` | POST | Bearer | Revoke current token |
| `/api/auth/me` | GET | Bearer | Current user profile |
| `/api/status` | GET | Bearer | Auth smoke test |

### Register request

```json
{
  "first_name": "Jane",
  "last_name": "Agent",
  "email": "jane@example.com",
  "phone_number": "0244123456",
  "location": "Accra",
  "password": "secret123",
  "password_confirmation": "secret123",
  "referral_code": "ABC12345"
}
```

### Success response (201)

```json
{
  "message": "Registration successful.",
  "token": "...",
  "token_type": "Bearer",
  "user": {
    "id": 1,
    "email": "jane@example.com",
    "access_level": "full",
    "wallet": { "available_balance": 0 }
  }
}
```

### Authenticated requests

```
Authorization: Bearer {token}
Accept: application/json
```

### Admin restriction

Users with `role = admin` **cannot** use `/api/auth/login`. Admins use `/admin/login` (session).

---

## 4. Response Conventions

### Success

- `200` — OK
- `201` — Created
- `204` — No content (future deletes)

### Errors

```json
{
  "message": "Human-readable error"
}
```

Validation errors (422):

```json
{
  "message": "The email field is required.",
  "errors": {
    "email": ["The email field is required."]
  }
}
```

---

## 5. Planned Endpoints (not yet implemented)

Mapped from **existing mobile features**:

| Group | Endpoints | Replaces |
|-------|-----------|----------|
| **Users** | `GET/PUT /api/users/me` | Firestore `users` |
| **Products** | `GET /api/products` | Firestore `storeProducts` |
| **Orders** | `GET/POST /api/orders` | Firestore `storeOrders` |
| **Applications** | `GET/POST /api/applications`, `POST /api/applications/{id}/documents` | Firestore + Storage |
| **Subscriptions** | `GET /api/subscription-plans`, `POST /api/subscriptions` | Firestore plans/subscriptions |
| **Wallet** | `GET /api/wallet`, `GET /api/wallet/transactions` | Firestore wallets |
| **Withdrawals** | `POST /api/withdrawals` | Firestore withdrawal_requests |
| **Referrals** | `GET /api/referrals` | Firestore referrals |
| **Notifications** | `GET /api/notifications`, `PATCH /api/notifications/{id}/read` | Firestore notifications |
| **Settings** | `GET /api/settings/business`, `GET /api/settings/app` | Firestore settings docs |

**Not planned for API:** USSD transaction history (local Room only).

---

## 6. Kotlin Integration Path

**Legacy (being removed per module):**

```
RepositoryImpl → FirestoreDataSource → Firebase
```

**Target (sole runtime path after cutover):**

```
RepositoryImpl → LaravelApiDataSource → Retrofit → Laravel /api/*
```

**Existing assets to reuse:**

- `di/NetworkModule.kt` — Retrofit + OkHttp already configured
- `data/remote/MStorepayApiService.kt` — extend interface (do not duplicate)
- Repository interfaces in `domain/repository/` — unchanged contracts

**Cutover approach (no Firebase fallback):**

1. Add Sanctum `Authorization` interceptor to OkHttp
2. Implement `LaravelApiDataSource` for the module
3. Swap DI binding from `Firestore*DataSource` to Laravel implementation
4. Remove Firebase SDK calls for that module
5. UAT against Laravel + PostgreSQL only

Do **not** implement dual-read repositories or feature flags that fall back to Firebase.

## 7. Admin API vs Web

| Surface | Path | Auth |
|---------|------|------|
| Mobile REST | `/api/*` | Sanctum bearer |
| Admin web | `/admin/*` | Session cookie |
| Admin JSON (future) | `/api/admin/*` or web AJAX | Session + `admin` middleware |

Next.js admin is **temporary legacy UI** on Firebase. It is retired when Laravel `/admin` reaches parity — it is **not** wired to Laravel as a permanent adapter.

---

## 8. Security

- Passwords hashed with bcrypt
- Sanctum tokens revocable per device (`logout` deletes current token)
- Admin routes protected by `EnsureUserIsAdmin` middleware
- CORS configured when mobile production domain is known
- Rate limiting to be added on auth endpoints (Phase 1)

---

## 9. File Structure

```
laravel/
├── routes/api.php
├── app/Http/Controllers/Api/
├── app/Http/Requests/Api/
├── app/Http/Resources/
├── app/Services/
└── tests/Feature/ApiAuthTest.php
```

---

*Phase 0 — auth foundation only. See [LARAVEL_MIGRATION_PLAN.md](./LARAVEL_MIGRATION_PLAN.md) for full roadmap.*
