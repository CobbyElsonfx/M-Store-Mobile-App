package com.momokotle.agent.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.momokotle.agent.models.ContactNetwork
import com.momokotle.agent.models.AgentFormType
import com.momokotle.agent.models.CustomFlow
import com.momokotle.agent.models.FormSubmission
import com.momokotle.agent.models.FormSubmissionStatusMapper
import com.momokotle.agent.models.OrderStatusMapper
import com.momokotle.agent.models.StoreOrder
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.momokotle.agent.models.ProfitRuleType
import com.momokotle.agent.utils.JsonStringMaps
import com.momokotle.agent.models.SavedContact
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.models.TransactionStatus
import com.momokotle.agent.models.TransactionType

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val amount: Double,
    val profit: Double,
    val commission: Double = 0.0,
    val commissionRuleUsed: String? = null,
    val phoneNumber: String?,
    val flowName: String?,
    val status: String,
    val note: String?,
    val timestamp: Long
)

@Entity(
    tableName = "profit_rules",
    indices = [Index(value = ["ruleType"])]
)
data class ProfitRuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ruleType: String,
    val minAmount: Double,
    val maxAmount: Double,
    val fixedCommission: Double = 0.0,
    val profitPercentage: Double = 0.0,
    val useFixedCommission: Boolean = false
)

@Entity(
    tableName = "saved_contacts",
    indices = [Index(value = ["phoneNumber"], unique = true)]
)
data class SavedContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val contactName: String,
    val phoneNumber: String,
    val network: String,
    val isPorted: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(tableName = "store_orders")
