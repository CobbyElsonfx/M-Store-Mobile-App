package com.momokotle.agent.ussd

import com.momokotle.agent.models.AutomationStatus
import com.momokotle.agent.models.UssdActionType
import com.momokotle.agent.utils.AutomationLogger
import com.momokotle.agent.utils.UssdSecurityHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicLong

class UssdAutomationEngine(
    private val logger: AutomationLogger,
    private val clock: () -> Long = { System.currentTimeMillis() }
) {
    private val _status = MutableStateFlow(AutomationStatus.IDLE)
    val status: StateFlow<AutomationStatus> = _status.asStateFlow()

    private val _currentStepIndex = MutableStateFlow(-1)
    val currentStepIndex: StateFlow<Int> = _currentStepIndex.asStateFlow()

    private val _lastError = MutableStateFlow<String?>(null)
    val lastError: StateFlow<String?> = _lastError.asStateFlow()

    private var activeFlow: UssdFlow? = null
    private var flowContext: UssdFlowContext = UssdFlowContext()
    private var stepAttempts = 0
    private var startedAt = 0L
    private var stepStartedAt = 0L
    private var waitingForPinSince = 0L
    private var submittedFingerprint: String? = null
    private var awaitingTransition = false
    private var windowGeneration = 0
    private var submittedWindowGeneration = 0
    private var sawDialogGap = false
    private var hasSeenDialog = false
    private var dialogAbsentSince = 0L
    private var submittedAt = 0L
    private var lastSubmittedValue: String? = null

    private val sessionSeq = AtomicLong(0)

    /** Unique id for the current (or last) automation session; used to ignore stale a11y events. */
    var currentSessionId: Long = 0L
        private set

    var activeTransactionId: Long? = null
        private set
    /** Type of the active (or last started) flow — used for Cash Out SMS confirmation. */
    var activeTransactionType: com.momokotle.agent.models.TransactionType? = null
        private set
    var verificationEnabled: Boolean = false
        private set
    var stepDelayMs: Long = DEFAULT_STEP_DELAY_MS
        private set
    var debounceMs: Long = DEFAULT_DEBOUNCE_MS
        private set
    var submitSettleMs: Long = DEFAULT_SUBMIT_SETTLE_MS
        private set

    val isRunning: Boolean
        get() = _status.value.isActive

    fun isCurrentSession(sessionId: Long): Boolean =
        _status.value.isActive && currentSessionId == sessionId

    fun beginStart(
        flow: UssdFlow,
        context: UssdFlowContext,
        transactionId: Long? = null,
        verificationEnabled: Boolean = false,
        stepDelayMs: Long = DEFAULT_STEP_DELAY_MS,
        debounceMs: Long = DEFAULT_DEBOUNCE_MS,
        submitSettleMs: Long = DEFAULT_SUBMIT_SETTLE_MS
    ): Long {
        currentSessionId = sessionSeq.incrementAndGet()
        activeFlow = flow
        flowContext = context
        activeTransactionId = transactionId
        activeTransactionType = flow.transactionType
        this.verificationEnabled = verificationEnabled
        this.stepDelayMs = stepDelayMs.coerceAtLeast(MIN_STEP_DELAY_MS)
        this.debounceMs = debounceMs.coerceAtLeast(MIN_DEBOUNCE_MS)
        this.submitSettleMs = submitSettleMs.coerceAtLeast(MIN_SUBMIT_SETTLE_MS)
        stepAttempts = 0
        startedAt = clock()
        stepStartedAt = startedAt
        submittedFingerprint = null
        awaitingTransition = false
        windowGeneration = 0
        submittedWindowGeneration = 0
        sawDialogGap = false
        hasSeenDialog = false
        dialogAbsentSince = 0L
        waitingForPinSince = 0L
        submittedAt = 0L
        lastSubmittedValue = null
        _currentStepIndex.value = 0
        logger.log(
            "Flow steps: ${flow.steps.mapIndexed { index, step -> "$index:${stepLabel(step)}" }}"
        )
        logger.log(
            "Timing debounce=${this.debounceMs}ms settle=${this.submitSettleMs}ms"
        )
        _lastError.value = null
        _status.value = AutomationStatus.STARTING
        logger.log("Preparing flow: ${flow.name} (${flow.ussdCode}) session=$currentSessionId")
        return currentSessionId
    }

    fun markDialing() {
        if (_status.value == AutomationStatus.STARTING) {
            _status.value = AutomationStatus.DIALING
            logger.log("Dialing ${activeFlow?.ussdCode}")
        }
    }

    fun markRunning() {
        if (_status.value == AutomationStatus.DIALING || _status.value == AutomationStatus.STARTING) {
            _status.value = AutomationStatus.RUNNING
            stepStartedAt = clock()
            logger.log("Automation running — step 0")
        }
    }

    fun cancel() {
        if (_status.value == AutomationStatus.IDLE) return
        logger.log("Flow cancelled")
        reset()
        _status.value = AutomationStatus.CANCELLED
    }

    fun markSuccess(message: String = "Transaction steps completed") {
        logger.log(message)
        reset()
        _status.value = AutomationStatus.SUCCESS
    }

    fun markFailed(message: String) {
        logger.log("Flow failed: $message")
        _lastError.value = message
        reset()
        _status.value = AutomationStatus.FAILED
    }

    fun markTimeout() {
        logger.log("Automation timed out")
        _lastError.value = "Automation timed out"
        reset()
        _status.value = AutomationStatus.TIMEOUT
    }

    /**
     * Releases the automation lock because the USSD session ended.
     * Does not imply financial SUCCESSFUL — callers update DB separately.
     */
    fun markSessionEnded(message: String = "USSD session ended") {
        if (!_status.value.isActive) return
        logger.log(message)
        reset()
        _status.value = AutomationStatus.SESSION_ENDED
    }

    fun acknowledgeIdle() {
        if (_status.value.isTerminal || _status.value == AutomationStatus.CANCELLED) {
            reset()
            _status.value = AutomationStatus.IDLE
        }
    }

    fun currentStep(): UssdStep? {
        val flow = activeFlow ?: return null
        return flow.steps.getOrNull(_currentStepIndex.value)
    }

    fun resolveInputValue(step: UssdStep): String? =
        flowContext.resolve(step.inputValue)

    fun evaluate(snapshot: UssdDialogSnapshot): AutomationDecision {
        val now = clock()
        val status = _status.value
        if (!status.isActive) return AutomationDecision.Ignore

        if (now - startedAt > FLOW_TIMEOUT_MS) {
            return AutomationDecision.FinishFailure("Automation timed out")
        }

        noteDialogPresence(snapshot, now)

        if (status == AutomationStatus.WAITING_FOR_PIN) {
            return evaluatePin(snapshot, now)
        }

        if (UssdScreenValidator.containsFatalError(snapshot.text)) {
            return AutomationDecision.FinishFailure("Unexpected USSD response")
        }

        if (UssdSecurityHelper.containsSensitiveKeyword(snapshot.text)) {
            handlePinStep()
            return AutomationDecision.WaitForPin
        }

        if (!snapshot.looksLikeDialog) {
            if (awaitingTransition) {
                sawDialogGap = true
            }
            sessionEndedIfDialogGone(now)?.let { return it }
            val pending = currentStep()
            return if (hasSeenDialog) {
                AutomationDecision.Wait
            } else if (pending != null) {
                timeoutOrWait(pending, now, "No USSD dialog")
            } else {
                AutomationDecision.Wait
            }
        }

        if (awaitingTransition) {
            if (!transitionConfirmed(snapshot, now)) {
                val pending = currentStep()
                return if (pending != null) {
                    timeoutOrWait(pending, now, "USSD did not advance")
                } else {
                    AutomationDecision.Wait
                }
            }
            completeTransition(now)
        }

        val step = currentStep()
        if (step == null) {
            return if (activeFlow?.stopBeforePin == true) {
                handlePinStep()
                AutomationDecision.WaitForPin
            } else {
                AutomationDecision.FinishSuccess("Transaction steps completed")
            }
        }

        if (step.actionType == UssdActionType.STOP_FOR_USER) {
            handlePinStep()
            return AutomationDecision.WaitForPin
        }

        if (snapshot.fingerprint == submittedFingerprint) {
            return AutomationDecision.Wait
        }

        if (step.actionType == UssdActionType.WAIT) {
            return AutomationDecision.Act(step, "")
        }

        val value = resolveInputValue(step).orEmpty()
        if (UssdScreenValidator.requiresResolvedValue(step) && value.isBlank()) {
            return AutomationDecision.FinishFailure("Missing phone or amount for this USSD step")
        }
        if (step.actionType == UssdActionType.INPUT && value.isBlank()) {
            return AutomationDecision.FinishFailure("Missing USSD input for ${stepLabel(step)}")
        }

        return AutomationDecision.Act(step, value)
    }

    fun noteNewWindow() {
        windowGeneration += 1
    }

    /**
     * Records that the current step was submitted. The step index advances only after the
     * next USSD prompt is confirmed — prevents Fast mode from typing the next value into
     * the previous screen when typed digits briefly change the accessibility dump.
     */
    fun onSubmitted(fingerprint: String, submittedValue: String? = null) {
        if (awaitingTransition) {
            // Caller already moved on (e.g. tests / menu click) — commit the pending advance.
            advanceStep()
        }
        submittedFingerprint = fingerprint
        submittedWindowGeneration = windowGeneration
        awaitingTransition = true
        sawDialogGap = false
        stepAttempts = 0
        stepStartedAt = clock()
        submittedAt = stepStartedAt
        lastSubmittedValue = submittedValue
    }

    fun onActionFailed() {
        val step = currentStep() ?: run {
            markFailed("USSD input failed")
            return
        }
        stepAttempts++
        val timedOut = clock() - stepStartedAt > step.timeoutMs
        if (stepAttempts > step.retryCount || timedOut) {
            markFailed("Could not complete: ${stepLabel(step)}")
        }
    }

    fun applyDecision(decision: AutomationDecision) {
        when (decision) {
            is AutomationDecision.FinishSuccess -> markSuccess(decision.message)
            is AutomationDecision.FinishFailure -> {
                if (decision.message.contains("timed out", ignoreCase = true)) {
                    markTimeout()
                } else {
                    markFailed(decision.message)
                }
            }
            AutomationDecision.SessionEnded -> markSessionEnded()
            AutomationDecision.WaitForPin -> Unit
            else -> Unit
        }
    }

    fun advanceStep() {
        val flow = activeFlow ?: return
        val next = _currentStepIndex.value + 1
        stepAttempts = 0
        stepStartedAt = clock()
        if (next >= flow.steps.size) {
            if (flow.stopBeforePin) {
                handlePinStep()
            } else {
                markSuccess()
            }
        } else {
            _currentStepIndex.value = next
            logger.log("Step $next/${flow.steps.lastIndex}: ${stepLabel(flow.steps[next])}")
        }
    }

    fun handlePinStep() {
        if (_status.value == AutomationStatus.WAITING_FOR_PIN) return
        logger.log("Security screen — waiting for PIN (not entered by app)")
        waitingForPinSince = clock()
        awaitingTransition = false
        lastSubmittedValue = null
        _status.value = AutomationStatus.WAITING_FOR_PIN
    }

    fun consumeTransactionId(): Long? {
        val id = activeTransactionId
        activeTransactionId = null
        return id
    }

    fun activeFlowName(): String? = activeFlow?.name

    private fun completeTransition(now: Long) {
        awaitingTransition = false
        submittedFingerprint = null
        sawDialogGap = false
        lastSubmittedValue = null
        advanceStep()
        stepAttempts = 0
        stepStartedAt = now
        logger.log("USSD advanced to step ${_currentStepIndex.value}")
    }

    private fun transitionConfirmed(snapshot: UssdDialogSnapshot, now: Long): Boolean {
        val newWindow = windowGeneration > submittedWindowGeneration
        // Window change / dialog gap is a strong signal — accept immediately.
        if (newWindow || sawDialogGap) return true

        // Fingerprint-only advances need a short settle so typed digits don't look like a new prompt.
        val minWait = maxOf(submitSettleMs, MIN_TRANSITION_WAIT_MS)
        if (submittedAt > 0L && now - submittedAt < minWait) {
            return false
        }

        val current = fingerprintIgnoringSubmittedValue(snapshot.fingerprint)
        val previous = submittedFingerprint ?: return false
        return current != previous
    }

    private fun fingerprintIgnoringSubmittedValue(fingerprint: String): String {
        val value = lastSubmittedValue?.trim()?.lowercase().orEmpty()
        if (value.isEmpty()) return fingerprint
        return fingerprint.lineSequence()
            .filter { it.trim() != value }
            .joinToString("\n")
    }

    private fun evaluatePin(snapshot: UssdDialogSnapshot, now: Long): AutomationDecision {
        if (UssdScreenValidator.containsFatalError(snapshot.text)) {
            return AutomationDecision.FinishFailure("Transaction failed on USSD screen")
        }
        val lower = snapshot.text.lowercase()
        // Definitive financial signals take priority over dialog-absence release.
        if (SUCCESS_KEYWORDS.any { lower.contains(it) }) {
            return AutomationDecision.FinishSuccess("Transaction completed successfully")
        }
        if (FAILURE_KEYWORDS.any { lower.contains(it) }) {
            return AutomationDecision.FinishFailure("Transaction failed on USSD screen")
        }
        sessionEndedIfDialogGone(now)?.let { return it }
        // Never hang on "Waiting for PIN" — release so the agent can start another run.
        if (waitingForPinSince > 0L && now - waitingForPinSince >= PIN_WAIT_MS) {
            return AutomationDecision.SessionEnded
        }
        return AutomationDecision.WaitForPin
    }

    private fun noteDialogPresence(snapshot: UssdDialogSnapshot, now: Long) {
        if (snapshot.looksLikeDialog) {
            hasSeenDialog = true
            dialogAbsentSince = 0L
        } else if (hasSeenDialog) {
            // Result screens often have no input field; keep last text available via evaluate,
            // but start the absence clock so dismissal can release the engine.
            if (dialogAbsentSince == 0L) dialogAbsentSince = now
        }
    }

    private fun sessionEndedIfDialogGone(now: Long): AutomationDecision.SessionEnded? {
        if (!hasSeenDialog) return null
        if (dialogAbsentSince == 0L) return null
        if (now - dialogAbsentSince < SESSION_ABSENCE_MS) return null
        return AutomationDecision.SessionEnded
    }

    private fun timeoutOrWait(step: UssdStep, now: Long, message: String): AutomationDecision {
        if (now - stepStartedAt > step.timeoutMs) {
            return AutomationDecision.FinishFailure(message)
        }
        return AutomationDecision.Wait
    }

    private fun stepLabel(step: UssdStep): String = when (step.actionType) {
        UssdActionType.INPUT -> when (step.inputValue) {
            "{phone}", "{phoneNumber}" -> "Input phone number"
            "{phone_confirm}" -> "Confirm phone number"
            "{amount}" -> "Input amount"
            else -> "Input ${step.inputValue.orEmpty()}"
        }
        UssdActionType.STOP_FOR_USER -> "Stop for user"
        UssdActionType.CLICK -> "Click ${step.expectedText.orEmpty()}"
        UssdActionType.WAIT -> "Wait"
    }

    private fun reset() {
        activeFlow = null
        flowContext = UssdFlowContext()
        activeTransactionId = null
        activeTransactionType = null
        verificationEnabled = false
        _currentStepIndex.value = -1
        stepAttempts = 0
        stepStartedAt = 0L
        waitingForPinSince = 0L
        submittedFingerprint = null
        awaitingTransition = false
        windowGeneration = 0
        submittedWindowGeneration = 0
        sawDialogGap = false
        hasSeenDialog = false
        dialogAbsentSince = 0L
        submittedAt = 0L
        lastSubmittedValue = null
    }

    companion object {
        const val DEFAULT_STEP_DELAY_MS = 250L
        const val MIN_STEP_DELAY_MS = 35L
        const val DEFAULT_DEBOUNCE_MS = 150L
        const val MIN_DEBOUNCE_MS = 25L
        const val DEFAULT_SUBMIT_SETTLE_MS = 250L
        const val MIN_SUBMIT_SETTLE_MS = 40L
        /** Floor for fingerprint-only advances; window/gap changes skip this wait. */
        const val MIN_TRANSITION_WAIT_MS = 40L
        const val FLOW_TIMEOUT_MS = 90_000L
        /** Max time on PIN screen without a result before releasing the automation lock. */
        const val PIN_WAIT_MS = 30_000L
        /** Sustained absence after a live dialog before treating the USSD session as ended. */
        const val SESSION_ABSENCE_MS = 1_500L
        private val SUCCESS_KEYWORDS = listOf(
            "successful",
            "completed",
            "sent successfully",
            "cash in made",
            "receive cash in"
            // Cash Out success is SMS-only (CashOutSmsConfirmation) — not USSD dialog keywords.
        )
        private val FAILURE_KEYWORDS = listOf(
            "failed",
            "insufficient balance",
            "declined",
            "cancelled",
            "transaction failed"
        )
    }
}
