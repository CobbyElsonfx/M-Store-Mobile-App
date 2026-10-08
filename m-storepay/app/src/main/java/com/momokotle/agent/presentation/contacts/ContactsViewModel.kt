package com.momokotle.agent.presentation.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.domain.repository.SavedContactRepository
import com.momokotle.agent.domain.usecase.SaveContactResult
import com.momokotle.agent.domain.usecase.SaveContactUseCase
import com.momokotle.agent.models.ContactNetwork
import com.momokotle.agent.models.SavedContact
import com.momokotle.agent.ui.components.SaveContactFormState
import com.momokotle.agent.utils.PhoneNumberFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ContactsUiState(
    val searchQuery: String = "",
    val allContacts: List<SavedContact> = emptyList(),
    val filteredContacts: List<SavedContact> = emptyList(),
    val showAddDialog: Boolean = false,
    val dialogForm: SaveContactFormState = SaveContactFormState(),
    val dialogError: String? = null,
    val snackMessage: String? = null
)

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val savedContactRepository: SavedContactRepository,
    private val saveContactUseCase: SaveContactUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState())
    val uiState: StateFlow<ContactsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            savedContactRepository.observeAll().collect { contacts ->
                _uiState.update { state ->
                    state.copy(
                        allContacts = contacts,
                        filteredContacts = filterContacts(contacts, state.searchQuery)
                    )
                }
            }
        }
    }

    fun onSearchChange(value: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = value,
                filteredContacts = filterContacts(state.allContacts, value)
            )
        }
    }

    private fun filterContacts(contacts: List<SavedContact>, query: String): List<SavedContact> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return contacts
        return contacts.filter {
            it.contactName.lowercase().contains(q) || it.phoneNumber.contains(q)
        }
    }

    fun openAddDialog() {
        _uiState.update {
            it.copy(
                showAddDialog = true,
                dialogForm = SaveContactFormState(),
                dialogError = null
            )
        }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showAddDialog = false, dialogError = null) }
    }

    fun onDialogNameChange(value: String) {
        _uiState.update { it.copy(dialogForm = it.dialogForm.copy(contactName = value)) }
    }

    fun onDialogPhoneChange(value: String) {
        _uiState.update {
            it.copy(dialogForm = it.dialogForm.copy(phoneNumber = PhoneNumberFormatter.filterInput(value)))
        }
    }

    fun onDialogNetworkChange(network: ContactNetwork) {
        _uiState.update { it.copy(dialogForm = it.dialogForm.copy(network = network)) }
    }

    fun onDialogPortedChange(checked: Boolean) {
        _uiState.update { it.copy(dialogForm = it.dialogForm.copy(isPorted = checked)) }
    }

    fun saveFromDialog() {
        val form = _uiState.value.dialogForm
        viewModelScope.launch {
            when (val result = saveContactUseCase(
                contactName = form.contactName,
                phoneNumber = form.phoneNumber,
                network = form.network,
                isPorted = form.isPorted
            )) {
                is SaveContactResult.Success -> {
                    _uiState.update {
                        it.copy(
                            showAddDialog = false,
                            dialogError = null,
                            snackMessage = "Contact saved"
                        )
                    }
                }
                is SaveContactResult.Error -> {
                    _uiState.update { it.copy(dialogError = result.message) }
                }
            }
        }
    }

    fun deleteContact(id: Long) {
        viewModelScope.launch {
            savedContactRepository.deleteContact(id)
            _uiState.update { it.copy(snackMessage = "Contact deleted") }
        }
    }

    fun clearSnackMessage() {
        _uiState.update { it.copy(snackMessage = null) }
    }
}
