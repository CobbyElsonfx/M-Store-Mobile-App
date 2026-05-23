package com.momokotle.agent.domain.usecase

import com.momokotle.agent.data.datastore.SettingsDataStore
import com.momokotle.agent.domain.repository.ProfitRuleRepository
import com.momokotle.agent.domain.repository.SavedContactRepository
import com.momokotle.agent.domain.repository.TransactionRepository
import com.momokotle.agent.models.ContactNetwork
import com.momokotle.agent.models.SavedContact
import com.momokotle.agent.models.ProfitCalculationMode
import com.momokotle.agent.models.ProfitResult
import com.momokotle.agent.models.ProfitRuleType
import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.utils.PercentFormatter
import com.momokotle.agent.utils.ProfitAmountMatcher
import com.momokotle.agent.models.SimPurpose
import com.momokotle.agent.models.SimSlot
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.models.TransactionStatus
import com.momokotle.agent.models.simPurposeForTransaction
import com.momokotle.agent.ussd.UssdAutomationEngine
import com.momokotle.agent.ussd.UssdDialHelper
import com.momokotle.agent.ussd.UssdFlow
import com.momokotle.agent.ussd.UssdFlowContext
import com.momokotle.agent.ussd.UssdFlows
import com.momokotle.agent.utils.PhoneNumberFormatter
import com.momokotle.agent.utils.TransactionVerificationAnalyzer
import kotlinx.coroutines.flow.first
import javax.inject.Inject

sealed class SaveContactResult {
    data class Success(val contact: SavedContact) : SaveContactResult()
    data class Error(val message: String) : SaveContactResult()
}

class SaveContactUseCase @Inject constructor(
    private val savedContactRepository: SavedContactRepository
) {
    suspend operator fun invoke(
        contactName: String,
        phoneNumber: String,
        network: ContactNetwork,
        isPorted: Boolean
    ): SaveContactResult {
        val name = contactName.trim()
        val phone = PhoneNumberFormatter.normalizeGhanaNumber(phoneNumber)

        when {
            name.isBlank() -> return SaveContactResult.Error("Contact name cannot be empty")
            phone.isBlank() -> return SaveContactResult.Error("Phone number cannot be empty")
            !PhoneNumberFormatter.isValidGhanaNumber(phone) ->
                return SaveContactResult.Error("Enter a valid phone number")
        }

        if (savedContactRepository.existsByPhoneNumber(phone)) {
            return SaveContactResult.Error("Contact already saved")
        }

        val now = System.currentTimeMillis()
        val contact = SavedContact(
            contactName = name,
            phoneNumber = phone,
            network = network,
            isPorted = isPorted,
            createdAt = now,
            updatedAt = now
        )
        return try {
            val id = savedContactRepository.insertContact(contact)
            SaveContactResult.Success(contact.copy(id = id))
        } catch (_: Exception) {
            SaveContactResult.Error("Contact already saved")
        }
    }
}

class StartUssdFlowUseCase @Inject constructor(
    private val dialHelper: UssdDialHelper,
    private val engine: UssdAutomationEngine,
    private val settingsDataStore: SettingsDataStore
) {
    suspend operator fun invoke(
        flow: UssdFlow,
        phoneNumber: String = "",
        amount: String = "",
        transactionId: Long? = null
    ): Result<Unit> {
        val settings = settingsDataStore.settings.first()
        val simPurpose = simPurposeForTransaction(flow.transactionType)
        val simSlot = settings.simSlotFor(simPurpose)

        val context = UssdFlowContext(
            phoneNumber = phoneNumber,
            amount = amount,
            recipient = phoneNumber
        )
        engine.start(
            flow = flow,
            context = context,
            transactionId = transactionId,
            verificationEnabled = settings.transactionVerificationEnabled
        )
        return dialHelper.dialUssdCode(flow.ussdCode, simSlot)
    }

    suspend operator fun invoke(
        type: TransactionType,
        phoneNumber: String,
        amount: String,
        transactionId: Long? = null
    ): Result<Unit> = invoke(
        flow = UssdFlows.forTransactionType(type),
        phoneNumber = phoneNumber,
        amount = amount,
        transactionId = transactionId
    )
}

