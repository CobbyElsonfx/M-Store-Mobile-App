@php
    $user = auth()->user();
    $initials = strtoupper(substr($user->first_name ?? $user->name ?? 'A', 0, 1).substr($user->last_name ?? '', 0, 1));
@endphp

<aside class="admin-sidebar">
    <div class="sidebar-brand">
        <strong>MoMo Kotle</strong>
        <span>Admin Console</span>
    </div>

    <nav class="sidebar-nav" aria-label="Admin navigation">
        <div class="nav-section">
            <div class="nav-section-label">Overview</div>
            <a href="{{ route('admin.dashboard') }}" @class(['nav-link', 'active' => request()->routeIs('admin.dashboard')])>
                <span class="nav-icon">◫</span> Dashboard
            </a>
            <a href="{{ route('admin.reports.index') }}" @class(['nav-link', 'active' => request()->routeIs('admin.reports.*')])>
                <span class="nav-icon">▤</span> Reports
            </a>
        </div>

        <div class="nav-section">
            <div class="nav-section-label">Operations</div>
            <a href="{{ route('admin.orders.index') }}" @class(['nav-link', 'active' => request()->routeIs('admin.orders.*')])>
                <span class="nav-icon">◈</span> Orders
            </a>
            <a href="{{ route('admin.submissions.index') }}" @class(['nav-link', 'active' => request()->routeIs('admin.submissions.*')])>
                <span class="nav-icon">▣</span> Submissions
            </a>
            <a href="{{ route('admin.withdrawals.index') }}" @class(['nav-link', 'active' => request()->routeIs('admin.withdrawals.*')])>
                <span class="nav-icon">◉</span> Withdrawals
            </a>
            <a href="{{ route('admin.notifications.create') }}" @class(['nav-link', 'active' => request()->routeIs('admin.notifications.*')])>
                <span class="nav-icon">◔</span> Notifications
            </a>
        </div>

        <div class="nav-section">
            <div class="nav-section-label">Catalog</div>
            <a href="{{ route('admin.products.index') }}" @class(['nav-link', 'active' => request()->routeIs('admin.products.*')])>
                <span class="nav-icon">▦</span> Products
            </a>
            <a href="{{ route('admin.subscription-plans.index') }}" @class(['nav-link', 'active' => request()->routeIs('admin.subscription-plans.*')])>
                <span class="nav-icon">▧</span> Subscription Plans
            </a>
        </div>

        <div class="nav-section">
            <div class="nav-section-label">Users & Finance</div>
            <a href="{{ route('admin.users.index') }}" @class(['nav-link', 'active' => request()->routeIs('admin.users.*')])>
                <span class="nav-icon">◍</span> Users
            </a>
            <a href="{{ route('admin.wallets.index') }}" @class(['nav-link', 'active' => request()->routeIs('admin.wallets.*')])>
                <span class="nav-icon">◎</span> Wallets
            </a>
            <a href="{{ route('admin.referrals.index') }}" @class(['nav-link', 'active' => request()->routeIs('admin.referrals.*')])>
                <span class="nav-icon">◑</span> Referrals
            </a>
            <a href="{{ route('admin.subscriptions.index') }}" @class(['nav-link', 'active' => request()->routeIs('admin.subscriptions.*')])>
                <span class="nav-icon">◐</span> Subscriptions
            </a>
            <a href="{{ route('admin.revenue.index') }}" @class(['nav-link', 'active' => request()->routeIs('admin.revenue.*')])>
                <span class="nav-icon">◒</span> Revenue
            </a>
            <a href="{{ route('admin.commissions.index') }}" @class(['nav-link', 'active' => request()->routeIs('admin.commissions.*')])>
                <span class="nav-icon">◓</span> Commissions
            </a>
        </div>

        <div class="nav-section">
            <div class="nav-section-label">System</div>
            <a href="{{ route('admin.settings.business.edit') }}" @class(['nav-link', 'active' => request()->routeIs('admin.settings.*')])>
                <span class="nav-icon">⚙</span> Business Settings
            </a>
            <a href="{{ route('admin.audit-log.index') }}" @class(['nav-link', 'active' => request()->routeIs('admin.audit-log.*')])>
                <span class="nav-icon">◷</span> Audit Log
            </a>
        </div>
    </nav>

    <div class="sidebar-footer">
        <div class="sidebar-user">
            <div class="sidebar-avatar">{{ $initials }}</div>
            <div class="sidebar-user-info">
                <strong>{{ $user->name }}</strong>
                <small>{{ $user->email }}</small>
            </div>
        </div>
        <form method="POST" action="{{ route('admin.logout') }}">
            @csrf
            <button class="sidebar-logout" type="submit">Sign out</button>
        </form>
    </div>
</aside>
