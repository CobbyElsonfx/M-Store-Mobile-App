package com.momokotle.agent.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import com.momokotle.agent.data.form.FormUploadKind
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileDocumentHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun validateUri(uri: Uri, kind: FormUploadKind): Result<Unit> = runCatching {
        val mime = context.contentResolver.getType(uri).orEmpty().lowercase(Locale.UK)
        val extension = resolveExtension(uri).lowercase(Locale.UK)
        val allowedExtensions = when (kind) {
            FormUploadKind.SIGNATURE -> setOf("png", "jpg", "jpeg")
            FormUploadKind.DOCUMENT -> setOf("png", "jpg", "jpeg", "pdf")
        }
        val allowedMimes = when (kind) {
            FormUploadKind.SIGNATURE -> setOf("image/png", "image/jpeg", "image/jpg")
            FormUploadKind.DOCUMENT -> setOf("image/png", "image/jpeg", "image/jpg", "application/pdf")
        }
        val extensionOk = extension in allowedExtensions
        val mimeOk = mime.isBlank() || mime in allowedMimes
        if (!extensionOk && !mimeOk) {
            error(
                when (kind) {
                    FormUploadKind.SIGNATURE -> "Signature must be PNG, JPG, or JPEG"
                    FormUploadKind.DOCUMENT -> "Allowed formats: JPG, JPEG, PNG, PDF"
                }
            )
        }
        val size = queryFileSize(uri)
        if (size > FormValidators.MAX_UPLOAD_BYTES) {
            error("File must be 10 MB or smaller")
        }
    }

    private fun queryFileSize(uri: Uri): Long {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst() && sizeIndex >= 0) {
                return cursor.getLong(sizeIndex)
            }
        }
        return 0L
    }

    fun copyToInternalStorage(uri: Uri, slotId: String, kind: FormUploadKind): Result<String> = runCatching {
        validateUri(uri, kind).getOrThrow()
        val dir = File(context.filesDir, "form_uploads").apply { mkdirs() }
        val extension = resolveExtension(uri)
        val dest = File(dir, "${slotId}_${UUID.randomUUID()}.$extension")
        context.contentResolver.openInputStream(uri)?.use { input ->
            dest.outputStream().use { output -> input.copyTo(output) }
        } ?: error("Could not read selected file")
        if (dest.length() > FormValidators.MAX_UPLOAD_BYTES) {
            dest.delete()
            error("File must be 10 MB or smaller")
        }
        dest.absolutePath
    }

    fun fileNameFromPath(path: String): String = File(path).name

    fun isImagePath(path: String): Boolean {
        val ext = File(path).extension.lowercase(Locale.UK)
        return ext in setOf("png", "jpg", "jpeg")
    }

    private fun resolveExtension(uri: Uri): String {
        val mime = context.contentResolver.getType(uri)
        val fromMime = mime?.let { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) }
        if (!fromMime.isNullOrBlank()) return fromMime
        val name = uri.lastPathSegment.orEmpty()
        val dot = name.lastIndexOf('.')
        return if (dot >= 0) name.substring(dot + 1) else "bin"
    }
}
