package com.momokotle.agent.presentation.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.data.catalog.StoreCatalog
import com.momokotle.agent.domain.repository.StoreRepository
import com.momokotle.agent.domain.repository.UserIdProvider
import com.momokotle.agent.models.StoreCategory
import com.momokotle.agent.models.StoreItem
import com.momokotle.agent.utils.InternetMessages
import com.momokotle.agent.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StoreUiState(
    val isOnline: Boolean = false,
    val selectedCategory: StoreCategory = StoreCategory.SIM_CARDS,
    val items: List<StoreItem> = emptyList(),
    val quantities: Map<String, Int> = emptyMap(),
    val checkoutItem: StoreItem? = null,
    val checkoutPhone: String = "",
    val checkoutLocation: String = "",
    val isSubmitting: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor,
    private val storeRepository: StoreRepository,
    private val userIdProvider: UserIdProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState.asStateFlow()

    init {
        refreshConnectivity()
        selectCategory(StoreCategory.SIM_CARDS)
    }

    fun refreshConnectivity() {
        _uiState.update { it.copy(isOnline = networkMonitor.isOnline()) }
    }

    fun selectCategory(category: StoreCategory) {
        _uiState.update {
            it.copy(
                selectedCategory = category,
                items = StoreCatalog.byCategory(category)
            )
        }
    }

    fun setQuantity(itemId: String, quantity: Int) {
        val q = quantity.coerceIn(1, 99)
        _uiState.update { state ->
            state.copy(quantities = state.quantities + (itemId to q))
        }
    }

    fun quantityFor(itemId: String): Int = _uiState.value.quantities[itemId] ?: 1

    fun openCheckout(item: StoreItem) {
        if (!networkMonitor.isOnline()) {
            _uiState.update { it.copy(errorMessage = InternetMessages.STORE_REQUIRES_INTERNET) }
            return
        }
        _uiState.update {
            it.copy(checkoutItem = item, checkoutPhone = "", checkoutLocation = "", errorMessage = null)
        }
    }

    fun dismissCheckout() {
        _uiState.update { it.copy(checkoutItem = null) }
    }

    fun onCheckoutPhoneChange(v: String) {
        _uiState.update { it.copy(checkoutPhone = v) }
    }

    fun onCheckoutLocationChange(v: String) {
        _uiState.update { it.copy(checkoutLocation = v) }
    }

    fun placeOrder() {
        val item = _uiState.value.checkoutItem ?: return
        if (!networkMonitor.isOnline()) {
            _uiState.update { it.copy(errorMessage = InternetMessages.STORE_REQUIRES_INTERNET) }
            return
        }
        val phone = _uiState.value.checkoutPhone.trim()
        val location = _uiState.value.checkoutLocation.trim()
        if (phone.isBlank() || location.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Enter phone number and location") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            val userId = userIdProvider.getUserId()
            val qty = quantityFor(item.id)
            storeRepository.createAndSubmitOrder(
                userId = userId,
                itemName = item.itemName,
                quantity = qty,
                unitPrice = item.price,
                phoneNumber = phone,
                location = location
            ).onSuccess { order ->
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        checkoutItem = null,
                        message = "Order ${order.status.name.lowercase()}: ${order.itemName}"
                    )
                }
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = e.message ?: "Order failed"
                    )
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(message = null, errorMessage = null) }
    }
}
