package com.momokotle.agent.domain.usecase

import com.momokotle.agent.domain.repository.SavedContactRepository
import com.momokotle.agent.domain.repository.TransactionRepository
import com.momokotle.agent.domain.ussd.DuplicatePhoneMatcher
import com.momokotle.agent.models.ContactNetwork
import com.momokotle.agent.models.SavedContact
import com.momokotle.agent.utils.PhoneNumberFormatter
import javax.inject.Inject

/**
 * Saves a customer phone into [saved_contacts] only after it has been used
 * frequently in local Room transaction history. One-off numbers are never saved.
 *
 * Already-saved contacts get [SavedContact.updatedAt] bumped so they rank in
 * dashboard auto-suggest.
 */
class PromoteFrequentCustomerContactUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val savedContactRepository: SavedContactRepository
) {
    suspend operator fun invoke(phoneNumber: String?) {
        val normalized = PhoneNumberFormatter.normalizeGhanaNumber(phoneNumber.orEmpty())
        if (!PhoneNumberFormatter.isValidGhanaNumber(normalized)) return

        val existing = savedContactRepository.getByPhoneNumber(normalized)
        val now = System.currentTimeMillis()
        if (existing != null) {
            savedContactRepository.updateContact(existing.copy(updatedAt = now))
            return
        }

        val phones = DuplicatePhoneMatcher.candidates(normalized)
        val usageCount = transactionRepository.countByPhoneNumbers(phones)
        if (usageCount < FREQUENT_USE_THRESHOLD) return

        try {
            savedContactRepository.insertContact(
                SavedContact(
                    contactName = autoName(normalized),
                    phoneNumber = normalized,
                    network = ContactNetwork.UNKNOWN,
                    isPorted = false,
                    createdAt = now,
                    updatedAt = now
                )
            )
        } catch (_: Exception) {
            // Unique phone race — already saved.
        }
    }

    companion object {
        /** Second use (and beyond) counts as frequent; first use is not saved. */
        const val FREQUENT_USE_THRESHOLD = 2

        fun autoName(normalizedPhone: String): String = "Customer $normalizedPhone"

        fun shouldPromote(usageCount: Int, alreadySaved: Boolean): Boolean {
            if (alreadySaved) return false
            return usageCount >= FREQUENT_USE_THRESHOLD
        }
    }
}
