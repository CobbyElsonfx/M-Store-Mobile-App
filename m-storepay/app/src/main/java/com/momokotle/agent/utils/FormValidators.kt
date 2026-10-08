package com.momokotle.agent.utils

import com.momokotle.agent.data.form.FormFieldKind
import com.momokotle.agent.data.form.FormOptions
import com.momokotle.agent.data.form.FormTextField
import com.momokotle.agent.data.form.FormUploadKind
import com.momokotle.agent.data.form.FormUploadSlot
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

object FormValidators {

    private val GHANA_PHONE_PREFIXES = setOf(
        "020", "023", "024", "025", "026", "027", "028", "029",
        "050", "053", "054", "055", "056", "057", "059"
    )

    private val GHANA_CARD_PATTERN = Regex("^GHA-\\d{9}-\\d$")
    private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.UK)
    const val MAX_UPLOAD_BYTES = 10L * 1024L * 1024L

    private val SIGNATURE_EXTENSIONS = setOf("png", "jpg", "jpeg")
    private val DOCUMENT_EXTENSIONS = setOf("png", "jpg", "jpeg", "pdf")

    fun filterInput(field: FormTextField, raw: String): String = when (field.kind) {
        FormFieldKind.PHONE -> raw.filter { it.isDigit() }.take(10)
        FormFieldKind.GHANA_CARD -> raw.uppercase(Locale.UK).filter { it.isLetterOrDigit() || it == '-' }.take(15)
        FormFieldKind.NAME -> raw.filter { it.isLetter() || it.isWhitespace() || it == '-' || it == '\'' }
        FormFieldKind.BUSINESS_NAME -> BusinessNameValidator.filterInput(raw)
        FormFieldKind.PLACE_OF_BIRTH -> raw.filter { it.isLetter() || it.isWhitespace() || it == '-' }
        FormFieldKind.YEAR -> raw.filter { it.isDigit() }.take(4)
        FormFieldKind.EMAIL -> raw.filter { !it.isWhitespace() }.take(120)
        else -> raw
    }

    fun normalizeField(field: FormTextField, value: String): String = when (field.kind) {
        FormFieldKind.PHONE -> value.filter { it.isDigit() }.take(10)
        FormFieldKind.GHANA_CARD -> value.uppercase(Locale.UK).trim()
        FormFieldKind.NAME, FormFieldKind.PLACE_OF_BIRTH, FormFieldKind.LOCATION ->
            normalizeName(value)
        FormFieldKind.BUSINESS_NAME -> BusinessNameValidator.normalize(value)
        else -> value.trim()
    }

    fun validateField(field: FormTextField, rawValue: String): String? {
        val value = normalizeField(field, rawValue)
        if (value.isBlank()) {
            return if (field.required) "${field.label} is required" else null
        }
        return when (field.kind) {
            FormFieldKind.PHONE -> validateGhanaPhone(value)
            FormFieldKind.GHANA_CARD -> validateGhanaCard(value)
            FormFieldKind.DATE_OF_BIRTH -> validateDateOfBirth(value)
            FormFieldKind.NAME -> validateName(value, field.label)
            FormFieldKind.BUSINESS_NAME -> {
                if (!field.required && value.isBlank()) null
                else BusinessNameValidator.validate(value, field.label)
            }
            FormFieldKind.PLACE_OF_BIRTH -> validatePlace(value, "Place of Birth")
            FormFieldKind.LOCATION -> validatePlace(value, "Location")
            FormFieldKind.SELECT -> validateSelect(value, field)
            FormFieldKind.DATE -> validatePastDate(value, field.label)
            FormFieldKind.EMAIL -> validateEmail(value)
            FormFieldKind.YEAR -> validateYear(value, field.label)
            FormFieldKind.TEXT -> null
        }
    }

    fun isFieldValid(field: FormTextField, rawValue: String): Boolean =
        validateField(field, rawValue) == null

    fun validateUpload(slot: FormUploadSlot, localPath: String?): String? {
        if (localPath.isNullOrBlank()) {
            return if (slot.required) {
                when (slot.kind) {
                    FormUploadKind.SIGNATURE -> "Signature is required."
                    FormUploadKind.DOCUMENT -> "Upload ${slot.label}"
                }
            } else {
                null
            }
        }
        val file = File(localPath)
        if (!file.exists() || file.length() == 0L) {
            return "Re-select ${slot.label} — file is no longer available"
        }
        if (file.length() > MAX_UPLOAD_BYTES) {
            return "File must be 10 MB or smaller"
        }
        val extension = file.extension.lowercase(Locale.UK)
        val allowed = when (slot.kind) {
            FormUploadKind.SIGNATURE -> SIGNATURE_EXTENSIONS
            FormUploadKind.DOCUMENT -> DOCUMENT_EXTENSIONS
        }
        return if (extension in allowed) {
            null
        } else {
            when (slot.kind) {
                FormUploadKind.SIGNATURE -> "Signature must be PNG, JPG, or JPEG"
                FormUploadKind.DOCUMENT -> "Allowed formats: JPG, JPEG, PNG, PDF"
            }
        }
    }

    fun isUploadValid(slot: FormUploadSlot, localPath: String?): Boolean =
        validateUpload(slot, localPath) == null

    fun validateUploadedDocument(
        slot: FormUploadSlot,
        value: String?,
        isUploading: Boolean
    ): String? {
        if (isUploading) return "Uploading document…"
        if (value.isNullOrBlank()) {
            return if (slot.required) {
                when (slot.kind) {
                    FormUploadKind.SIGNATURE -> "Signature is required."
                    FormUploadKind.DOCUMENT -> "Upload ${slot.label}"
                }
            } else {
                null
            }
        }
        return if (value.startsWith("http", ignoreCase = true)) {
            null
        } else {
            "Document upload incomplete. Please wait or re-select the file."
        }
    }

    fun isUploadedDocumentValid(
        slot: FormUploadSlot,
        value: String?,
        isUploading: Boolean
    ): Boolean = validateUploadedDocument(slot, value, isUploading) == null

    fun validateGhanaPhone(value: String): String? {
        val digits = value.filter { it.isDigit() }
        if (digits.length != 10) return "Enter a valid Ghana phone number."
        if (!digits.startsWith("0")) return "Enter a valid Ghana phone number."
        val prefix = digits.take(3)
        return if (prefix in GHANA_PHONE_PREFIXES) null else "Enter a valid Ghana phone number."
    }

    fun validateGhanaCard(value: String): String? =
        if (GHANA_CARD_PATTERN.matches(value)) null else "Enter a valid Ghana Card number."

    fun validateDateOfBirth(value: String): String? {
        val date = parseDate(value) ?: return "Select a valid date (DD/MM/YYYY)"
        val today = LocalDate.now()
        if (date.isAfter(today)) return "Date of birth cannot be in the future"
        if (date.isAfter(today.minusYears(18))) return "Applicant must be at least 18 years old."
        return null
    }

    fun validateName(value: String, label: String): String? {
        val normalized = normalizeName(value)
        return if (normalized.length < 2) "$label must be at least 2 characters" else null
    }

    fun validatePlace(value: String, label: String): String? {
        val trimmed = value.trim()
        return if (trimmed.length < 2) "$label must be at least 2 characters" else null
    }

    fun validateSelect(value: String, field: FormTextField): String? {
        if (field.options.isEmpty()) return null
        return if (value in field.options) null else "Select a valid ${field.label.lowercase()}"
    }

    fun validatePastDate(value: String, label: String): String? {
        val date = parseDate(value) ?: return "Select a valid date (DD/MM/YYYY)"
        if (date.isAfter(LocalDate.now())) return "$label cannot be in the future"
        return null
    }

    fun validateEmail(value: String): String? {
        val email = value.trim()
        val valid = email.length >= 5 &&
            email.contains('@') &&
            email.substringAfter('@').contains('.') &&
            !email.startsWith('@') &&
            !email.endsWith('@') &&
            !email.endsWith('.')
        return if (valid) null else "Enter a valid email address"
    }

    fun validateYear(value: String, label: String): String? {
        val year = value.toIntOrNull() ?: return "$label must be a 4-digit year"
        val current = LocalDate.now().year
        if (year < 1900 || year > current) return "$label must be between 1900 and $current"
        return null
    }

    fun educationDateOrderError(values: Map<String, String>): String? {
        if (values["hasFormalEducation"] != "Yes") return null
        val startYear = values["schoolYearStarted"]?.toIntOrNull() ?: return null
        val endYear = values["schoolYearEnded"]?.toIntOrNull() ?: return null
        val startMonth = monthIndex(values["schoolMonthStarted"].orEmpty())
        val endMonth = monthIndex(values["schoolMonthEnded"].orEmpty())
        if (endYear < startYear) return "Education end date should not be before start date"
        if (endYear == startYear && startMonth != null && endMonth != null && endMonth < startMonth) {
            return "Education end date should not be before start date"
        }
        return null
    }

    private fun monthIndex(month: String): Int? {
        val index = FormOptions.MONTH_OPTIONS.indexOf(month)
        return index.takeIf { it >= 0 }
    }

    fun formatDate(date: LocalDate): String = date.format(DATE_FORMATTER)

    fun parseDate(value: String): LocalDate? = try {
        LocalDate.parse(value.trim(), DATE_FORMATTER)
    } catch (_: DateTimeParseException) {
        null
    }

    fun normalizeName(value: String): String =
        value.trim()
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }
            .joinToString(" ") { word ->
                word.lowercase(Locale.UK).replaceFirstChar { ch ->
                    if (ch.isLowerCase()) ch.titlecase(Locale.UK) else ch.toString()
                }
            }

    fun placeholderFor(field: FormTextField): String = field.placeholder ?: when (field.kind) {
        FormFieldKind.PHONE -> "0241234567"
        FormFieldKind.GHANA_CARD -> "GHA-123456789-1"
        FormFieldKind.DATE_OF_BIRTH -> "Select date"
        FormFieldKind.NAME -> when (field.key) {
            "surname" -> "Mensah"
            "firstName" -> "Kwame"
            "fullName" -> "Kwame Mensah"
            "nextOfKinName" -> "Ama Mensah"
            else -> ""
        }
        FormFieldKind.BUSINESS_NAME -> when (field.key) {
            "businessName1" -> "ADDO 4 Ent."
            else -> "SMART-LINK ENTERPRISE"
        }
        FormFieldKind.PLACE_OF_BIRTH -> "Accra"
        FormFieldKind.LOCATION -> "Kasoa"
        FormFieldKind.SELECT -> field.placeholder ?: "Select an option"
        FormFieldKind.DATE -> "Select date"
        FormFieldKind.EMAIL -> "name@example.com"
        FormFieldKind.YEAR -> "2010"
        else -> ""
    }

    fun maxLengthFor(field: FormTextField): Int? = field.maxLength ?: when (field.kind) {
        FormFieldKind.PHONE -> 10
        FormFieldKind.GHANA_CARD -> 15
        FormFieldKind.BUSINESS_NAME -> BusinessNameValidator.MAX_LENGTH
        FormFieldKind.YEAR -> 4
        FormFieldKind.EMAIL -> 120
        else -> null
    }
}
