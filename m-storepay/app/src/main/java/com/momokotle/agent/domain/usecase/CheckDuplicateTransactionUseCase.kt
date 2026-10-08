package com.momokotle.agent.domain.usecase

import com.momokotle.agent.data.datastore.SettingsDataStore
import com.momokotle.agent.domain.repository.TransactionRepository
import com.momokotle.agent.domain.ussd.DuplicatePhoneMatcher
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.utils.PhoneNumberFormatter
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Local Room-only duplicate check. Returns the most recent matching transaction
 * inside the configured window, or null when Off / no match / invalid phone.
 * Does not call Laravel or mutate transaction state.
 */
class CheckDuplicateTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val settingsDataStore: SettingsDataStore
) {
    suspend operator fun invoke(phoneNumber: String?): Transaction? {
        if (phoneNumber.isNullOrBlank()) return null
        val window = settingsDataStore.settings.first().duplicateTransactionWarningWindow
        val windowMs = window.windowMs ?: return null
        val normalized = PhoneNumberFormatter.normalizeGhanaNumber(phoneNumber)
        val phones = DuplicatePhoneMatcher.candidates(normalized)
        if (phones.isEmpty()) return null
        val cutoff = System.currentTimeMillis() - windowMs
        return transactionRepository.findRecentByPhoneNumbers(phones, cutoff)
    }
}
