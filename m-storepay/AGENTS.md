# MoMo Kotle agent notes

The Android app default is a **professional light theme** with MTN yellow as Material 3 primary. Do not follow the system dark preference in `MainActivity`. Dark color-scheme architecture may remain in `Theme.kt` unused.

## Surfaces (Android)

| Token | Hex | Use |
|---|---|---|
| Primary | `#FACC15` | MTN yellow buttons, focus rings, selected chips, brand marks |
| On primary | `#141D30` | Text/icons on yellow |
| Background | `#F8FAFC` | Page / scaffold |
| Surface / card | `#FFFFFF` | Cards, dialogs |
| Surface variant | `#F1F5F9` | Subtle wells, disabled fills |
| Field well | `#E2E8F0` | Inputs, selects, textareas, search |
| Ink / body | `#0F172A` / `#111827` | Typed input and main text |
| Muted | `#6B7280` / `#475569` | Labels, placeholders, helper text |
| Outline | `#E5E7EB` | Card and button borders |
| Field border | `#94A3B8` | Input outlines |
| Success | `#16A34A` | ON / approved / completed |
| Warning | `#F59E0B` | Pending / accessibility off |
| Error | `#DC2626` | Failed / rejected |
| Info | `#2563EB` | Informational banners |

## Admin web

Admin CSS lives in `laravel/resources/views/admin/partials/styles.blade.php`. Keep the daylight admin chrome. Do not hardcode dark navy sidebars.

## Rules

- App form colors live in `app/src/main/java/com/momokotle/agent/ui/theme/AppFormStyles.kt` and `Color.kt`.
- `MomoKotleTheme(darkTheme = false)` in `MainActivity`.
- Typed text in fields must stay `#0F172A` on `#E2E8F0`. Never fade disabled date/select values.
- Do not use `colorScheme.primary` for body/amount text (it is yellow). Use `onSurface` / `onBackground` or status tokens.
- New screens reuse `AppTextField`, `AppButton`, `AppCard`, `FormSelectField`, `FormDateField`.
