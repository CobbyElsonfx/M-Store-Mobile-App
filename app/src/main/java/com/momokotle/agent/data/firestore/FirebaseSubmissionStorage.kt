package com.momokotle.agent.data.firestore

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.momokotle.agent.data.form.DocumentSlotRegistry
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class FirebaseSubmissionStorage @Inject constructor(
    private val storage: FirebaseStorage
) {
    suspend fun uploadDocument(
        userId: String,
        submissionId: String,
        slotId: String,
        localPath: String,
        onProgress: (Int) -> Unit = {}
    ): String {
        val file = File(localPath)
        if (!file.exists()) {
            throw IllegalArgumentException(
                "Uploaded file is missing on device ($slotId). Please select the file again."
            )
        }
        if (file.length() == 0L) {
            throw IllegalArgumentException(
                "Uploaded file is empty ($slotId). Please select the file again."
            )
        }

        val fileName = DocumentSlotRegistry.storageFileName(slotId, localPath)
        val storagePath = "agent_uploads/$userId/$submissionId/$fileName"
        val ref = storage.reference.child(storagePath)

        return try {
            val snapshot = uploadWithProgress(ref, Uri.fromFile(file), onProgress)
            snapshot.storage.downloadUrl.await().toString()
        } catch (e: StorageException) {
            throw mapStorageException(e, storagePath)
        } catch (e: Exception) {
            val message = e.message.orEmpty()
            if (message.contains("object does not exist", ignoreCase = true)) {
                throw IllegalStateException(
                    "Firebase Storage is not available. Enable Storage in Firebase Console " +
                        "and deploy storage.rules.example, then try again.",
                    e
                )
            }
            throw e
        }
    }

    suspend fun deleteByUrl(downloadUrl: String) {
        runCatching {
            storage.getReferenceFromUrl(downloadUrl).delete().await()
        }
    }

    suspend fun deleteDocument(userId: String, submissionId: String, slotId: String, localPath: String) {
        runCatching {
            val fileName = DocumentSlotRegistry.storageFileName(slotId, localPath)
            val storagePath = "agent_uploads/$userId/$submissionId/$fileName"
            storage.reference.child(storagePath).delete().await()
        }
    }

    private suspend fun uploadWithProgress(
        ref: com.google.firebase.storage.StorageReference,
        uri: Uri,
        onProgress: (Int) -> Unit
    ) = suspendCancellableCoroutine { cont ->
        val task = ref.putFile(uri)
        task.addOnProgressListener { snapshot ->
            val total = snapshot.totalByteCount
            if (total > 0L) {
                val percent = ((snapshot.bytesTransferred * 100) / total).toInt()
                onProgress(percent.coerceIn(0, 100))
            }
        }
        task.addOnSuccessListener { cont.resume(it) }
        task.addOnFailureListener { cont.resumeWithException(it) }
        cont.invokeOnCancellation { task.cancel() }
    }

    private fun mapStorageException(e: StorageException, path: String): Exception = when (e.errorCode) {
        StorageException.ERROR_OBJECT_NOT_FOUND -> IllegalStateException(
            "Storage object not found at $path. Enable Firebase Storage and deploy storage.rules.example.",
            e
        )
        StorageException.ERROR_BUCKET_NOT_FOUND -> IllegalStateException(
            "Firebase Storage bucket is not configured.",
            e
        )
        StorageException.ERROR_NOT_AUTHORIZED -> IllegalStateException(
            "Not allowed to upload documents. Deploy storage.rules.example for agent_uploads/.",
            e
        )
        else -> IllegalStateException(e.message ?: "Document upload failed", e)
    }
}
