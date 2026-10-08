@extends('admin.layout')

@section('content')
<div class="card">
    <p class="card-lead">Delivery log for transactional emails. Failed emails never undo a successful payment, subscription, or application.</p>

    <form class="toolbar" method="GET" action="{{ route('admin.email-notifications.index') }}">
        <div>
            <label for="search">Search</label>
            <input id="search" name="search" type="search" value="{{ $search }}" placeholder="Email, subject, reference">
        </div>
        <div>
            <label for="type">Type</label>
            <select id="type" name="type">
                <option value="all" @selected($typeFilter === 'all')>All</option>
                @foreach ($types as $type)
                    <option value="{{ $type }}" @selected($typeFilter === $type)>{{ $type }}</option>
                @endforeach
            </select>
        </div>
        <div>
            <label for="status">Status</label>
            <select id="status" name="status">
                <option value="all" @selected($statusFilter === 'all')>All</option>
                @foreach ($statuses as $status)
                    <option value="{{ $status }}" @selected($statusFilter === $status)>{{ ucfirst($status) }}</option>
                @endforeach
            </select>
        </div>
        <div>
            <label>&nbsp;</label>
            <button type="submit">Filter</button>
        </div>
    </form>

    <div class="table-wrap"><table>
        <thead>
            <tr>
                <th>Sent</th>
                <th>Recipient</th>
                <th>Type</th>
                <th>Subject</th>
                <th>Status</th>
                <th>Reference</th>
                <th>Failure</th>
            </tr>
        </thead>
        <tbody>
            @forelse ($logs as $log)
                <tr>
                    <td>{{ $log->sent_at?->format('Y-m-d H:i') ?: $log->created_at?->format('Y-m-d H:i') }}</td>
                    <td>{{ $log->recipient_email }}</td>
                    <td>{{ $log->notification_type }}</td>
                    <td>{{ $log->subject }}</td>
                    <td><span class="badge {{ $log->status === 'sent' ? 'approved' : ($log->status === 'failed' ? 'rejected' : 'pending') }}">{{ $log->status }}</span></td>
                    <td>{{ $log->reference_id ?: '—' }}</td>
                    <td>{{ $log->failure_reason ?: '—' }}</td>
                </tr>
            @empty
                <tr><td colspan="7">No email notifications logged.</td></tr>
            @endforelse
        </tbody>
    </table></div>

    @include('admin.partials.pagination', ['paginator' => $logs])
</div>
@endsection
