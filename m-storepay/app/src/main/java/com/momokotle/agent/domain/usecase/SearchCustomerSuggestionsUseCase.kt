package com.momokotle.agent.domain.usecase

import com.momokotle.agent.domain.repository.SavedContactRepository
import com.momokotle.agent.domain.repository.TransactionRepository
import com.momokotle.agent.models.ContactNetwork
import com.momokotle.agent.models.SavedContact
import com.momokotle.agent.utils.PhoneNumberFormatter
import javax.inject.Inject

/**
 * Dashboard phone auto-suggest: manually + auto-saved contacts, plus frequent
 * history numbers not yet promoted (so typing still surfaces them).
 */
class SearchCustomerSuggestionsUseCase @Inject constructor(
    private val savedContactRepository: SavedContactRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(query: String, limit: Int = 8): List<SavedContact> {
        val trimmed = query.trim()
        if (trimmed.length < 2) return emptyList()

        val saved = savedContactRepository.searchContacts(trimmed, limit)
        if (saved.size >= limit) return saved

        val savedPhones = saved.map { it.phoneNumber }.toHashSet()
        val remaining = limit - saved.size
        val frequentPhones = transactionRepository.findFrequentPhonesMatching(
            query = trimmed,
            minCount = PromoteFrequentCustomerContactUseCase.FREQUENT_USE_THRESHOLD,
            limit = remaining + savedPhones.size
        )

        val extras = frequentPhones
            .map { PhoneNumberFormatter.normalizeGhanaNumber(it) }
            .filter { PhoneNumberFormatter.isValidGhanaNumber(it) }
            .distinct()
            .filter { it !in savedPhones }
            .take(remaining)
            .map { phone ->
                SavedContact(
                    id = 0L,
                    contactName = PromoteFrequentCustomerContactUseCase.autoName(phone),
                    phoneNumber = phone,
                    network = ContactNetwork.UNKNOWN,
                    isPorted = false
                )
            }

        return saved + extras
    }
}
