package com.momokotle.agent.repository

import com.momokotle.agent.data.datastore.AuthTokenDataStore
import com.momokotle.agent.data.datastore.UserAccountDataStore
import com.momokotle.agent.data.remote.ResendRegistrationOtpRequest
import com.momokotle.agent.data.remote.VerifyRegistrationOtpRequest
import com.momokotle.agent.data.remote.AuthLoginRequest
import com.momokotle.agent.data.remote.AuthRegisterRequest
import com.momokotle.agent.data.remote.LaravelApiMapper
import com.momokotle.agent.data.remote.MStorepayApiService
import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.domain.repository.AuthResult
import com.momokotle.agent.models.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImpl @Inject constructor(
    private val api: MStorepayApiService,
    private val authTokenDataStore: AuthTokenDataStore,
    private val userAccountDataStore: UserAccountDataStore
) : AuthRepository {

    override fun observeAuthState(): Flow<UserProfile?> =
        authTokenDataStore.observeToken()
            .flatMapLatest { token ->
                if (token.isNullOrBlank()) {
                    flowOf(null)
                } else {
                    userAccountDataStore.observeProfile()
                }
            }
            .distinctUntilChanged()

    override fun observeCachedProfile(): Flow<UserProfile?> =
        userAccountDataStore.observeProfile()

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        location: String,
        password: String,
        referralCode: String?
    ): AuthResult {
        return try {
            val response = api.register(
                AuthRegisterRequest(
                    firstName = firstName.trim(),
                    lastName = lastName.trim(),
                    email = email.trim(),
                    phoneNumber = phoneNumber,
                    location = location.trim(),
                    password = password,
                    passwordConfirmation = password,
                    referralCode = referralCode?.trim()?.takeIf { it.isNotBlank() }
                )
            )
            if (response.requiresVerification) {
                AuthResult.PendingVerification(
                    email = response.email ?: email.trim(),
                    message = response.message ?: "Verification code sent to your email."
                )
            } else {
                AuthResult.Error("Registration failed. Please try again.")
            }
        } catch (e: Exception) {
            AuthResult.Error(LaravelApiMapper.parseApiError(e))
        }
    }

    override suspend fun verifyRegistrationOtp(email: String, otpCode: String): AuthResult {
        return try {
            val response = api.verifyRegistration(
                VerifyRegistrationOtpRequest(
                    email = email.trim(),
                    otpCode = otpCode.trim()
                )
            )
            handleAuthResponse(response.token, response.user)
        } catch (e: Exception) {
            AuthResult.Error(LaravelApiMapper.parseApiError(e))
        }
    }

    override suspend fun resendRegistrationOtp(email: String): AuthResult {
        return try {
            val response = api.resendRegistrationOtp(
                ResendRegistrationOtpRequest(email = email.trim())
            )
            AuthResult.PendingVerification(
                email = email.trim(),
                message = response.message ?: "Verification code sent to your email."
            )
        } catch (e: Exception) {
            AuthResult.Error(LaravelApiMapper.parseApiError(e))
        }
    }

    override suspend fun login(email: String, password: String): AuthResult {
        return try {
            val response = api.login(
                AuthLoginRequest(email = email.trim(), password = password)
            )
            handleAuthResponse(response.token, response.user)
        } catch (e: Exception) {
            AuthResult.Error(LaravelApiMapper.parseApiError(e))
        }
    }

    override suspend fun logout() {
        runCatching { api.logout() }
        authTokenDataStore.clearToken()
        userAccountDataStore.clearProfile()
    }

    override suspend fun getCurrentUser(): UserProfile? {
        if (authTokenDataStore.getToken().isNullOrBlank()) return null
        return userAccountDataStore.getProfile()
    }

    override suspend fun getCurrentUserId(): String? {
        val token = authTokenDataStore.getToken()
        if (token.isNullOrBlank()) return null
        return userAccountDataStore.getProfile()?.userId
    }

    override suspend fun refreshUserProfile(): AuthResult {
        if (authTokenDataStore.getToken().isNullOrBlank()) {
            return AuthResult.Error("Not signed in")
        }
        return try {
            val response = api.me()
            val profile = LaravelApiMapper.mapUser(response.user)
            if (profile.isSuspended) {
                clearSession()
                return AuthResult.Error(SUSPENDED_MESSAGE)
            }
            userAccountDataStore.saveProfile(profile)
            AuthResult.Success(profile)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                clearSession()
            }
            AuthResult.Error(LaravelApiMapper.parseApiError(e))
        } catch (e: Exception) {
            AuthResult.Error(LaravelApiMapper.parseApiError(e))
        }
    }

    override suspend fun requireAuthenticatedProfile(): UserProfile {
        val cached = userAccountDataStore.getProfile()
        if (cached != null && !authTokenDataStore.getToken().isNullOrBlank()) {
            if (cached.isSuspended) throw IllegalStateException(SUSPENDED_MESSAGE)
            return cached
        }
        val profile = getCurrentUser()
            ?: throw IllegalStateException("Not authenticated")
        if (profile.isSuspended) throw IllegalStateException(SUSPENDED_MESSAGE)
        return profile
    }

    private suspend fun handleAuthResponse(
        token: String?,
        userDto: com.momokotle.agent.data.remote.ApiUserDto?
    ): AuthResult {
        if (token.isNullOrBlank() || userDto == null) {
            return AuthResult.Error("Authentication failed. Please try again.")
        }
        val profile = LaravelApiMapper.mapUser(userDto)
        if (profile.isSuspended) {
            return AuthResult.Error(SUSPENDED_MESSAGE)
        }
        authTokenDataStore.saveToken(token)
        userAccountDataStore.saveProfile(profile)
        return AuthResult.Success(profile)
    }

    private suspend fun clearSession() {
        authTokenDataStore.clearToken()
        userAccountDataStore.clearProfile()
    }

    companion object {
        const val SUSPENDED_MESSAGE =
            "Your account has been suspended. Please contact support."
    }
}
