package com.momokotle.agent.ussd

import com.momokotle.agent.models.AutomationStatus
import com.momokotle.agent.models.UssdActionType
import com.momokotle.agent.utils.AutomationLogger
import com.momokotle.agent.utils.UssdSecurityHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UssdAutomationEngine(
    private val logger: AutomationLogger
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

    var activeTransactionId: Long? = null
        private set
    var verificationEnabled: Boolean = false
        private set

    val isRunning: Boolean
        get() = _status.value == AutomationStatus.RUNNING

    fun start(
        flow: UssdFlow,
        context: UssdFlowContext,
        transactionId: Long? = null,
        verificationEnabled: Boolean = false
    ) {
        activeFlow = flow
        flowContext = context
        activeTransactionId = transactionId
        this.verificationEnabled = verificationEnabled
        stepAttempts = 0
        startedAt = System.currentTimeMillis()
        _currentStepIndex.value = 0
        _lastError.value = null
        _status.value = AutomationStatus.RUNNING
        logger.log("Started flow: ${flow.name} (${flow.ussdCode})")
    }

    fun cancel() {
        if (_status.value == AutomationStatus.IDLE) return
        logger.log("Flow cancelled")
        reset()
        _status.value = AutomationStatus.CANCELLED
    }

    fun markSuccess() {
        logger.log("Flow completed — stopped before PIN/confirmation")
        reset()
        _status.value = AutomationStatus.SUCCESS
    }

    fun markFailed(message: String) {
        logger.log("Flow failed: $message")
        _lastError.value = message
        reset()
        _status.value = AutomationStatus.FAILED
    }

    fun currentStep(): UssdStep? {
        val flow = activeFlow ?: return null
        val index = _currentStepIndex.value
        return flow.steps.getOrNull(index)
    }

    fun resolveInputValue(step: UssdStep): String? =
        flowContext.resolve(step.inputValue)

    fun advanceStep() {
        val flow = activeFlow ?: return
        val next = _currentStepIndex.value + 1
        stepAttempts = 0
        if (next >= flow.steps.size) {
            markSuccess()
        } else {
            _currentStepIndex.value = next
            val nextStep = flow.steps[next]
            logger.log("Step $next/${flow.steps.lastIndex}: ${stepLabel(nextStep)}")
        }
    }

    fun recordStepAttempt(): Boolean {
        val step = currentStep() ?: return false
        stepAttempts++
        if (stepAttempts > step.retryCount) {
            markFailed("Step timed out: ${stepLabel(step)}")
            return false
        }
        return true
    }

    fun handlePinStep() {
        logger.log("Security screen detected — automation paused (PIN not logged)")
        _status.value = AutomationStatus.WAITING_FOR_USER
    }

    fun resumeAfterUserInput() {
        if (_status.value == AutomationStatus.WAITING_FOR_USER) {
            _status.value = AutomationStatus.RUNNING
            advanceStep()
        }
    }

    fun matchesExpected(screenText: String, step: UssdStep): Boolean {
        val expected = step.expectedText ?: return true
        return if (step.caseInsensitive) {
            val haystack = screenText.lowercase()
            val needle = expected.lowercase()
            if (step.partialMatch) haystack.contains(needle) else haystack == needle
        } else {
            if (step.partialMatch) screenText.contains(expected) else screenText == expected
        }
    }

    fun shouldStopForPin(text: String): Boolean =
        UssdSecurityHelper.containsSensitiveKeyword(text)

    fun isTimedOut(maxDurationMs: Long = 120_000L): Boolean =
        System.currentTimeMillis() - startedAt > maxDurationMs

    fun consumeTransactionId(): Long? {
        val id = activeTransactionId
        activeTransactionId = null
        return id
    }

    private fun stepLabel(step: UssdStep): String = when (step.actionType) {
        UssdActionType.INPUT -> when (step.inputValue) {
            "{phone}", "{phoneNumber}" -> "Input phone number"
            "{amount}" -> "Input amount"
            else -> "Input menu option ${step.inputValue.orEmpty()}"
        }
        UssdActionType.STOP_FOR_USER -> "Stop for user"
        UssdActionType.CLICK -> "Click ${step.expectedText.orEmpty()}"
        UssdActionType.WAIT -> "Wait"
    }

    private fun reset() {
        activeFlow = null
        flowContext = UssdFlowContext()
        activeTransactionId = null
        verificationEnabled = false
        _currentStepIndex.value = -1
        stepAttempts = 0
    }
}
