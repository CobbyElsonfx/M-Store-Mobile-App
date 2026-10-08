package com.momokotle.agent.utils;

import android.content.Context;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;
import com.momokotle.agent.data.form.FormUploadKind;
import dagger.hilt.android.qualifiers.ApplicationContext;
import java.io.File;
import java.util.Locale;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J1\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\f\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\r\u0010\u000eJ\u000e\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u0007J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0010\u001a\u00020\u0007J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\b\u001a\u00020\tH\u0002J\u0010\u0010\u0015\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0002J)\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0018\u0010\u0019R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u001a"}, d2 = {"Lcom/momokotle/agent/utils/FileDocumentHelper;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "copyToInternalStorage", "Lkotlin/Result;", "", "uri", "Landroid/net/Uri;", "slotId", "kind", "Lcom/momokotle/agent/data/form/FormUploadKind;", "copyToInternalStorage-0E7RQCE", "(Landroid/net/Uri;Ljava/lang/String;Lcom/momokotle/agent/data/form/FormUploadKind;)Ljava/lang/Object;", "fileNameFromPath", "path", "isImagePath", "", "queryFileSize", "", "resolveExtension", "validateUri", "", "validateUri-gIAlu-s", "(Landroid/net/Uri;Lcom/momokotle/agent/data/form/FormUploadKind;)Ljava/lang/Object;", "app_debug"})
public final class FileDocumentHelper {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    
    @javax.inject.Inject()
    public FileDocumentHelper(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final long queryFileSize(android.net.Uri uri) {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fileNameFromPath(@org.jetbrains.annotations.NotNull()
    java.lang.String path) {
        return null;
    }
    
    public final boolean isImagePath(@org.jetbrains.annotations.NotNull()
    java.lang.String path) {
        return false;
    }
    
    private final java.lang.String resolveExtension(android.net.Uri uri) {
        return null;
    }
}