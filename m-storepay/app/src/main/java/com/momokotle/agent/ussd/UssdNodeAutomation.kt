package com.momokotle.agent.ussd

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.accessibility.AccessibilityNodeInfo
import com.momokotle.agent.utils.AutomationLogger

object UssdNodeAutomation {

    private val submitLabels = listOf("Send", "SEND", "OK", "Okay", "Done", "Submit", "Reply")
    private val cancelLabels = listOf("cancel", "close", "no", "back", "dismiss")

    fun enterValue(
        context: Context,
        root: AccessibilityNodeInfo,
        value: String,
        logger: AutomationLogger
    ): Boolean {
        val field = findInputField(root)
        if (field == null) {
            logger.log("No USSD input field found")
            return false
        }

        field.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
        field.performAction(AccessibilityNodeInfo.ACTION_CLICK)

        val clearArgs = Bundle().apply {
            putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "")
        }
        field.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, clearArgs)

        val args = Bundle().apply {
            putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, value)
        }
        var entered = field.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)
        if (!entered) {
            entered = pasteIntoField(context, field, value)
        }

        // Confirm the dialer actually accepted the value (Fast mode used to OK empty fields).
        if (entered && !fieldShowsValue(field, value)) {
            field.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, clearArgs)
            entered = field.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)
            if (!entered) {
                entered = pasteIntoField(context, field, value)
            }
            if (!fieldShowsValue(field, value)) {
                logger.log("USSD input not confirmed in field after retry (value=$value)")
            }
        }

        field.recycleSafely()
        if (!entered) {
            logger.log("Could not set USSD input text")
        }
        return entered
    }

    /** True when the input field visibly holds [value] (digits compared loosely). */
    fun fieldShowsValue(field: AccessibilityNodeInfo, value: String): Boolean {
        val shown = field.text?.toString().orEmpty()
        if (shown.isEmpty()) return false
        if (shown == value) return true
        if (shown.contains(value)) return true
        val shownDigits = shown.filter { it.isDigit() }
        val valueDigits = value.filter { it.isDigit() }
        return valueDigits.isNotEmpty() && shownDigits.endsWith(valueDigits)
    }

    fun clickSubmit(root: AccessibilityNodeInfo, logger: AutomationLogger): Boolean {
        findNodeByViewId(root, "android:id/button1")?.let { button ->
            val clicked = clickNodeOrAncestor(button)
            button.recycleSafely()
            if (clicked) return true
        }

        for (label in submitLabels) {
            val node = findNodeByText(root, label, clickableOnly = false) ?: continue
            if (isCancelNode(node)) {
                node.recycleSafely()
                continue
            }
            val clicked = clickNodeOrAncestor(node)
            node.recycleSafely()
            if (clicked) return true
        }

        val fallback = clickFirstSubmitButton(root)
        if (!fallback) {
            logger.log("USSD submit button was not found")
        }
        return fallback
    }

    fun clickMenuOption(
        root: AccessibilityNodeInfo,
        option: String,
        logger: AutomationLogger
    ): Boolean {
        val targetLine = findMenuNode(root, option) ?: return false
        val clicked = clickNodeOrAncestor(targetLine)
        if (clicked) {
            logger.log("Selected menu option $option via menu line click")
        }
        targetLine.recycleSafely()
        return clicked
    }

    fun hasInputField(root: AccessibilityNodeInfo): Boolean {
        val field = findInputField(root) ?: return false
        if (field !== root) field.recycleSafely()
        return true
    }

    fun findInputField(root: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        findFocusedInput(root)?.let { return it }

        traverse(root) { node ->
            if (supportsSetText(node)) node else null
        }?.let { return it }

        traverse(root) { node ->
            if (isEditTextLike(node)) node else null
        }?.let { return it }

        return traverse(root) { node ->
            if (node.isEditable) node else null
        }
    }

    private fun findFocusedInput(root: AccessibilityNodeInfo): AccessibilityNodeInfo? =
        traverse(root) { node ->
            if (node.isFocused && (node.isEditable || isEditTextLike(node) || supportsSetText(node))) {
                node
            } else {
                null
            }
        }

    private fun pasteIntoField(
        context: Context,
        field: AccessibilityNodeInfo,
        value: String
    ): Boolean {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            ?: return false
        clipboard.setPrimaryClip(ClipData.newPlainText("ussd_input", value))
        field.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
        return field.performAction(AccessibilityNodeInfo.ACTION_PASTE)
    }

    private fun findMenuNode(root: AccessibilityNodeInfo, option: String): AccessibilityNodeInfo? {
        traverse(root) { node ->
            val text = node.nodeText()
            if (text.isNotBlank() && UssdMenuLineMatcher.lineMatchesOption(text, option)) {
                return@traverse node
            }
            null
        }?.let { return it }

        return traverse(root) { node ->
            val text = node.nodeText()
            if (text == option && node.isClickable) node else null
        }
    }

    private fun clickFirstSubmitButton(root: AccessibilityNodeInfo): Boolean {
        val button = traverse(root) { node ->
            if (!node.isClickable) return@traverse null
            if (isEditTextLike(node) || node.isEditable) return@traverse null
            if (isCancelNode(node)) return@traverse null
            val className = node.className?.toString().orEmpty()
            if (className.contains("Button", ignoreCase = true)) node else null
        } ?: return false
        return button.performAction(AccessibilityNodeInfo.ACTION_CLICK).also {
            button.recycleSafely()
        }
    }

    private fun clickNodeOrAncestor(node: AccessibilityNodeInfo): Boolean {
        var current: AccessibilityNodeInfo? = node
        var depth = 0
        while (current != null && depth < 4) {
            if (!isCancelNode(current) &&
                current.isClickable &&
                current.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            ) {
                if (current !== node) current.recycleSafely()
                return true
            }
            val parent = current.parent
            if (current !== node) current.recycleSafely()
            current = parent
            depth++
        }
        return false
    }

    private fun findNodeByViewId(root: AccessibilityNodeInfo, viewId: String): AccessibilityNodeInfo? =
        root.findAccessibilityNodeInfosByViewId(viewId)?.firstOrNull()

    private fun findNodeByText(
        root: AccessibilityNodeInfo,
        text: String,
        clickableOnly: Boolean
    ): AccessibilityNodeInfo? {
        root.findAccessibilityNodeInfosByText(text)?.firstOrNull { node ->
            !isCancelNode(node) && (!clickableOnly || node.isClickable)
        }?.let { return it }

        return traverse(root) { node ->
            val matches = node.nodeText().contains(text, ignoreCase = true)
            if (matches && !isCancelNode(node) && (!clickableOnly || node.isClickable)) {
                node
            } else {
                null
            }
        }
    }

    private fun isCancelNode(node: AccessibilityNodeInfo): Boolean {
        val text = node.nodeText().lowercase()
        return cancelLabels.any { text.equals(it, ignoreCase = true) || text == it }
    }

    private fun supportsSetText(node: AccessibilityNodeInfo): Boolean =
        node.actionList?.any { it.id == AccessibilityNodeInfo.ACTION_SET_TEXT } == true

    private fun isEditTextLike(node: AccessibilityNodeInfo): Boolean {
        val className = node.className?.toString().orEmpty()
        return className.contains("EditText", ignoreCase = true) ||
            className.contains("AutoCompleteTextView", ignoreCase = true)
    }

    private fun AccessibilityNodeInfo.nodeText(): String {
        val textPart = text?.toString().orEmpty()
        val descPart = contentDescription?.toString().orEmpty()
        return listOf(textPart, descPart).filter { it.isNotBlank() }.joinToString(" ")
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

    private fun AccessibilityNodeInfo?.recycleSafely() {
        try {
            this?.recycle()
        } catch (_: Exception) {
        }
    }
}
