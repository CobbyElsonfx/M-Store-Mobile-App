package com.momokotle.agent.domain.usecase

import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.domain.repository.AuthResult
import com.momokotle.agent.domain.repository.FormSubmissionRepository
import com.momokotle.agent.domain.repository.OrderRepository
import com.momokotle.agent.domain.repository.ProductRepository
import com.momokotle.agent.models.UserProfile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        val mail = email.trim()
        when {
            mail.isBlank() -> return AuthResult.Error("Email is required")
            password.isBlank() -> return AuthResult.Error("Password is required")
        }
        return authRepository.login(mail, password)
    }
}

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val formSubmissionRepository: FormSubmissionRepository
) {
    suspend operator fun invoke() {
        productRepository.stopSync()
        orderRepository.stopSync()
        formSubmissionRepository.stopSync()
        authRepository.logout()
    }
}

class ObserveAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<UserProfile?> = authRepository.observeAuthState()
}

class RefreshUserProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): AuthResult = authRepository.refreshUserProfile()
}

class GetAuthenticatedUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): UserProfile = authRepository.requireAuthenticatedProfile()
}
