package com.momokotle.agent.domain.usecase

import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.domain.repository.AuthResult
import com.momokotle.agent.domain.repository.FormSubmissionRepository
import com.momokotle.agent.domain.repository.OrderRepository
import com.momokotle.agent.domain.repository.ProductRepository
import com.momokotle.agent.models.UserProfile
import com.momokotle.agent.utils.PhoneNumberFormatter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        fullName: String,
        email: String,
        phoneNumber: String,
        location: String,
        password: String,
        confirmPassword: String
    ): AuthResult {
        val name = fullName.trim()
        val mail = email.trim()
        val loc = location.trim()
        val phone = PhoneNumberFormatter.normalizeGhanaNumber(phoneNumber)

        when {
            name.isBlank() -> return AuthResult.Error("Full name is required")
            mail.isBlank() -> return AuthResult.Error("Email is required")
            !android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches() ->
                return AuthResult.Error("Enter a valid email address")
            phone.isBlank() -> return AuthResult.Error("Phone number is required")
            !PhoneNumberFormatter.isValidGhanaNumber(phone) ->
                return AuthResult.Error("Enter a valid Ghana phone number")
            loc.isBlank() -> return AuthResult.Error("Location is required")
            password.length < 6 -> return AuthResult.Error("Password must be at least 6 characters")
            password != confirmPassword -> return AuthResult.Error("Passwords do not match")
        }

        return authRepository.register(
            fullName = name,
            email = mail,
            phoneNumber = phone,
            location = loc,
            password = password
        )
    }
}

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
