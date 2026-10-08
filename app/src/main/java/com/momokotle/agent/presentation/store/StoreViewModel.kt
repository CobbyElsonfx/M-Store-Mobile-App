package com.momokotle.agent.presentation.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momokotle.agent.domain.repository.ProductRepository
import com.momokotle.agent.domain.repository.ProductValidationResult
import com.momokotle.agent.domain.repository.StoreRepository
import com.momokotle.agent.domain.usecase.GetAuthenticatedUserUseCase
import com.momokotle.agent.models.StoreCategory
import com.momokotle.agent.models.StoreProduct
import com.momokotle.agent.utils.InternetMessages
import com.momokotle.agent.utils.NetworkMonitor
import com.momokotle.agent.utils.PhoneNumberFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StoreUiState(
    val isOnline: Boolean = false,
    val isLoadingProducts: Boolean = true,
    val selectedCategory: StoreCategory = StoreCategory.SIM_CARDS,
    val products: List<StoreProduct> = emptyList(),
    val quantities: Map<String, Int> = emptyMap(),
    val checkoutProduct: StoreProduct? = null,
    val checkoutPhone: String = "",
    val checkoutLocation: String = "",
    val isSubmitting: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor,
    private val productRepository: ProductRepository,
    private val storeRepository: StoreRepository,
    private val getAuthenticatedUserUseCase: GetAuthenticatedUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState.asStateFlow()

    private var productsJob: Job? = null

    init {
        refreshConnectivity()
        observeProductsForCategory(StoreCategory.SIM_CARDS)
    }

    override fun onCleared() {
        super.onCleared()
        productsJob?.cancel()
    }

    fun refreshConnectivity() {
        _uiState.update { it.copy(isOnline = networkMonitor.isOnline()) }
    }

    fun selectCategory(category: StoreCategory) {
        if (_uiState.value.selectedCategory == category) return
        _uiState.update { it.copy(selectedCategory = category, isLoadingProducts = true) }
        observeProductsForCategory(category)
    }

    private fun observeProductsForCategory(category: StoreCategory) {
        productsJob?.cancel()
        productsJob = viewModelScope.launch {
            productRepository.observeProductsByCategory(category).collect { products ->
                _uiState.update {
                    it.copy(
                        products = products,
                        isLoadingProducts = false
                    )
                }
            }
        }
    }

    fun setQuantity(productId: String, quantity: Int) {
        val product = _uiState.value.products.find { it.productId == productId } ?: return
        val maxQty = product.stockQuantity.coerceAtLeast(1).coerceAtMost(99)
        val q = quantity.coerceIn(1, maxQty)
        _uiState.update { state ->
            state.copy(quantities = state.quantities + (productId to q))
        }
    }

    fun quantityFor(productId: String): Int = _uiState.value.quantities[productId] ?: 1

    fun openCheckout(product: StoreProduct) {
        if (!networkMonitor.isOnline()) {
            _uiState.update { it.copy(errorMessage = InternetMessages.STORE_REQUIRES_INTERNET) }
            return
        }

        viewModelScope.launch {
            when (
                val validation = productRepository.validateForPurchase(
                    product.productId,
                    quantityFor(product.productId)
                )
            ) {
                is ProductValidationResult.Invalid -> {
                    _uiState.update { it.copy(errorMessage = validation.message) }
                    return@launch
                }
                is ProductValidationResult.Valid -> {
                    val profile = runCatching { getAuthenticatedUserUseCase() }.getOrNull()
                    _uiState.update {
                        it.copy(
                            checkoutProduct = product,
                            checkoutPhone = profile?.phoneNumber.orEmpty(),
                            checkoutLocation = profile?.location.orEmpty(),
                            errorMessage = null
                        )
                    }
                }
            }
        }
    }

    fun dismissCheckout() {
        _uiState.update { it.copy(checkoutProduct = null) }
    }

    fun onCheckoutPhoneChange(v: String) {
        _uiState.update { it.copy(checkoutPhone = v) }
    }

    fun onCheckoutLocationChange(v: String) {
        _uiState.update { it.copy(checkoutLocation = v) }
    }

    fun placeOrder() {
        val product = _uiState.value.checkoutProduct ?: return
        if (!networkMonitor.isOnline()) {
            _uiState.update { it.copy(errorMessage = InternetMessages.STORE_REQUIRES_INTERNET) }
            return
        }

        viewModelScope.launch {
            val qty = quantityFor(product.productId)
            when (val validation = productRepository.validateForPurchase(product.productId, qty)) {
                is ProductValidationResult.Invalid -> {
                    _uiState.update {
                        it.copy(errorMessage = validation.message, checkoutProduct = null)
                    }
                    return@launch
                }
                is ProductValidationResult.Valid -> {
                    val validated = validation.product
                    val profile = try {
                        getAuthenticatedUserUseCase()
                    } catch (_: Exception) {
                        _uiState.update { it.copy(errorMessage = "Please sign in to place an order") }
                        return@launch
                    }
                    val phone = PhoneNumberFormatter.normalizeGhanaNumber(
                        _uiState.value.checkoutPhone.trim().ifBlank { profile.phoneNumber }
                    )
                    val location = _uiState.value.checkoutLocation.trim().ifBlank { profile.location }
                    if (!PhoneNumberFormatter.isValidGhanaNumber(phone)) {
                        _uiState.update { it.copy(errorMessage = "Enter a valid phone number") }
                        return@launch
                    }
                    if (location.isBlank()) {
                        _uiState.update { it.copy(errorMessage = "Enter delivery location") }
                        return@launch
                    }
                    _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
                    storeRepository.createAndSubmitOrder(
                        userId = profile.userId,
                        agentName = profile.fullName,
                        itemName = validated.productName,
                        quantity = qty,
                        unitPrice = validated.price,
                        phoneNumber = phone,
                        location = location
                    ).onSuccess { order ->
                        _uiState.update {
                            it.copy(
                                isSubmitting = false,
                                checkoutProduct = null,
                                message = "Order ${order.status.firestoreValue}: ${order.itemName}"
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
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(message = null, errorMessage = null) }
    }
}