data class StoreOrderEntity(
    @PrimaryKey val orderId: String,
    val userId: String,
    val agentName: String,
    val itemName: String,
    val quantity: Int,
    val totalAmount: Double,
    val phoneNumber: String,
    val location: String,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(tableName = "form_submissions")
data class FormSubmissionEntity(
    @PrimaryKey val submissionId: String,
    val userId: String,
    val applicantName: String,
    val phoneNumber: String,
    val formType: String,
    val formDataJson: String,
    val documentsJson: String,
    val status: String,
    val adminNote: String?,
    val requiresPayment: Boolean = false,
    val paymentAmount: Double = 0.0,
    val paymentStatus: String? = null,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(tableName = "custom_flows")
data class CustomFlowEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val ussdCode: String,
    /** JSON array of step strings (max [CustomFlow.MAX_STEPS]). */
    val stepsJson: String = "[]",
    val useDynamicPhone: Boolean,
    val useDynamicAmount: Boolean
)

fun TransactionEntity.toDomain() = Transaction(
    id = id,
    type = TransactionType.valueOf(type),
    amount = amount,
    profit = profit,
    commission = commission,
    commissionRuleUsed = commissionRuleUsed,
    phoneNumber = phoneNumber,
    flowName = flowName,
    status = TransactionStatus.valueOf(status),
    note = note,
    timestamp = timestamp
)

fun Transaction.toEntity() = TransactionEntity(
    id = id,
    type = type.name,
    amount = amount,
    profit = profit,
    commission = commission,
    commissionRuleUsed = commissionRuleUsed,
    phoneNumber = phoneNumber,
    flowName = flowName,
    status = status.name,
    note = note,
    timestamp = timestamp
)

fun SavedContactEntity.toDomain() = SavedContact(
    id = id,
    contactName = contactName,
    phoneNumber = phoneNumber,
    network = ContactNetwork.valueOf(network),
    isPorted = isPorted,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun SavedContact.toEntity() = SavedContactEntity(
    id = id,
    contactName = contactName,
    phoneNumber = phoneNumber,
    network = network.name,
    isPorted = isPorted,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ProfitRuleEntity.toDomain() = com.momokotle.agent.models.ProfitRule(
    id = id,
    ruleType = ProfitRuleType.valueOf(ruleType),
    minAmount = minAmount,
    maxAmount = maxAmount,
    fixedCommission = fixedCommission,
    profitPercentage = profitPercentage,
    useFixedCommission = useFixedCommission
)

fun com.momokotle.agent.models.ProfitRule.toEntity() = ProfitRuleEntity(
    id = id,
    ruleType = ruleType.name,
    minAmount = minAmount,
    maxAmount = maxAmount,
    fixedCommission = fixedCommission,
    profitPercentage = profitPercentage,
    useFixedCommission = useFixedCommission
)

fun CustomFlowEntity.toDomain() = CustomFlow(
    id = id,
    name = name,
    ussdCode = ussdCode,
    steps = parseCustomFlowSteps(stepsJson),
    useDynamicPhone = useDynamicPhone,
    useDynamicAmount = useDynamicAmount
)

fun CustomFlow.toEntity() = CustomFlowEntity(
    id = id,
    name = name,
    ussdCode = ussdCode,
    stepsJson = encodeCustomFlowSteps(normalizedSteps()),
    useDynamicPhone = useDynamicPhone,
    useDynamicAmount = useDynamicAmount
)

/** Encode without TypeToken so release minify cannot break step persistence. */
internal fun encodeCustomFlowSteps(steps: List<String>): String {
    val array = JsonArray()
    steps.forEach { array.add(it) }
    return array.toString()
}

/** Decode JSON array of strings; never relies on reflective TypeToken. */
internal fun parseCustomFlowSteps(json: String): List<String> {
    if (json.isBlank()) return emptyList()
    return runCatching {
        val element = JsonParser.parseString(json)
        if (!element.isJsonArray) return@runCatching emptyList()
        element.asJsonArray.mapNotNull { item ->
            when {
                item.isJsonPrimitive -> item.asString.trim().takeIf { it.isNotEmpty() }
                else -> null
            }
        }.take(CustomFlow.MAX_STEPS)
    }.getOrDefault(emptyList())
}

private val gson = Gson()

fun StoreOrderEntity.toDomain() = StoreOrder(
    orderId = orderId,
    userId = userId,
    agentName = agentName,
    itemName = itemName,
    quantity = quantity,
    totalAmount = totalAmount,
    phoneNumber = phoneNumber,
    location = location,
    status = OrderStatusMapper.fromStorage(status),
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun StoreOrder.toEntity() = StoreOrderEntity(
    orderId = orderId,
    userId = userId,
    agentName = agentName,
    itemName = itemName,
    quantity = quantity,
    totalAmount = totalAmount,
    phoneNumber = phoneNumber,
    location = location,
    status = OrderStatusMapper.toStorageValue(status),
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun FormSubmissionEntity.toDomain(): FormSubmission? = runCatching {
    val type = AgentFormType.fromDisplayName(formType)
        ?: runCatching { AgentFormType.valueOf(formType) }.getOrNull()
        ?: return@runCatching null
    FormSubmission(
        submissionId = submissionId,
        userId = userId,
        formType = type,
        applicantName = applicantName,
        phoneNumber = phoneNumber,
        formData = JsonStringMaps.fromJson(formDataJson),
        uploadedDocuments = JsonStringMaps.fromJson(documentsJson),
        status = FormSubmissionStatusMapper.fromStorage(status),
        adminNote = adminNote?.takeIf { it.isNotBlank() },
        requiresPayment = requiresPayment,
        paymentAmount = paymentAmount,
        paymentStatus = paymentStatus,
        isPaid = paymentStatus.equals("Confirmed", ignoreCase = true),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}.getOrNull()

fun FormSubmission.toEntity() = FormSubmissionEntity(
    submissionId = submissionId,
    userId = userId,
    applicantName = applicantName,
    phoneNumber = phoneNumber,
    formType = formType.name,
    formDataJson = gson.toJson(formData),
    documentsJson = gson.toJson(uploadedDocuments),
    status = FormSubmissionStatusMapper.toStorageValue(status),
    adminNote = adminNote,
    requiresPayment = requiresPayment,
    paymentAmount = paymentAmount,
    paymentStatus = paymentStatus,
    createdAt = createdAt,
    updatedAt = updatedAt
)
