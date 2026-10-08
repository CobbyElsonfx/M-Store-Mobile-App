<?php

namespace App\Mail;

use Illuminate\Bus\Queueable;
use Illuminate\Mail\Mailable;
use Illuminate\Mail\Mailables\Content;
use Illuminate\Mail\Mailables\Envelope;
use Illuminate\Queue\SerializesModels;

class MoMoKotleMail extends Mailable
{
    use Queueable, SerializesModels;

    /**
     * @param  array<string, string|null>  $rows
     */
    public function __construct(
        public readonly string $subjectLine,
        public readonly string $title,
        public readonly string $greeting,
        public readonly string $intro,
        public readonly array $rows = [],
        public readonly ?string $nextSteps = null,
        public readonly ?string $note = null,
        public readonly ?string $supportContact = null,
    ) {}

    public function envelope(): Envelope
    {
        return new Envelope(subject: $this->subjectLine);
    }

    public function content(): Content
    {
        return new Content(
            html: 'mail.transactional',
            text: 'mail.transactional-text',
            with: [
                'title' => $this->title,
                'greeting' => $this->greeting,
                'intro' => $this->intro,
                'rows' => array_filter($this->rows, fn ($value) => $value !== null && $value !== ''),
                'nextSteps' => $this->nextSteps,
                'note' => $this->note,
                'supportContact' => $this->supportContact,
            ],
        );
    }
}
