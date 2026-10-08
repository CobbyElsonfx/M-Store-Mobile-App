<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="color-scheme" content="light">
    <title>{{ $title }}</title>
</head>
<body style="margin:0;padding:0;background:#F8FAFC;font-family:Arial,Helvetica,sans-serif;color:#0F172A;">
<table role="presentation" width="100%" cellspacing="0" cellpadding="0" style="background:#F8FAFC;padding:24px 12px;">
    <tr>
        <td align="center">
            <table role="presentation" width="100%" cellspacing="0" cellpadding="0" style="max-width:560px;background:#FFFFFF;border:1px solid #E5E7EB;border-radius:12px;overflow:hidden;">
                <tr>
                    <td style="background:#FACC15;padding:18px 24px;color:#141D30;font-size:18px;font-weight:700;">
                        MoMo Kotle
                    </td>
                </tr>
                <tr>
                    <td style="padding:24px;">
                        <h1 style="margin:0 0 12px;font-size:20px;line-height:1.3;color:#0F172A;">{{ $title }}</h1>
                        <p style="margin:0 0 16px;font-size:15px;line-height:1.5;color:#111827;">{{ $greeting }}</p>
                        <p style="margin:0 0 20px;font-size:15px;line-height:1.5;color:#111827;">{{ $intro }}</p>

                        @if (!empty($rows))
                            <table role="presentation" width="100%" cellspacing="0" cellpadding="0" style="margin:0 0 20px;border:1px solid #E5E7EB;border-radius:8px;background:#F8FAFC;">
                                @foreach ($rows as $label => $value)
                                    <tr>
                                        <td style="padding:10px 14px;border-bottom:1px solid #E5E7EB;font-size:13px;color:#6B7280;width:42%;">{{ $label }}</td>
                                        <td style="padding:10px 14px;border-bottom:1px solid #E5E7EB;font-size:14px;color:#0F172A;font-weight:600;">{{ $value }}</td>
                                    </tr>
                                @endforeach
                            </table>
                        @endif

                        @if ($nextSteps)
                            <p style="margin:0 0 16px;font-size:15px;line-height:1.5;color:#111827;">{{ $nextSteps }}</p>
                        @endif

                        @if ($note)
                            <p style="margin:0 0 8px;font-size:13px;line-height:1.5;color:#475569;">{{ $note }}</p>
                        @endif
                    </td>
                </tr>
                <tr>
                    <td style="padding:16px 24px;background:#F1F5F9;font-size:12px;line-height:1.5;color:#6B7280;">
                        Open the MoMo Kotle app for full details.
                        @if ($supportContact)
                            <br>Support: {{ $supportContact }}
                        @endif
                        <br>&copy; {{ date('Y') }} MoMo Kotle. All rights reserved.
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>
