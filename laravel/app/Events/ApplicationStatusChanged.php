<?php

namespace App\Events;

use App\Models\AgentApplication;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class ApplicationStatusChanged
{
    use Dispatchable, SerializesModels;

    public function __construct(
        public readonly AgentApplication $application,
        public readonly string $previousStatus,
        public readonly string $newStatus,
        public readonly ?string $adminNote = null,
    ) {}
}
