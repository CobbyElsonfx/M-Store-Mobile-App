package com.momokotle.agent.domain.usecase

import com.momokotle.agent.domain.repository.SavedContactRepository
import com.momokotle.agent.domain.repository.TransactionRepository
import com.momokotle.agent.models.ContactNetwork
import com.momokotle.agent.models.SavedContact
import com.momokotle.agent.models.Transaction
import com.momokotle.agent.models.TransactionStatus
import com.momokotle.agent.models.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PromoteFrequentCustomerContactUseCaseTest {

    private val contacts = FakeSavedContacts()
    private val transactions = FakeTransactions()
    private val useCase = PromoteFrequentCustomerContactUseCase(transactions, contacts)

    @Test
    fun thresholdHelper_firstUseNotPromoted() {
        assertFalse(PromoteFrequentCustomerContactUseCase.shouldPromote(1, alreadySaved = false))
        assertTrue(PromoteFrequentCustomerContactUseCase.shouldPromote(2, alreadySaved = false))
        assertFalse(PromoteFrequentCustomerContactUseCase.shouldPromote(5, alreadySaved = true))
    }

    @Test
    fun firstDial_doesNotSaveContact() = runBlocking {
        transactions.insert(
            tx(phone = "0241234567")
        )
        useCase("0241234567")
        assertEquals(0, contacts.list.size)
    }

    @Test
    fun secondDial_savesFrequentContact() = runBlocking {
        transactions.insert(tx(phone = "0241234567"))
        transactions.insert(tx(phone = "0241234567"))
        useCase("0241234567")
        assertEquals(1, contacts.list.size)
        assertEquals("0241234567", contacts.list.first().phoneNumber)
        assertEquals("Customer 0241234567", contacts.list.first().contactName)
    }

    @Test
    fun alreadySaved_bumpsUpdatedAtWithoutDuplicate() = runBlocking {
        val now = 1_000L
        contacts.list += SavedContact(
            id = 1,
            contactName = "Deborah",
            phoneNumber = "0241234567",
            network = ContactNetwork.MTN,
            createdAt = now,
            updatedAt = now
        )
        transactions.insert(tx(phone = "0241234567"))
        useCase("0241234567")
        assertEquals(1, contacts.list.size)
        assertTrue(contacts.list.first().updatedAt > now)
        assertEquals("Deborah", contacts.list.first().contactName)
    }

    @Test
    fun oneOffDifferentNumbers_notSaved() = runBlocking {
        transactions.insert(tx(phone = "0241111111"))
        transactions.insert(tx(phone = "0242222222"))
        useCase("0241111111")
        useCase("0242222222")
        assertEquals(0, contacts.list.size)
    }

    private fun tx(phone: String) = Transaction(
        type = TransactionType.MTN_CASHIN,
        amount = 10.0,
        phoneNumber = phone,
        status = TransactionStatus.ATTEMPTED
    )
}

private class FakeSavedContacts : SavedContactRepository {
    val list = mutableListOf<SavedContact>()

    override fun observeAll(): Flow<List<SavedContact>> = MutableStateFlow(list.toList())

    override suspend fun getByPhoneNumber(phoneNumber: String): SavedContact? =
        list.find { it.phoneNumber == phoneNumber }

    override suspend fun searchContacts(query: String, limit: Int): List<SavedContact> =
        list.filter {
            it.contactName.contains(query, ignoreCase = true) || it.phoneNumber.contains(query)
        }.take(limit)

    override suspend fun insertContact(contact: SavedContact): Long {
        val id = (list.maxOfOrNull { it.id } ?: 0L) + 1L
        list += contact.copy(id = id)
        return id
    }

    override suspend fun updateContact(contact: SavedContact) {
        val idx = list.indexOfFirst { it.id == contact.id }
        if (idx >= 0) list[idx] = contact
    }

    override suspend fun deleteContact(id: Long) {
        list.removeAll { it.id == id }
    }

    override suspend fun existsByPhoneNumber(phoneNumber: String): Boolean =
        list.any { it.phoneNumber == phoneNumber }
}

private class FakeTransactions : TransactionRepository {
    private val rows = mutableListOf<Transaction>()

    override fun observeAll(): Flow<List<Transaction>> = flowOf(rows)

    override fun observeByType(type: TransactionType): Flow<List<Transaction>> =
        flowOf(rows.filter { it.type == type })

    override fun observeRecent(limit: Int): Flow<List<Transaction>> =
        flowOf(rows.take(limit))

    override suspend fun getById(id: Long): Transaction? = rows.find { it.id == id }

    override suspend fun insert(transaction: Transaction): Long {
        val id = (rows.maxOfOrNull { it.id } ?: 0L) + 1L
        rows += transaction.copy(id = id)
        return id
    }

    override suspend fun updateStatus(id: Long, status: TransactionStatus) = Unit

    override suspend fun updateStatusAndCommission(
        id: Long,
        status: TransactionStatus,
        commission: Double,
        commissionRuleUsed: String?
    ) = Unit

    override suspend fun sumSuccessfulCommissionSince(
        type: TransactionType,
        startMillis: Long
    ): Double = 0.0

    override suspend fun findRecentByPhoneNumbers(
        phoneNumbers: List<String>,
        cutoffTime: Long
    ): Transaction? = rows
        .filter { it.phoneNumber in phoneNumbers && it.timestamp >= cutoffTime }
        .maxByOrNull { it.timestamp }

    override suspend fun countByPhoneNumbers(phoneNumbers: List<String>): Int =
        rows.count { it.phoneNumber in phoneNumbers }

    override suspend fun findFrequentPhonesMatching(
        query: String,
        minCount: Int,
        limit: Int
    ): List<String> =
        rows
            .mapNotNull { it.phoneNumber }
            .filter { it.contains(query) }
            .groupingBy { it }
            .eachCount()
            .filter { it.value >= minCount }
            .keys
            .take(limit)
            .toList()

    override suspend fun delete(id: Long) {
        rows.removeAll { it.id == id }
    }
}
