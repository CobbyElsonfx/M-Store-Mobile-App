package com.momokotle.agent.utils

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileDocumentHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun copyToInternalStorage(uri: Uri, slotId: String): Result<String> = runCatching {
        val dir = File(context.filesDir, "form_uploads").apply { mkdirs() }
        val extension = resolveExtension(uri)
        val dest = File(dir, "${slotId}_${UUID.randomUUID()}.$extension")
        context.contentResolver.openInputStream(uri)?.use { input ->
            dest.outputStream().use { output -> input.copyTo(output) }
        } ?: error("Could not read selected file")
        dest.absolutePath
    }

    fun fileNameFromPath(path: String): String = File(path).name

    private fun resolveExtension(uri: Uri): String {
        val mime = context.contentResolver.getType(uri)
        val fromMime = mime?.let { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) }
        if (!fromMime.isNullOrBlank()) return fromMime
        val name = uri.lastPathSegment.orEmpty()
        val dot = name.lastIndexOf('.')
        return if (dot >= 0) name.substring(dot + 1) else "bin"
    }
}
