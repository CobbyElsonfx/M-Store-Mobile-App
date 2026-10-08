<style>
    :root {
        --sidebar-width: 260px;
        --topbar-height: 64px;
        --color-bg: #f1f5f9;
        --color-surface: #ffffff;
        --color-border: #e2e8f0;
        --color-text: #0f172a;
        --color-muted: #64748b;
        --color-sidebar: #0f172a;
        --color-sidebar-hover: #1e293b;
        --color-accent: #10b981;
        --color-accent-soft: rgba(16, 185, 129, 0.12);
        --color-danger: #dc2626;
        --color-danger-soft: #fef2f2;
        --color-success-soft: #ecfdf5;
        --color-warning-soft: #fffbeb;
        --radius-sm: 8px;
        --radius-md: 12px;
        --radius-lg: 16px;
        --shadow-sm: 0 1px 2px rgba(15, 23, 42, 0.05);
        --shadow-md: 0 8px 24px rgba(15, 23, 42, 0.08);
        --font: "Segoe UI", system-ui, -apple-system, BlinkMacSystemFont, sans-serif;
    }

    *, *::before, *::after { box-sizing: border-box; }

    body {
        margin: 0;
        font-family: var(--font);
        background: var(--color-bg);
        color: var(--color-text);
        line-height: 1.5;
        -webkit-font-smoothing: antialiased;
    }

    a { color: #047857; text-decoration: none; }
    a:hover { color: #065f46; }

    /* Shell */
    .admin-shell {
        display: flex;
        min-height: 100vh;
    }

    /* Sidebar */
    .admin-sidebar {
        width: var(--sidebar-width);
        background: var(--color-sidebar);
        color: #e2e8f0;
        display: flex;
        flex-direction: column;
        position: fixed;
        inset: 0 auto 0 0;
        z-index: 40;
        border-right: 1px solid rgba(255, 255, 255, 0.06);
    }

    .sidebar-brand {
        padding: 1.25rem 1.25rem 1rem;
        border-bottom: 1px solid rgba(255, 255, 255, 0.08);
    }

    .sidebar-brand strong {
        display: block;
        font-size: 1.05rem;
        color: #fff;
        letter-spacing: -0.02em;
    }

    .sidebar-brand span {
        display: block;
        margin-top: 0.15rem;
        font-size: 0.75rem;
        color: #94a3b8;
        text-transform: uppercase;
        letter-spacing: 0.08em;
    }

    .sidebar-nav {
        flex: 1;
        overflow-y: auto;
        padding: 1rem 0.75rem 1.5rem;
    }

    .nav-section {
        margin-bottom: 1.25rem;
    }

    .nav-section-label {
        padding: 0 0.75rem;
        margin-bottom: 0.35rem;
        font-size: 0.68rem;
        font-weight: 600;
        text-transform: uppercase;
        letter-spacing: 0.1em;
        color: #64748b;
    }

    .nav-link {
        display: flex;
        align-items: center;
        gap: 0.65rem;
        padding: 0.6rem 0.75rem;
        margin-bottom: 0.15rem;
        border-radius: var(--radius-sm);
        color: #cbd5e1;
        font-size: 0.9rem;
        font-weight: 500;
        transition: background 0.15s, color 0.15s;
    }

    .nav-link:hover {
        background: var(--color-sidebar-hover);
        color: #fff;
    }

    .nav-link.active {
        background: var(--color-accent-soft);
        color: #6ee7b7;
        box-shadow: inset 3px 0 0 var(--color-accent);
    }

    .nav-icon {
        width: 1.1rem;
        text-align: center;
        opacity: 0.85;
        font-size: 0.85rem;
    }

    .sidebar-footer {
        padding: 1rem 0.75rem;
        border-top: 1px solid rgba(255, 255, 255, 0.08);
    }

    .sidebar-user {
        display: flex;
        align-items: center;
        gap: 0.65rem;
        padding: 0.65rem 0.75rem;
        border-radius: var(--radius-sm);
        background: rgba(255, 255, 255, 0.04);
    }

    .sidebar-avatar {
        width: 36px;
        height: 36px;
        border-radius: 999px;
        background: linear-gradient(135deg, #10b981, #059669);
        color: #fff;
        display: grid;
        place-items: center;
        font-size: 0.8rem;
        font-weight: 700;
        flex-shrink: 0;
    }

    .sidebar-user-info {
        min-width: 0;
        flex: 1;
    }

    .sidebar-user-info strong {
        display: block;
        font-size: 0.82rem;
        color: #f8fafc;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .sidebar-user-info small {
        display: block;
        color: #94a3b8;
        font-size: 0.72rem;
    }

    .sidebar-logout {
        margin-top: 0.65rem;
        width: 100%;
        padding: 0.55rem 0.75rem;
        border: 1px solid rgba(255, 255, 255, 0.12);
        border-radius: var(--radius-sm);
        background: transparent;
        color: #cbd5e1;
        font: inherit;
        font-size: 0.85rem;
        cursor: pointer;
        transition: background 0.15s, color 0.15s;
    }

    .sidebar-logout:hover {
        background: rgba(255, 255, 255, 0.06);
        color: #fff;
    }

    /* Main area */
    .admin-main {
        flex: 1;
        margin-left: var(--sidebar-width);
        min-width: 0;
        display: flex;
        flex-direction: column;
        min-height: 100vh;
    }

    .admin-topbar {
        height: var(--topbar-height);
        background: var(--color-surface);
        border-bottom: 1px solid var(--color-border);
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 1.75rem;
        position: sticky;
        top: 0;
        z-index: 30;
        box-shadow: var(--shadow-sm);
    }

    .admin-topbar h1 {
        margin: 0;
        font-size: 1.25rem;
        font-weight: 600;
        letter-spacing: -0.02em;
    }

    .admin-topbar-meta {
        font-size: 0.85rem;
        color: var(--color-muted);
    }

    .admin-content {
        flex: 1;
        padding: 1.5rem 1.75rem 2rem;
    }

    /* Cards & layout */
    .card {
        background: var(--color-surface);
        border: 1px solid var(--color-border);
        border-radius: var(--radius-lg);
        padding: 1.5rem;
        box-shadow: var(--shadow-sm);
    }

    .card + .card { margin-top: 1rem; }

    .card-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        gap: 1rem;
        flex-wrap: wrap;
        margin-bottom: 1.25rem;
    }

    .card-header h2 {
        margin: 0;
        font-size: 1.05rem;
        font-weight: 600;
    }

    .card-header p {
        margin: 0.25rem 0 0;
        color: var(--color-muted);
        font-size: 0.9rem;
    }

    .page-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        gap: 1rem;
        flex-wrap: wrap;
        margin-bottom: 1.25rem;
    }

    .card-lead {
        margin: 0 0 1.25rem;
        color: var(--color-muted);
        font-size: 0.92rem;
    }

    .page-header h1,
    .card > h1 {
        margin: 0 0 0.35rem;
        font-size: 1.15rem;
        font-weight: 600;
    }

    .card > p:first-of-type,
    .page-header p {
        margin: 0 0 1rem;
        color: var(--color-muted);
        font-size: 0.9rem;
    }

    /* Stats */
    .stats-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        gap: 1rem;
        margin-bottom: 1.5rem;
    }

    .stat-box {
        background: var(--color-surface);
        border: 1px solid var(--color-border);
        border-radius: var(--radius-md);
        padding: 1rem 1.1rem;
        box-shadow: var(--shadow-sm);
    }

    .stat-box strong {
        display: block;
        font-size: 0.78rem;
        font-weight: 600;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        color: var(--color-muted);
    }

    .stat-box p {
        margin: 0.5rem 0 0;
        font-size: 1.5rem;
        font-weight: 700;
        letter-spacing: -0.03em;
        color: var(--color-text);
    }

    .stat-box.accent {
        border-color: #a7f3d0;
        background: linear-gradient(180deg, #f0fdf4 0%, #fff 100%);
    }

    .detail-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
        gap: 0.75rem;
        margin: 1rem 0;
    }

    .quick-links {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
        gap: 0.65rem;
        list-style: none;
        padding: 0;
        margin: 0;
    }

    .quick-links a {
        display: block;
        padding: 0.75rem 1rem;
        border: 1px solid var(--color-border);
        border-radius: var(--radius-sm);
        background: #f8fafc;
        color: var(--color-text);
        font-size: 0.9rem;
        font-weight: 500;
        transition: border-color 0.15s, background 0.15s;
    }

    .quick-links a:hover {
        border-color: #86efac;
        background: #f0fdf4;
        color: #065f46;
    }

    /* Flash messages */
    .flash {
        background: var(--color-success-soft);
        border: 1px solid #a7f3d0;
        color: #065f46;
        padding: 0.85rem 1rem;
        border-radius: var(--radius-sm);
        margin-bottom: 1rem;
        font-size: 0.9rem;
    }

    .flash.error {
        background: var(--color-danger-soft);
        border-color: #fecaca;
        color: #991b1b;
    }

    /* Tables */
    .table-wrap {
        overflow-x: auto;
        border: 1px solid var(--color-border);
        border-radius: var(--radius-md);
    }

    table {
        width: 100%;
        border-collapse: collapse;
        font-size: 0.88rem;
        background: var(--color-surface);
    }

    th, td {
        text-align: left;
        padding: 0.75rem 1rem;
        border-bottom: 1px solid var(--color-border);
        vertical-align: top;
    }

    tbody tr:last-child td { border-bottom: 0; }
    tbody tr:hover { background: #f8fafc; }

    th {
        font-size: 0.72rem;
        text-transform: uppercase;
        letter-spacing: 0.06em;
        color: var(--color-muted);
        font-weight: 600;
        background: #f8fafc;
    }

    /* Badges */
    .badge {
        display: inline-block;
        padding: 0.2rem 0.6rem;
        border-radius: 999px;
        font-size: 0.75rem;
        font-weight: 600;
        background: #f1f5f9;
        color: #475569;
    }

    .badge.delivered, .badge.active, .badge.approved, .badge.paid { background: #dcfce7; color: #166534; }
    .badge.pending, .badge.processing { background: #fef3c7; color: #92400e; }
    .badge.cancelled, .badge.rejected, .badge.suspended { background: #fee2e2; color: #991b1b; }
    .badge.disabled { background: #e2e8f0; color: #475569; }

    /* Forms & toolbar */
    .toolbar {
        display: flex;
        gap: 0.75rem;
        flex-wrap: wrap;
        margin-bottom: 1.25rem;
        align-items: end;
        padding: 1rem;
        background: #f8fafc;
        border: 1px solid var(--color-border);
        border-radius: var(--radius-md);
    }

    .toolbar label {
        display: block;
        font-size: 0.75rem;
        font-weight: 600;
        color: var(--color-muted);
        margin-bottom: 0.35rem;
        text-transform: uppercase;
        letter-spacing: 0.04em;
    }

    input, select, textarea, button { font: inherit; }

    input, select, textarea {
        padding: 0.55rem 0.75rem;
        border: 1px solid var(--color-border);
        border-radius: var(--radius-sm);
        background: var(--color-surface);
        color: var(--color-text);
        transition: border-color 0.15s, box-shadow 0.15s;
    }

    input:focus, select:focus, textarea:focus {
        outline: none;
        border-color: #6ee7b7;
        box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.15);
    }

    textarea { width: 100%; resize: vertical; }

    button, .btn {
        padding: 0.55rem 1rem;
        border-radius: var(--radius-sm);
        border: 0;
        background: var(--color-sidebar);
        color: #fff;
        cursor: pointer;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        font-size: 0.88rem;
        font-weight: 600;
        transition: background 0.15s, transform 0.1s;
    }

    button:hover, .btn:hover {
        background: #1e293b;
        color: #fff;
    }

    .btn-primary {
        background: var(--color-accent);
    }

    .btn-primary:hover { background: #059669; }

    button.link, .btn.link {
        background: none;
        color: #047857;
        padding: 0;
        font-weight: 500;
    }

    button.link:hover, .btn.link:hover { color: #065f46; background: none; }

    .btn-danger, button.btn-danger { background: var(--color-danger); }
    .btn-danger:hover { background: #b91c1c; }

    button.link.danger, a.danger { color: var(--color-danger); }
    button.link.danger:hover { color: #b91c1c; background: none; }

    form.inline { display: inline; }

    /* Pagination */
    .pagination {
        margin-top: 1.25rem;
        display: flex;
        gap: 0.35rem;
        flex-wrap: wrap;
        align-items: center;
    }

    .pagination a, .pagination span {
        padding: 0.4rem 0.7rem;
        border-radius: var(--radius-sm);
        border: 1px solid var(--color-border);
        text-decoration: none;
        color: inherit;
        font-size: 0.85rem;
        background: var(--color-surface);
    }

    .pagination span.current {
        background: var(--color-sidebar);
        color: #fff;
        border-color: var(--color-sidebar);
    }

    /* Misc */
    .note-box {
        background: #f8fafc;
        border: 1px solid var(--color-border);
        border-radius: var(--radius-sm);
        padding: 0.85rem 1rem;
        margin: 1rem 0;
    }

    .documents-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
        gap: 1rem;
        margin: 1rem 0;
    }

    .document-card {
        border: 1px solid var(--color-border);
        border-radius: var(--radius-sm);
        padding: 0.85rem;
        background: #f8fafc;
    }

    .document-preview {
        max-width: 100%;
        max-height: 180px;
        border-radius: var(--radius-sm);
        display: block;
        margin: 0.5rem 0;
    }

    .status-form {
        display: grid;
        gap: 0.75rem;
        max-width: 520px;
        margin-top: 0.75rem;
    }

    code {
        font-size: 0.82rem;
        background: #f1f5f9;
        padding: 0.15rem 0.35rem;
        border-radius: 4px;
    }

    .inline-action {
        display: inline-flex;
        gap: 0.35rem;
        align-items: center;
        flex-wrap: wrap;
        margin: 0.25rem 0;
    }

    .product-thumb {
        width: 40px;
        height: 40px;
        object-fit: cover;
        border-radius: var(--radius-sm);
        vertical-align: middle;
        margin-right: 0.5rem;
    }

    .product-preview {
        max-width: 200px;
        border-radius: var(--radius-sm);
        display: block;
        margin: 0.5rem 0;
    }

    .field-error { color: var(--color-danger); display: block; margin-top: 0.25rem; font-size: 0.85rem; }

    .wide-form { max-width: 760px; }

    .toggle-row { display: flex; flex-direction: column; gap: 0.5rem; }
    .toggle-row label { display: flex; align-items: center; gap: 0.5rem; font-weight: 500; }

    .back-link {
        display: inline-flex;
        align-items: center;
        gap: 0.35rem;
        margin-bottom: 1rem;
        font-size: 0.88rem;
        font-weight: 500;
        color: var(--color-muted);
    }

    .back-link:hover { color: #047857; }

    @media (max-width: 960px) {
        .admin-sidebar {
            transform: translateX(-100%);
        }

        .admin-main {
            margin-left: 0;
        }

        .admin-content {
            padding: 1rem;
        }
    }
</style>
