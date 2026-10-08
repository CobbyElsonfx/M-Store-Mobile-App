<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\EmailNotificationLog;
use Illuminate\Http\Request;
use Illuminate\View\View;

class EmailNotificationLogController extends Controller
{
    public function index(Request $request): View
    {
        $status = $request->string('status')->toString();
        $type = $request->string('type')->toString();
        $search = trim($request->string('search')->toString());

        $logs = EmailNotificationLog::query()
            ->with('user:id,name,email,first_name,last_name')
            ->when($status !== '' && $status !== 'all', fn ($query) => $query->where('status', $status))
            ->when($type !== '' && $type !== 'all', fn ($query) => $query->where('notification_type', $type))
            ->when($search !== '', function ($query) use ($search) {
                $query->where(function ($inner) use ($search) {
                    $inner->where('recipient_email', 'like', "%{$search}%")
                        ->orWhere('subject', 'like', "%{$search}%")
                        ->orWhere('event_key', 'like', "%{$search}%")
                        ->orWhere('reference_id', 'like', "%{$search}%");
                });
            })
            ->orderByDesc('created_at')
            ->paginate(50)
            ->withQueryString();

        $types = EmailNotificationLog::query()
            ->select('notification_type')
            ->distinct()
            ->orderBy('notification_type')
            ->pluck('notification_type');

        return view('admin.email-notifications.index', [
            'title' => 'Email Log',
            'logs' => $logs,
            'types' => $types,
            'statuses' => ['queued', 'sent', 'failed'],
            'statusFilter' => $status !== '' ? $status : 'all',
            'typeFilter' => $type !== '' ? $type : 'all',
            'search' => $search,
        ]);
    }
}
