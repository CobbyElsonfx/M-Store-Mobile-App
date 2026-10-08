package com.momokotle.agent.domain.repository

import com.momokotle.agent.models.UserProfile
import kotlinx.coroutines.flow.Flow

sealed class AuthResult {
    data class Success(val profile: UserProfile) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

interface AuthRepository {
    fun observeAuthState(): Flow<UserProfile?>
    fun observeCachedProfile(): Flow<UserProfile?>
    suspend fun register(
        fullName: String,
        email: String,
        phoneNumber: String,
        location: String,
        password: String
    ): AuthResult

    suspend fun login(email: String, password: String): AuthResult
    suspend fun logout()
    suspend fun getCurrentUser(): UserProfile?
    suspend fun getCurrentUserId(): String?
    suspend fun refreshUserProfile(): AuthResult
    suspend fun requireAuthenticatedProfile(): UserProfile
}
