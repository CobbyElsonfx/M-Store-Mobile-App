# Deployment — MoMo Kotle

Deployment guide for the **monorepo** containing Android and Laravel backend.

---

## 1. Repository Layout

```
momo-kotle/
├── app/                 # Android (Gradle)
├── laravel/             # Laravel API + /admin (Composer)
└── docs/
```

Each component deploys independently.

---

## 2. Laravel Backend

### Requirements

- PHP 8.2+ (8.3 tested)
- Composer 2.x
- SQLite (local dev only) or **PostgreSQL** (production target)
- Extensions: `pdo`, `pdo_pgsql`, `mbstring`, `openssl`, `tokenizer`, `xml`, `ctype`, `json`, `bcmath`

**Not required:** Redis, Docker, Node.js, persistent queue workers (for basic operation)

### Local development

```bash
cd laravel
composer install
cp .env.example .env
php artisan key:generate
php artisan migrate
php artisan db:seed
php artisan serve
```

- API: http://localhost:8000/api/health
- Admin: http://localhost:8000/admin/login
- Seeded admin: `admin@momokotle.test` / `password`

### Database selection

Edit `.env`:

```env
# Local development (SQLite)
DB_CONNECTION=sqlite

# Production — authoritative backend
DB_CONNECTION=pgsql
DB_HOST=127.0.0.1
DB_PORT=5432
DB_DATABASE=momo_kotle
DB_USERNAME=...
DB_PASSWORD=...
```

PostgreSQL is the target production database. Use Laravel migrations — no database-specific SQL in application code.

### Shared hosting deployment

Typical layout:

1. Upload `laravel/` project files (exclude `node_modules`, `tests` optional)
2. Point web root to `laravel/public/`
3. Set document root `.htaccess` (Apache) or nginx equivalent
4. Configure `.env` with production values
5. Run once via SSH or host panel:
   ```bash
   composer install --no-dev --optimize-autoloader
   php artisan migrate --force
   php artisan config:cache
   php artisan route:cache
   php artisan view:cache
   ```
6. Ensure `storage/` and `bootstrap/cache/` are writable

### Queue / cron (optional)

Default `.env` uses `QUEUE_CONNECTION=database` — compatible with shared hosting.

For scheduled tasks (subscription expiry, referral bonuses):

```cron
* * * * * cd /path/to/laravel && php artisan schedule:run >> /dev/null 2>&1
```

No Redis required.

### Private file storage

Documents stored on `agent_documents` disk:

```
storage/app/private/agent_documents/{user_id}/{application_id}/{filename}
```

Files served only through authorized Laravel routes (to be implemented Phase 2).

Run `php artisan storage:link` only for **public** assets (product images if needed).

---

## 3. Android App

```bash
./gradlew :app:assembleDebug
```

Debug builds use `API_BASE_URL=http://10.0.2.2:8000/` in `app/build.gradle.kts`. Set the release URL to your production Laravel domain.

---

## 4. Environment Variables Summary

### Laravel (`laravel/.env`)

| Variable | Purpose |
|----------|---------|
| `APP_URL` | Base URL |
| `DB_*` | Database connection |
| `FILESYSTEM_DISK` | Default disk (`local`) |
| `QUEUE_CONNECTION` | `database` for shared hosting |
| `SESSION_DRIVER` | `database` |

### Android

| Setting | Location |
|---------|----------|
| `API_BASE_URL` | `app/build.gradle.kts` |

---

## 5. Migration Deployment Strategy

| Phase | Laravel + PostgreSQL | Kotlin |
|-------|---------------------|--------|
| Current | Authoritative backend + `/admin` | Laravel REST API only |
| Optional | Import legacy Firebase JSON snapshot | — |

**Rules:**

- No Firebase runtime fallback for Laravel
- Do not delete or mutate Firebase until export/import verification is complete and migration is formally signed off
- Each module cutover is hard switch to Laravel + PostgreSQL

---

## 6. Verification Checklist

After deployment:

- [ ] `GET /api/health` returns 200
- [ ] `POST /api/auth/register` creates user + wallet
- [ ] Admin login at `/admin/login` works for `role=admin`
- [ ] Non-admin blocked from `/admin`
- [ ] `php artisan test` passes
- [ ] Android `./gradlew :app:compileDebugKotlin` succeeds

---

## 7. Security Production Notes

- Change seeded admin password immediately
- Set `APP_DEBUG=false`
- Use HTTPS everywhere
- Do not commit `.env`

---

*See [LARAVEL_MIGRATION_PLAN.md](./LARAVEL_MIGRATION_PLAN.md) for migration phases.*
