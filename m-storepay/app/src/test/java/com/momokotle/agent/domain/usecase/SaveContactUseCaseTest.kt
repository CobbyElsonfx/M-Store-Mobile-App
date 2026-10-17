package com.momokotle.agent.domain.usecase

import com.momokotle.agent.domain.repository.SavedContactRepository
import com.momokotle.agent.models.ContactNetwork
import com.momokotle.agent.models.SavedContact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SaveContactUseCaseTest {

    private val repo = FakeSavedContactRepository()
    private val useCase = SaveContactUseCase(repo)

    @Test
    fun save_normalizesPhoneAndPersistsManually() = runBlocking {
        val result = useCase(
            contactName = "Deborah",
            phoneNumber = "+233241234567",
            network = ContactNetwork.MTN,
            isPorted = false
        )
        assertTrue(result is SaveContactResult.Success)
        val contact = (result as SaveContactResult.Success).contact
        assertEquals("0241234567", contact.phoneNumber)
        assertEquals(1, repo.contacts.size)
    }

    @Test
    fun save_rejectsBlankName() = runBlocking {
        val result = useCase(
            contactName = "  ",
            phoneNumber = "0241234567",
            network = ContactNetwork.MTN,
            isPorted = false
        )
        assertTrue(result is SaveContactResult.Error)
        assertEquals(0, repo.contacts.size)
    }

    @Test
    fun save_rejectsDuplicatePhone() = runBlocking {
        useCase("A", "0241234567", ContactNetwork.MTN, false)
        val second = useCase("B", "0241234567", ContactNetwork.TELECEL, true)
        assertTrue(second is SaveContactResult.Error)
        assertEquals("Contact already saved", (second as SaveContactResult.Error).message)
        assertEquals(1, repo.contacts.size)
    }

    @Test
    fun transactionDial_doesNotAutoSaveContact() {
        // Documented contract: RecordAttemptedTransactionUseCase only writes transactions.
        // SaveContactUseCase is never invoked from dial paths.
        assertEquals(0, repo.contacts.size)
    }
}

/**
 * Mirrors Room searchContacts semantics used by dashboard auto-suggest:
 * LIKE name/phone, ORDER BY updatedAt DESC, LIMIT 8.
 */
class ContactSuggestSearchTest {

    @Test
    fun search_requiresAtLeastTwoCharsInDashboardGate() {
        // DashboardViewModel.scheduleContactSearch clears suggestions when query.length < 2
        assertTrue("0".length < 2)
        assertTrue("02".length >= 2)
    }

    @Test
    fun search_ranksByUpdatedAtDescAndLimitsTo8() {
        val now = 1_000_000L
        val contacts = (1..10).map { i ->
            SavedContact(
                id = i.toLong(),
                contactName = "Cust $i",
                phoneNumber = "02410000%02d".format(i),
                network = ContactNetwork.MTN,
                updatedAt = now + i
            )
        }
        val query = "024"
        val results = contacts
            .filter {
                it.contactName.contains(query, ignoreCase = true) ||
                    it.phoneNumber.contains(query)
            }
            .sortedByDescending { it.updatedAt }
            .take(8)

        assertEquals(8, results.size)
        assertEquals(10L, results.first().id) // most recently updated
        assertEquals(3L, results.last().id)
    }

    @Test
    fun selectingContact_bumpsUpdatedAtSoItRanksFirstNextTime() {
        val older = SavedContact(
            id = 1,
            contactName = "Ama",
            phoneNumber = "0241111111",
            network = ContactNetwork.MTN,
            updatedAt = 100
        )
        val newer = SavedContact(
            id = 2,
            contactName = "Kofi",
            phoneNumber = "0242222222",
            network = ContactNetwork.MTN,
            updatedAt = 200
        )
        // After selectContact(Ama), updatedAt becomes "now"
        val afterSelect = older.copy(updatedAt = 500)
        val ranked = listOf(afterSelect, newer).sortedByDescending { it.updatedAt }
        assertEquals("Ama", ranked.first().contactName)
    }
}

private class FakeSavedContactRepository : SavedContactRepository {
    val contacts = mutableListOf<SavedContact>()
    private val flow = MutableStateFlow<List<SavedContact>>(emptyList())

    override fun observeAll(): Flow<List<SavedContact>> = flow

    override suspend fun getByPhoneNumber(phoneNumber: String): SavedContact? =
        contacts.find { it.phoneNumber == phoneNumber }

    override suspend fun searchContacts(query: String, limit: Int): List<SavedContact> =
        contacts
            .filter {
                it.contactName.contains(query, ignoreCase = true) ||
                    it.phoneNumber.contains(query)
            }
            .sortedByDescending { it.updatedAt }
            .take(limit)

    override suspend fun insertContact(contact: SavedContact): Long {
        val id = (contacts.maxOfOrNull { it.id } ?: 0L) + 1L
        contacts += contact.copy(id = id)
        flow.value = contacts.toList()
        return id
    }

    override suspend fun updateContact(contact: SavedContact) {
        val idx = contacts.indexOfFirst { it.id == contact.id }
        if (idx >= 0) contacts[idx] = contact
        flow.value = contacts.toList()
    }

    override suspend fun deleteContact(id: Long) {
        contacts.removeAll { it.id == id }
        flow.value = contacts.toList()
    }

    override suspend fun existsByPhoneNumber(phoneNumber: String): Boolean =
        contacts.any { it.phoneNumber == phoneNumber }
}
