package com.momokotle.agent.presentation.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.domain.repository.OrderRepository
import com.momokotle.agent.models.StoreOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    authRepository: AuthRepository,
    orderRepository: OrderRepository
) : ViewModel() {

    val orders: StateFlow<List<StoreOrder>> = authRepository.observeCachedProfile()
        .flatMapLatest { profile ->
            val userId = profile?.userId
            if (userId.isNullOrBlank()) {
                flowOf(emptyList())
            } else {
                orderRepository.observeMyOrders(userId)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _order = MutableStateFlow<StoreOrder?>(null)
    val order: StateFlow<StoreOrder?> = _order

    fun loadOrder(orderId: String) {
        viewModelScope.launch {
            _order.value = orderRepository.getOrder(orderId)
        }
    }
}
