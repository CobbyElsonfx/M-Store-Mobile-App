package com.momokotle.agent.accessibility

import android.accessibilityservice.AccessibilityService
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.momokotle.agent.models.AutomationStatus
import com.momokotle.agent.models.UssdActionType
import com.momokotle.agent.domain.repository.AccessibilityRepository
import com.momokotle.agent.domain.usecase.UpdateTransactionStatusUseCase
import com.momokotle.agent.ussd.UssdAutomationEngine
import com.momokotle.agent.utils.AutomationLogger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MomoAccessibilityService : AccessibilityService() {

    @Inject lateinit var engine: UssdAutomationEngine
    @Inject lateinit var logger: AutomationLogger
    @Inject lateinit var updateTransactionStatusUseCase: UpdateTransactionStatusUseCase
    @Inject lateinit var accessibilityRepository: AccessibilityRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val handler = Handler(Looper.getMainLooper())
    private var lastProcessedText: String? = null
    private var debounceRunnable: Runnable? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        accessibilityRepository.refresh()
        logger.log("Accessibility service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        val status = engine.status.value
        if (status != AutomationStatus.RUNNING && status != AutomationStatus.WAITING_FOR_USER) return

        val type = event.eventType
        if (type != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
            type != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        ) {
            return
        }

        debounceRunnable?.let { handler.removeCallbacks(it) }
        debounceRunnable = Runnable { processCurrentWindow() }
        handler.postDelayed(debounceRunnable!!, DEBOUNCE_MS)
    }

    private fun processCurrentWindow() {
        if (engine.isTimedOut()) {
            engine.markFailed("Automation timed out")
            return
        }

        val root = rootInActiveWindow ?: return
        val screenText = collectText(root)
        if (screenText.isBlank()) {
            root.recycleSafely()
            return
        }

        if (engine.shouldStopForPin(screenText)) {
            lastProcessedText = null
            engine.handlePinStep()
            root.recycleSafely()
            return
        }

        tryVerifyTransaction(screenText)

        if (screenText == lastProcessedText) {
            root.recycleSafely()
            return
        }
        lastProcessedText = screenText

        val step = engine.currentStep() ?: run {
            root.recycleSafely()
            return
        }

        if (step.actionType == UssdActionType.STOP_FOR_USER) {
            engine.handlePinStep()
            root.recycleSafely()
            return
        }

        if (step.expectedText != null && !engine.matchesExpected(screenText, step)) {
            if (!engine.recordStepAttempt()) return
            root.recycleSafely()
            return
        }

        val success = when (step.actionType) {
            UssdActionType.CLICK -> performClick(root, step.expectedText)
            UssdActionType.INPUT -> performInput(root, engine.resolveInputValue(step))
            UssdActionType.WAIT -> true
            UssdActionType.STOP_FOR_USER -> {
                engine.handlePinStep()
                false
            }
        }

        root.recycleSafely()

        if (success) {
            handler.postDelayed({ engine.advanceStep() }, STEP_DELAY_MS)
        } else if (!engine.recordStepAttempt()) {
            return
        }
    }

    private fun tryVerifyTransaction(screenText: String) {
        val transactionId = engine.activeTransactionId ?: return
        if (!engine.verificationEnabled) return
        serviceScope.launch {
            updateTransactionStatusUseCase(transactionId, screenText)
        }
    }

    private fun performClick(root: AccessibilityNodeInfo, targetText: String?): Boolean {
        if (targetText.isNullOrBlank()) {
            return clickFirstButton(root)
        }
        val node = findNodeByText(root, targetText) ?: return false
        return node.performAction(AccessibilityNodeInfo.ACTION_CLICK).also {
            node.recycleSafely()
        }
    }

    private fun performInput(root: AccessibilityNodeInfo, value: String?): Boolean {
        if (value.isNullOrBlank()) return false
        val field = findEditableField(root) ?: return false
        val args = Bundle().apply {
            putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, value)
        }
        val result = field.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)
        if (result) {
            clickSendButton(root)
        }
        field.recycleSafely()
        return result
    }

    private fun clickSendButton(root: AccessibilityNodeInfo): Boolean {
        val sendLabels = listOf("Send", "OK", "Done", "Submit", "Reply")
        for (label in sendLabels) {
            val node = findNodeByText(root, label)
            if (node != null) {
                val clicked = node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                node.recycleSafely()
                if (clicked) return true
            }
        }
        return clickFirstButton(root)
    }

    private fun clickFirstButton(root: AccessibilityNodeInfo): Boolean {
        val button = findFirstClickable(root)
        return button?.performAction(AccessibilityNodeInfo.ACTION_CLICK) == true
    }

    private fun findNodeByText(
        root: AccessibilityNodeInfo,
        text: String,
        partial: Boolean = true
    ): AccessibilityNodeInfo? {
        val nodes = root.findAccessibilityNodeInfosByText(text)
        if (!nodes.isNullOrEmpty()) return nodes.first()

        return traverse(root) { node ->
            val nodeText = node.text?.toString().orEmpty()
            val contentDesc = node.contentDescription?.toString().orEmpty()
            val combined = "$nodeText $contentDesc"
            val matches = if (partial) {
                combined.contains(text, ignoreCase = true)
            } else {
                combined.equals(text, ignoreCase = true)
            }
            if (matches && node.isClickable) node else null
        }
    }

    private fun findEditableField(root: AccessibilityNodeInfo): AccessibilityNodeInfo? =
        traverse(root) { node ->
            if (node.isEditable || node.className?.contains("EditText") == true) node else null
        }

    private fun findFirstClickable(root: AccessibilityNodeInfo): AccessibilityNodeInfo? =
        traverse(root) { node ->
            val className = node.className?.toString().orEmpty()
            if (node.isClickable && !className.contains("EditText")) node else null
        }

    private fun traverse(
        root: AccessibilityNodeInfo,
        matcher: (AccessibilityNodeInfo) -> AccessibilityNodeInfo?
    ): AccessibilityNodeInfo? {
        matcher(root)?.let { return it }
        for (i in 0 until root.childCount) {
            val child = root.getChild(i) ?: continue
            val found = traverse(child, matcher)
            if (found != null) return found
            child.recycleSafely()
        }
        return null
    }

    private fun collectText(root: AccessibilityNodeInfo): String {
        val builder = StringBuilder()
        appendTextRecursive(root, builder)
        return builder.toString().trim()
    }

    private fun appendTextRecursive(node: AccessibilityNodeInfo, builder: StringBuilder) {
        node.text?.let { if (it.isNotBlank()) builder.append(it).append('\n') }
        node.contentDescription?.let { if (it.isNotBlank()) builder.append(it).append('\n') }
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            appendTextRecursive(child, builder)
            child.recycleSafely()
        }
    }

    private fun AccessibilityNodeInfo?.recycleSafely() {
        try {
            this?.recycle()
        } catch (_: Exception) {
        }
    }

    override fun onInterrupt() {
        logger.log("Accessibility service interrupted")
        accessibilityRepository.refresh()
        engine.cancel()
    }

    override fun onDestroy() {
        debounceRunnable?.let { handler.removeCallbacks(it) }
        super.onDestroy()
    }

    companion object {
        private const val DEBOUNCE_MS = 300L
        private const val STEP_DELAY_MS = 600L
    }
}
