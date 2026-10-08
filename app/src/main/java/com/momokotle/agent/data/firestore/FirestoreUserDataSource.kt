package com.momokotle.agent.data.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.momokotle.agent.models.UserProfile
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreUserDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private fun usersCollection() = firestore.collection(COLLECTION_USERS)

    suspend fun createUserProfile(profile: UserProfile) {
        val now = Timestamp.now()
        val data = mapOf(
            FIELD_USER_ID to profile.userId,
            FIELD_FULL_NAME to profile.fullName,
            FIELD_EMAIL to profile.email,
            FIELD_PHONE_NUMBER to profile.phoneNumber,
            FIELD_LOCATION to profile.location,
            FIELD_ACCOUNT_STATUS to profile.accountStatus,
            FIELD_CREATED_AT to now,
            FIELD_UPDATED_AT to now
        )
        usersCollection().document(profile.userId).set(data).await()
    }

    suspend fun fetchUserProfile(userId: String): UserProfile? {
        val snapshot = usersCollection().document(userId).get().await()
        if (!snapshot.exists()) return null
        return snapshot.toUserProfile(userId)
    }

    companion object {
        const val COLLECTION_USERS = "users"

        const val FIELD_USER_ID = "userId"
        const val FIELD_FULL_NAME = "fullName"
        const val FIELD_EMAIL = "email"
        const val FIELD_PHONE_NUMBER = "phoneNumber"
        const val FIELD_LOCATION = "location"
        const val FIELD_ACCOUNT_STATUS = "accountStatus"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"
    }
}

private fun com.google.firebase.firestore.DocumentSnapshot.toUserProfile(fallbackUserId: String): UserProfile {
    return UserProfile(
        userId = getString(FirestoreUserDataSource.FIELD_USER_ID) ?: fallbackUserId,
        fullName = getString(FirestoreUserDataSource.FIELD_FULL_NAME).orEmpty(),
        email = getString(FirestoreUserDataSource.FIELD_EMAIL).orEmpty(),
        phoneNumber = getString(FirestoreUserDataSource.FIELD_PHONE_NUMBER).orEmpty(),
        location = getString(FirestoreUserDataSource.FIELD_LOCATION).orEmpty(),
        accountStatus = getString(FirestoreUserDataSource.FIELD_ACCOUNT_STATUS)
            ?: UserProfile.ACCOUNT_STATUS_ACTIVE
    )
}
