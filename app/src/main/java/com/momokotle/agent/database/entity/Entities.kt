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
import com.google.gson.reflect.TypeToken
import com.momokotle.agent.models.ProfitRuleType
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
    val phoneNumber: String?,
    val flowName: String?,
    val status: String,
    val note: String?,
    val timestamp: Long
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

@Entity(
    tableName = "profit_rules",
    indices = [Index(value = ["ruleType", "amount"], unique = true)]
)
data class ProfitRuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ruleType: String,
    val amount: Double,
    val profitPercentage: Double
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
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(tableName = "custom_flows")
data class CustomFlowEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val ussdCode: String,
    val step1: String,
    val step2: String,
    val step3: String,
    val useDynamicPhone: Boolean,
    val useDynamicAmount: Boolean
)

fun TransactionEntity.toDomain() = Transaction(
    id = id,
    type = TransactionType.valueOf(type),
    amount = amount,
    profit = profit,
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
    amount = amount,
    profitPercentage = profitPercentage
)

fun com.momokotle.agent.models.ProfitRule.toEntity() = ProfitRuleEntity(
    id = id,
    ruleType = ruleType.name,
    amount = amount,
    profitPercentage = profitPercentage
)

fun CustomFlowEntity.toDomain() = CustomFlow(
    id = id,
    name = name,
    ussdCode = ussdCode,
    step1 = step1,
    step2 = step2,
    step3 = step3,
    useDynamicPhone = useDynamicPhone,
    useDynamicAmount = useDynamicAmount
)

fun CustomFlow.toEntity() = CustomFlowEntity(
    id = id,
    name = name,
    ussdCode = ussdCode,
    step1 = step1,
    step2 = step2,
    step3 = step3,
    useDynamicPhone = useDynamicPhone,
    useDynamicAmount = useDynamicAmount
)

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

fun FormSubmissionEntity.toDomain(): FormSubmission? {
    val type = AgentFormType.fromDisplayName(formType)
        ?: runCatching { AgentFormType.valueOf(formType) }.getOrNull()
        ?: return null
    return FormSubmission(
        submissionId = submissionId,
        userId = userId,
        formType = type,
        applicantName = applicantName,
        phoneNumber = phoneNumber,
        formData = gson.fromJson(formDataJson, object : TypeToken<Map<String, String>>() {}.type),
        uploadedDocuments = gson.fromJson(documentsJson, object : TypeToken<Map<String, String>>() {}.type),
        status = FormSubmissionStatusMapper.fromStorage(status),
        adminNote = adminNote?.takeIf { it.isNotBlank() },
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

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
    createdAt = createdAt,
    updatedAt = updatedAt
)
