package com.momokotle.agent.domain.usecase

import com.momokotle.agent.data.datastore.SettingsDataStore
import com.momokotle.agent.domain.engine.AgentCommissionEngine
import com.momokotle.agent.domain.repository.ProfitRuleRepository
import com.momokotle.agent.domain.repository.SavedContactRepository
import com.momokotle.agent.domain.repository.TransactionRepository
import com.momokotle.agent.models.AgentCommissionResult
import com.momokotle.agent.models.CommissionSummary
import com.momokotle.agent.models.ContactNetwork
import com.momokotle.agent.models.ProfitResult
import com.momokotle.agent.models.ProfitRuleType
import com.momokotle.agent.models.SavedContact
import com.momokotle.agent.models.TransactionType
import com.momokotle.agent.models.SimPurpose
import com.momokotle.agent.models.SimSlot
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.models.TransactionStatus
import com.momokotle.agent.models.simPurposeForTransaction
import com.momokotle.agent.ussd.UssdAutomationEngine
import com.momokotle.agent.ussd.UssdDialHelper
import com.momokotle.agent.ussd.UssdFlow
import com.momokotle.agent.ussd.UssdFlowContext
import com.momokotle.agent.utils.PhoneNumberFormatter
import com.momokotle.agent.utils.TransactionVerificationAnalyzer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Calendar
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
    private val settingsDataStore: SettingsDataStore,
    private val systemUssdFlowRepository: com.momokotle.agent.domain.repository.SystemUssdFlowRepository,
    private val cashOutSmsWatcher: com.momokotle.agent.sms.CashOutSmsWatcher
) {
    suspend operator fun invoke(
        flow: UssdFlow,
        phoneNumber: String = "",
        amount: String = "",
        transactionId: Long? = null
    ): Result<Unit> {
        // Subscription/access is resolved once by the presentation layer before dial.
        val settings = settingsDataStore.settings.first()
        val simPurpose = simPurposeForTransaction(flow.transactionType)
        val simSlot = settings.simSlotFor(simPurpose)

        val context = UssdFlowContext(
            phoneNumber = phoneNumber,
            amount = amount,
            recipient = phoneNumber
        )
        engine.beginStart(
            flow = flow,
            context = context,
            transactionId = transactionId,
            verificationEnabled = settings.transactionVerificationEnabled,
            stepDelayMs = settings.automationSpeed.stepDelayMs,
            debounceMs = settings.automationSpeed.debounceMs,
            submitSettleMs = settings.automationSpeed.submitSettleMs
        )
        // Cash Out success arrives only via MoMo SMS — arm watcher at dial time.
        if (flow.transactionType == TransactionType.MTN_CASHOUT && transactionId != null) {
            cashOutSmsWatcher.arm(transactionId, amountHint = amount.ifBlank { null })
        }
        engine.markDialing()
        val dialResult = dialHelper.dialUssdCode(flow.ussdCode, simSlot)
        if (dialResult.isSuccess) {
            engine.markRunning()
        } else {
            if (flow.transactionType == TransactionType.MTN_CASHOUT && transactionId != null) {
                cashOutSmsWatcher.disarm(transactionId)
            }
            engine.markFailed(dialResult.exceptionOrNull()?.message ?: "Could not dial USSD code")
        }
        return dialResult
    }

    suspend operator fun invoke(
        type: TransactionType,
        phoneNumber: String,
        amount: String,
        transactionId: Long? = null
    ): Result<Unit> = invoke(
        flow = systemUssdFlowRepository.getFlow(type),
        phoneNumber = phoneNumber,
        amount = amount,
        transactionId = transactionId
    )
}

