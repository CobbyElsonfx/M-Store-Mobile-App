{{ $title }}

{{ $greeting }}

{{ $intro }}

@foreach ($rows as $label => $value)
{{ $label }}: {{ $value }}
@endforeach

@if ($nextSteps)
{{ $nextSteps }}
@endif

@if ($note)
{{ $note }}
@endif

Open the MoMo Kotle app for full details.
@if ($supportContact)
Support: {{ $supportContact }}
@endif
