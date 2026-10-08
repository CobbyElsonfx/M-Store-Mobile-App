# Google Play upload — MoMo Kotle

Use this checklist in Play Console. The signed Android App Bundle is built from `m-storepay`.

## Package (already set in the app)

| Field | Value |
|---|---|
| App name (in-app) | M Storepay |
| Suggested Play title | MoMo Kotle |
| Package name | `com.momokotle.agent` |
| Version name | `1.0.6` |
| Version code | `7` |
| Min Android | 8.0 (API 26) |
| Target Android | 16 (API 36) |
| Default language | English |
| API base URL | `https://test.ihilengineeringltd.com/` |
| Privacy policy | https://test.ihilengineeringltd.com/privacy |
| Account deletion | https://test.ihilengineeringltd.com/account-deletion |
| Support email (in-app) | support@mstorepay.com |
| Support phone (placeholder) | +233 30 000 0000 |

**Do not change the package name after the first upload.**

## Files to upload

1. Open Play Console → Create app → **MoMo Kotle** (or M Storepay — pick one brand and keep it).
2. Upload the AAB: `m-storepay/app/build/outputs/bundle/release/app-release.aab`
3. App signing: let Google manage the **app signing key**. You keep the **upload key** created on this PC.

### Upload key (keep a backup off this PC)

- File: `m-storepay/keystore/play-upload.jks`
- Alias: `momokotle-upload`
- Passwords: `m-storepay/keystore.properties` (not in git)

If you lose this keystore, you must ask Google to reset the upload key.

## Store listing (you type these in Play Console)

### Short description (80 characters max)

```
MoMo agent app for Ghana: USSD Cash In, Cash Out, airtime, and subscriptions.
```

### Full description

```
MoMo Kotle helps MTN Mobile Money agents in Ghana run everyday USSD jobs from one app.

• Cash In, Cash Out, merchant pay, airtime, balance, and commission
• Accessibility automation fills the USSD menu; you type the PIN yourself
• Works offline for USSD after your subscription has been verified
• Contacts, transaction history, and commission preview
• Store, referrals, wallet, and service applications for subscribed agents

Requires an MTN agent SIM, call permission, and the MoMo Kotle Accessibility service. An active subscription or trial is required to start USSD automation.
```

### Category

- Application type: **App**
- Category: **Finance** or **Business**
- Tags: mobile money, MTN, agent, Ghana, USSD

### Graphics you must create

| Asset | Size | Notes |
|---|---|---|
| App icon | 512 × 512 PNG | 32-bit, no transparency. Export from the yellow launcher. |
| Feature graphic | 1024 × 500 PNG | Banner for the store page |
| Phone screenshots | at least 2, up to 8 | 16:9 or 9:16; Home, Cash In, Subscription, Settings |
| 7-inch tablet screenshots | optional | |
| Promo video | optional | YouTube URL |

Take screenshots on a real phone after installing the release build.

## Contact and legal

| Field | Suggested value |
|---|---|
| Developer name | IHIL Engineering Ltd (or your legal entity) |
| Contact email | A mailbox you monitor (replace support@mstorepay.com if unused) |
| Contact phone | Real Ghana number (the in-app +233 30 000 0000 is a placeholder) |
| Website | https://test.ihilengineeringltd.com |
| Privacy policy URL | https://test.ihilengineeringltd.com/privacy |
| Account deletion URL | https://test.ihilengineeringltd.com/account-deletion |

Deploy the Laravel `/privacy` and `/account-deletion` pages on that domain before you submit. Play will open the URLs.

## App content declarations

### Privacy / Data safety (declare what the app actually does)

Collected and sent to your server:

- Name, email, phone, location
- Account / subscription status
- Payment reference (Paystack), not full card numbers
- Form submissions and uploaded documents
- Device network status

Collected on device only:

- Local transaction history
- Saved contacts
- Auth token and subscription cache

Data uses: **App functionality**, **Account management**, **Payments**.  
Not sold. Encrypted in transit (HTTPS). Users can request deletion.

### Permissions Play will ask about

| Permission | Why |
|---|---|
| Phone / Call | Dial `*171#` and other USSD codes |
| Phone state | Choose the agent SIM |
| Accessibility | Read/fill MTN USSD dialogs only; PIN is never typed by the app |
| Internet | Login, sync, Paystack, store |

In Play Console → App content → **Sensitive permissions**, declare Phone and Accessibility with this core functionality: *agent Mobile Money USSD*.

### Ads

No ads.

### Target audience

18+ (financial / agent tool). Not designed for children.

### News / COVID / Data safety / Government apps

No, unless you later add those features.

### Financial features

Yes — subscriptions via Paystack and MoMo agent workflows. Complete the **Financial features** form. This is not a bank app; it assists existing MTN agent USSD.

## Policy risk you must plan for

Google often rejects Accessibility services that automate other apps unless they are clearly for users with disabilities.

Be ready to explain:

- The service is limited to the phone/USSD packages listed in `accessibility_service_config.xml`
- The user opts in from Android Settings
- The user always enters the PIN
- It is for licensed MoMo agents, not background scraping

If Play rejects Accessibility, keep the rest of the app and ask users to complete USSD manually until you get an exception.

## Testing tracks

1. **Internal testing** first (up to 100 testers) with the AAB
2. Closed testing if needed
3. Production after review

Testers need a Gmail account added to the tester list.

## Before you press Submit

- [ ] Laravel is live on `test.ihilengineeringltd.com` with valid SSL for that hostname
- [ ] `/privacy` and `/account-deletion` return 200
- [ ] Paystack keys on the server are the ones you intend for public testers
- [ ] In-app support email and phone are real
- [ ] 512×512 icon and 1024×500 feature graphic uploaded
- [ ] At least 2 phone screenshots
- [ ] Content rating questionnaire completed (IARC)
- [ ] Data safety form completed
- [ ] Phone + Accessibility declarations completed
- [ ] Upload keystore backed up

## Build command (already configured)

```bat
cd m-storepay
gradlew :app:bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab`
