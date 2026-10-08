<?php

namespace App\Events;

use App\Models\AgentApplication;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class ApplicationSubmitted
{
    use Dispatchable, SerializesModels;

    public function __construct(public readonly AgentApplication $application) {}
}
