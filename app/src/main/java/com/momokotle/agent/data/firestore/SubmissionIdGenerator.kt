package com.momokotle.agent.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubmissionIdGenerator @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun generateNextId(): String {
        val datePart = LocalDate.now().format(DATE_FORMAT)
        val counterDoc = firestore.collection(COUNTERS_COLLECTION).document("submission_$datePart")
        return firestore.runTransaction { transaction ->
            val snapshot = transaction.get(counterDoc)
            val next = (snapshot.getLong(FIELD_SEQ) ?: 0L) + 1L
            transaction.set(counterDoc, mapOf(FIELD_SEQ to next), SetOptions.merge())
            "SUB$datePart${String.format(Locale.UK, "%04d", next)}"
        }.await()
    }

    companion object {
        private const val COUNTERS_COLLECTION = "counters"
        private const val FIELD_SEQ = "seq"
        private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd")
    }
}
