package com.momokotle.agent.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.momokotle.agent.data.datastore.UserAccountDataStore
import com.momokotle.agent.data.firestore.FirestoreUserDataSource
import com.momokotle.agent.domain.repository.AuthRepository
import com.momokotle.agent.domain.repository.AuthResult
import com.momokotle.agent.models.UserProfile
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestoreUserDataSource: FirestoreUserDataSource,
    private val userAccountDataStore: UserAccountDataStore
) : AuthRepository {

    override fun observeAuthState(): Flow<UserProfile?> =
        firebaseAuthStateFlow()
            .flatMapLatest { firebaseUser ->
                flow {
                    emit(
                        if (firebaseUser == null) {
                            null
                        } else {
                            try {
                                loadProfileForFirebaseUser(firebaseUser)
                            } catch (_: Exception) {
                                null
                            }
                        }
                    )
                }
            }
            .distinctUntilChanged()

    private fun firebaseAuthStateFlow(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        firebaseAuth.addAuthStateListener(listener)
        trySend(firebaseAuth.currentUser)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override fun observeCachedProfile(): Flow<UserProfile?> =
        userAccountDataStore.observeProfile()

    override suspend fun register(
        fullName: String,
        email: String,
        phoneNumber: String,
        location: String,
        password: String
    ): AuthResult {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(
                email.trim(),
                password
            ).await()
            val firebaseUser = authResult.user
                ?: return AuthResult.Error("Registration failed. Please try again.")

            val profile = UserProfile(
                userId = firebaseUser.uid,
                fullName = fullName.trim(),
                email = email.trim(),
                phoneNumber = phoneNumber,
                location = location.trim(),
                accountStatus = UserProfile.ACCOUNT_STATUS_ACTIVE
            )
            firestoreUserDataSource.createUserProfile(profile)
            userAccountDataStore.saveProfile(profile)
            AuthResult.Success(profile)
        } catch (e: Exception) {
            AuthResult.Error(mapFirebaseError(e))
        }
    }

    override suspend fun login(email: String, password: String): AuthResult {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(
                email.trim(),
                password
            ).await()
            val firebaseUser = authResult.user
                ?: return AuthResult.Error("Login failed. Please try again.")

            val profile = loadProfileForFirebaseUser(firebaseUser)
                ?: return AuthResult.Error("User profile not found. Please contact support.")

            if (profile.isSuspended) {
                firebaseAuth.signOut()
                userAccountDataStore.clearProfile()
                return AuthResult.Error(SUSPENDED_MESSAGE)
            }

            userAccountDataStore.saveProfile(profile)
            AuthResult.Success(profile)
        } catch (e: Exception) {
            AuthResult.Error(mapFirebaseError(e))
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
        userAccountDataStore.clearProfile()
    }

    override suspend fun getCurrentUser(): UserProfile? {
        val firebaseUser = firebaseAuth.currentUser ?: return null
        return loadProfileForFirebaseUser(firebaseUser)
    }

    override suspend fun getCurrentUserId(): String? =
        firebaseAuth.currentUser?.uid

    override suspend fun refreshUserProfile(): AuthResult {
        val firebaseUser = firebaseAuth.currentUser
            ?: return AuthResult.Error("Not signed in")
        return try {
            val profile = loadProfileForFirebaseUser(firebaseUser)
                ?: return AuthResult.Error("User profile not found.")
            if (profile.isSuspended) {
                firebaseAuth.signOut()
                userAccountDataStore.clearProfile()
                return AuthResult.Error(SUSPENDED_MESSAGE)
            }
            userAccountDataStore.saveProfile(profile)
            AuthResult.Success(profile)
        } catch (e: Exception) {
            AuthResult.Error(mapFirebaseError(e))
        }
    }

    override suspend fun requireAuthenticatedProfile(): UserProfile {
        val cached = userAccountDataStore.getProfile()
        if (cached != null && firebaseAuth.currentUser?.uid == cached.userId) {
            if (cached.isSuspended) throw IllegalStateException(SUSPENDED_MESSAGE)
            return cached
        }
        val profile = getCurrentUser()
            ?: throw IllegalStateException("Not authenticated")
        if (profile.isSuspended) throw IllegalStateException(SUSPENDED_MESSAGE)
        userAccountDataStore.saveProfile(profile)
        return profile
    }

    private suspend fun loadProfileForFirebaseUser(firebaseUser: FirebaseUser): UserProfile? {
        val profile = firestoreUserDataSource.fetchUserProfile(firebaseUser.uid)
        return profile?.copy(
            email = profile.email.ifBlank { firebaseUser.email.orEmpty() }
        )
    }

    private fun mapFirebaseError(error: Exception): String {
        if (error is FirebaseAuthException) {
            return when (error.errorCode) {
                "ERROR_EMAIL_ALREADY_IN_USE" -> "An account with this email already exists."
                "ERROR_INVALID_EMAIL" -> "Enter a valid email address."
                "ERROR_WEAK_PASSWORD" -> "Password must be at least 6 characters."
                "ERROR_USER_NOT_FOUND",
                "ERROR_INVALID_CREDENTIAL",
                "ERROR_WRONG_PASSWORD" -> "Invalid email or password."
                "ERROR_USER_DISABLED" -> SUSPENDED_MESSAGE
                "ERROR_TOO_MANY_REQUESTS" -> "Too many attempts. Please try again later."
                "ERROR_NETWORK_REQUEST_FAILED" -> "Network error. Check your connection."
                else -> error.message ?: "Authentication failed."
            }
        }
        return error.message ?: "Something went wrong. Please try again."
    }

    companion object {
        const val SUSPENDED_MESSAGE =
            "Your account has been suspended. Please contact support."
    }
}