class CalculateVendorProfitUseCase @Inject constructor(
    private val profitRuleRepository: ProfitRuleRepository,
    private val settingsDataStore: SettingsDataStore
) {
    suspend fun calculateCashIn(amount: Double): ProfitResult {
        if (amount <= 0) return ProfitResult(0.0)
        val settings = settingsDataStore.settings.first()
        return when (settings.cashInCalculationMode) {
            ProfitCalculationMode.DEFAULT -> ProfitResult(
                profit = PercentFormatter.calculateProfit(amount, settings.defaultCashInPercentage)
            )
            ProfitCalculationMode.CUSTOMIZE -> {
                val normalized = ProfitAmountMatcher.normalize(amount)
                val rule = profitRuleRepository.findExactCustomRule(ProfitRuleType.CASH_IN, normalized)
                if (rule != null) {
                    ProfitResult(profit = rule.profitForAmount(normalized))
                } else {
                    ProfitResult(
                        profit = 0.0,
                        message = "No custom rule found for this amount"
                    )
                }
            }
        }
    }

    suspend fun calculateCashOut(amount: Double): ProfitResult {
        if (amount <= 0) return ProfitResult(0.0)
        val settings = settingsDataStore.settings.first()
        return when (settings.cashOutCalculationMode) {
            ProfitCalculationMode.DEFAULT -> ProfitResult(
                profit = PercentFormatter.calculateProfit(amount, settings.defaultCashOutPercentage)
            )
            ProfitCalculationMode.CUSTOMIZE -> {
                val normalized = ProfitAmountMatcher.normalize(amount)
                val rule = profitRuleRepository.findExactCustomRule(ProfitRuleType.CASH_OUT, normalized)
                if (rule != null) {
                    ProfitResult(profit = rule.profitForAmount(normalized))
                } else {
                    ProfitResult(
                        profit = 0.0,
                        message = "No custom rule found for this amount"
                    )
                }
            }
        }
    }

    suspend fun calculateForTransaction(type: TransactionType, amount: Double): Double {
        if (amount <= 0) return 0.0
        return when (type) {
            TransactionType.MTN_CASHIN -> calculateCashIn(amount).profit
            TransactionType.MTN_CASHOUT -> calculateCashOut(amount).profit
            else -> 0.0
        }
    }
}

class RecordAttemptedTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val calculateVendorProfitUseCase: CalculateVendorProfitUseCase,
    private val settingsDataStore: SettingsDataStore
) {
    suspend operator fun invoke(
        type: TransactionType,
        flowName: String,
        amount: Double = 0.0,
        phoneNumber: String? = null
    ): Long {
        val settings = settingsDataStore.settings.first()
        val profit = calculateVendorProfitUseCase.calculateForTransaction(type, amount)
        val status = if (settings.transactionVerificationEnabled) {
            TransactionStatus.ATTEMPTED
        } else {
            TransactionStatus.ATTEMPTED
        }
        return transactionRepository.insert(
            Transaction(
                type = type,
                amount = amount,
                profit = profit,
                phoneNumber = phoneNumber,
                flowName = flowName,
                status = status
            )
        )
    }
}

class UpdateTransactionStatusUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val settingsDataStore: SettingsDataStore
) {
    suspend operator fun invoke(transactionId: Long, screenText: String) {
        val settings = settingsDataStore.settings.first()
        if (!settings.transactionVerificationEnabled) return
        val status = TransactionVerificationAnalyzer.analyze(screenText) ?: return
        transactionRepository.updateStatus(transactionId, status)
    }

    suspend operator fun invoke(transactionId: Long, status: TransactionStatus) {
        val settings = settingsDataStore.settings.first()
        if (!settings.transactionVerificationEnabled) return
        transactionRepository.updateStatus(transactionId, status)
    }
}
