package com.momokotle.agent.data.form

import java.io.File
import java.util.Locale

/**
 * Maps form upload slot keys to Firebase Storage filenames and Firestore document URL fields.
 */
object DocumentSlotRegistry {

    data class SlotMapping(
        val slotKey: String,
        val storageFileName: String,
        val firestoreUrlKey: String
    )

    private val mappings = listOf(
        SlotMapping("ghanaCardFront", "ghana_card_front.jpg", "ghanaCardFrontUrl"),
        SlotMapping("ghanaCardBack", "ghana_card_back.jpg", "ghanaCardBackUrl"),
        SlotMapping("businessCertificate", "business_certificate.pdf", "businessCertificateUrl"),
        SlotMapping("formA", "form_a.pdf", "formAUrl"),
        SlotMapping("formB", "form_b.pdf", "formBUrl"),
        SlotMapping("formC", "form_c.pdf", "formCUrl"),
        SlotMapping("signature", "signature.png", "signatureUrl"),
        SlotMapping("applicantHoldingFront", "applicant_front.jpg", "applicantFrontUrl"),
        SlotMapping("applicantHoldingBack", "applicant_back.jpg", "applicantBackUrl"),
        SlotMapping("ghanaCard", "ghana_card.jpg", "ghanaCardUrl"),
        SlotMapping("birthProof", "birth_proof.pdf", "birthProofUrl"),
        SlotMapping("businessCertificateFormA", "form_a.pdf", "formAUrl"),
        SlotMapping("businessCertificateFormB", "form_b.pdf", "formBUrl"),
        SlotMapping("businessCertificateFormC", "form_c.pdf", "formCUrl")
    )

    private val bySlotKey = mappings.associateBy { it.slotKey }
    private val byFirestoreKey = mappings.associateBy { it.firestoreUrlKey }

    fun mappingFor(slotKey: String): SlotMapping =
        bySlotKey[slotKey] ?: SlotMapping(
            slotKey = slotKey,
            storageFileName = "${slotKey.replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase(Locale.UK)}.bin",
            firestoreUrlKey = "${slotKey}Url"
        )

    fun storageFileName(slotKey: String, localPath: String): String {
        val mapping = mappingFor(slotKey)
        val extension = File(localPath).extension.lowercase(Locale.UK).ifBlank { "bin" }
        val base = mapping.storageFileName.substringBeforeLast('.')
        return "$base.$extension"
    }

    fun slotUrlsToFirestoreDocuments(slotUrls: Map<String, String>): Map<String, String> =
        slotUrls.mapKeys { (slotKey, _) -> mappingFor(slotKey).firestoreUrlKey }

    fun firestoreDocumentsToSlotUrls(documents: Map<String, String>): Map<String, String> =
        documents.mapNotNull { (urlKey, url) ->
            val slotKey = byFirestoreKey[urlKey]?.slotKey
                ?: urlKey.removeSuffix("Url").takeIf { it.isNotBlank() }
            slotKey?.let { it to url }
        }.toMap()

    fun documentDisplayName(firestoreUrlKey: String): String = when (firestoreUrlKey) {
        "ghanaCardFrontUrl" -> "Ghana Card Front"
        "ghanaCardBackUrl" -> "Ghana Card Back"
        "businessCertificateUrl" -> "Business Certificate"
        "formAUrl" -> "Form A"
        "formBUrl" -> "Form B"
        "formCUrl" -> "Form C"
        "signatureUrl" -> "Signature"
        "applicantFrontUrl" -> "Applicant Holding Front"
        "applicantBackUrl" -> "Applicant Holding Back"
        "ghanaCardUrl" -> "Ghana Card"
        "birthProofUrl" -> "Birth Proof"
        else -> firestoreUrlKey.removeSuffix("Url")
    }
}