/** Calculates agent commission for Cash-In / Cash-Out preview and on successful transactions. */
class CalculateVendorProfitUseCase @Inject constructor(
    private val agentCommissionEngine: AgentCommissionEngine,
    private val profitRuleRepository: ProfitRuleRepository,
    private val settingsDataStore: SettingsDataStore
) {
    suspend fun calculateCashIn(amount: Double): ProfitResult {
        if (amount <= 0) return ProfitResult(0.0)
        val settings = settingsDataStore.settings.first()
        val rules = profitRuleRepository.getTierRules(ProfitRuleType.CASH_IN)
        return toProfitResult(agentCommissionEngine.calculateCashIn(amount, settings, rules))
    }

    suspend fun calculateCashOut(amount: Double): ProfitResult {
        if (amount <= 0) return ProfitResult(0.0)
        val settings = settingsDataStore.settings.first()
        val rules = profitRuleRepository.getTierRules(ProfitRuleType.CASH_OUT)
        return toProfitResult(agentCommissionEngine.calculateCashOut(amount, settings, rules))
    }

    suspend fun calculateForSuccessfulTransaction(
        type: TransactionType,
        amount: Double
    ): AgentCommissionResult {
        if (amount <= 0) return AgentCommissionResult(0.0, null)
        val settings = settingsDataStore.settings.first()
        val cashInRules = profitRuleRepository.getTierRules(ProfitRuleType.CASH_IN)
        val cashOutRules = profitRuleRepository.getTierRules(ProfitRuleType.CASH_OUT)
        return agentCommissionEngine.calculateForTransaction(
            type = type,
            amount = amount,
            settings = settings,
            cashInRules = cashInRules,
            cashOutRules = cashOutRules
        )
    }

    private fun toProfitResult(result: AgentCommissionResult): ProfitResult {
        if (result.commission <= 0 && result.ruleUsed?.contains("no matching") == true) {
            return ProfitResult(0.0, result.ruleUsed)
        }
        return ProfitResult(result.commission, result.ruleUsed)
    }
}

class RecordAttemptedTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        type: TransactionType,
        flowName: String,
        amount: Double = 0.0,
        phoneNumber: String? = null
    ): Long {
        return transactionRepository.insert(
            Transaction(
                type = type,
                amount = amount,
                profit = 0.0,
                commission = 0.0,
                commissionRuleUsed = null,
                phoneNumber = phoneNumber,
                flowName = flowName,
                status = TransactionStatus.ATTEMPTED
            )
        )
    }
}

class UpdateTransactionStatusUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val calculateVendorProfitUseCase: CalculateVendorProfitUseCase,
    private val settingsDataStore: SettingsDataStore
) {
    suspend operator fun invoke(transactionId: Long, screenText: String) {
        val settings = settingsDataStore.settings.first()
        if (!settings.transactionVerificationEnabled) return
        val status = TransactionVerificationAnalyzer.analyze(screenText) ?: return
        applyStatus(transactionId, status)
    }

    suspend operator fun invoke(transactionId: Long, status: TransactionStatus) {
        applyStatus(transactionId, status)
    }

    private suspend fun applyStatus(transactionId: Long, status: TransactionStatus) {
        val transaction = transactionRepository.getById(transactionId) ?: return
        when (status) {
            TransactionStatus.SUCCESSFUL -> applySuccessfulCommission(transaction)
            TransactionStatus.FAILED -> clearCommission(transactionId, TransactionStatus.FAILED)
            else -> transactionRepository.updateStatus(transactionId, status)
        }
    }

    private suspend fun applySuccessfulCommission(transaction: Transaction) {
        val result = calculateVendorProfitUseCase.calculateForSuccessfulTransaction(
            type = transaction.type,
            amount = transaction.amount
        )
        transactionRepository.updateStatusAndCommission(
            id = transaction.id,
            status = TransactionStatus.SUCCESSFUL,
            commission = result.commission,
            commissionRuleUsed = result.ruleUsed
        )
    }

    private suspend fun clearCommission(transactionId: Long, status: TransactionStatus) {
        transactionRepository.updateStatusAndCommission(
            id = transactionId,
            status = status,
            commission = 0.0,
            commissionRuleUsed = null
        )
    }
}

enum class CommissionPeriod { TODAY, THIS_WEEK, THIS_MONTH, LIFETIME }

class ObserveCommissionSummaryUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    fun observe(period: CommissionPeriod): Flow<CommissionSummary> =
        transactionRepository.observeAll().map { transactions ->
            CommissionSummaryCalculator.summarize(
                transactions = transactions,
                startMillis = periodStartMillis(period),
                lifetime = period == CommissionPeriod.LIFETIME
            )
        }

    private fun periodStartMillis(period: CommissionPeriod): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return when (period) {
            CommissionPeriod.TODAY -> cal.timeInMillis
            CommissionPeriod.THIS_WEEK -> {
                cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
                cal.timeInMillis
            }
            CommissionPeriod.THIS_MONTH -> {
                cal.set(Calendar.DAY_OF_MONTH, 1)
                cal.timeInMillis
            }
            CommissionPeriod.LIFETIME -> 0L
        }
    }
}
