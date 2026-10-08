package com.momokotle.agent.ussd

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.accessibility.AccessibilityNodeInfo
import com.momokotle.agent.utils.AutomationLogger

object UssdNodeAutomation {

    private val submitLabels = listOf("Send", "OK", "Done", "Submit", "Reply", "Yes", "Confirm")

    fun performStepInput(
        context: Context,
        root: AccessibilityNodeInfo,
        step: UssdStep,
        value: String?,
        logger: AutomationLogger
    ): Boolean {
        if (value.isNullOrBlank()) {
            logger.log("Step input skipped — empty value for ${step.inputValue.orEmpty()}")
            return false
        }

        if (UssdScreenValidator.isMenuOptionStep(step)) {
            if (tryTypeIntoField(context, root, value, logger)) return true
            if (tryClickMenuOption(root, value, logger)) return true
            logger.log("Menu option $value — no input field or clickable menu line found")
            return false
        }

        if (tryTypeIntoField(context, root, value, logger)) return true
        logger.log("Input step failed — could not enter value for ${step.inputValue.orEmpty()}")
        return false
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

    private fun tryTypeIntoField(
        context: Context,
        root: AccessibilityNodeInfo,
        value: String,
        logger: AutomationLogger
    ): Boolean {
        val field = findInputField(root) ?: return false

        field.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
        field.performAction(AccessibilityNodeInfo.ACTION_CLICK)

        val args = Bundle().apply {
            putCharSequence(
                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                value
            )
        }

        var entered = field.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)
        if (!entered) {
            entered = pasteIntoField(context, field, value)
        }

        if (!entered) {
            field.recycleSafely()
            logger.log("Could not set USSD input text")
            return false
        }

        val submitted = submitDialog(root, logger)
        field.recycleSafely()
        if (!submitted) {
            logger.log("USSD input entered but submit button was not found")
        }
        return submitted
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

    private fun submitDialog(
        root: AccessibilityNodeInfo,
        logger: AutomationLogger
    ): Boolean {
        if (clickSubmitButton(root)) return true

        val fallback = clickFirstButton(root)
        if (fallback) {
            logger.log("Submitted USSD input using fallback button")
        }
        return fallback
    }

    private fun tryClickMenuOption(
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

    private fun clickSubmitButton(root: AccessibilityNodeInfo): Boolean {
        for (label in submitLabels) {
            val node = findNodeByText(root, label) ?: continue
            val clicked = clickNodeOrAncestor(node)
            node.recycleSafely()
            if (clicked) return true
        }
        return false
    }

    private fun clickFirstButton(root: AccessibilityNodeInfo): Boolean {
        val button = traverse(root) { node ->
            val className = node.className?.toString().orEmpty()
            if (node.isClickable && !className.contains("EditText", ignoreCase = true)) {
                node
            } else {
                null
            }
        } ?: return false
        return button.performAction(AccessibilityNodeInfo.ACTION_CLICK).also {
            button.recycleSafely()
        }
    }

    private fun clickNodeOrAncestor(node: AccessibilityNodeInfo): Boolean {
        var current: AccessibilityNodeInfo? = node
        var depth = 0
        while (current != null && depth < 4) {
            if (current.isClickable &&
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

    private fun findNodeByText(
        root: AccessibilityNodeInfo,
        text: String
    ): AccessibilityNodeInfo? {
        root.findAccessibilityNodeInfosByText(text)?.firstOrNull()?.let { return it }
        return traverse(root) { node ->
            if (node.nodeText().contains(text, ignoreCase = true) && node.isClickable) {
                node
            } else {
                null
            }
        }
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
