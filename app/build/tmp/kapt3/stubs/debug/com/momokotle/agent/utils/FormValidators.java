package com.momokotle.agent.utils;

import com.momokotle.agent.data.form.FormFieldKind;
import com.momokotle.agent.data.form.FormTextField;
import com.momokotle.agent.data.form.FormUploadKind;
import com.momokotle.agent.data.form.FormUploadSlot;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u000f\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\bJ\u000e\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u0015J\u0016\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\bJ\u0018\u0010\u0019\u001a\u00020\u00172\u0006\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\bJ \u0010\u001d\u001a\u00020\u00172\u0006\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001e\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001f\u001a\u00020\u0017J\u0015\u0010 \u001a\u0004\u0018\u00010!2\u0006\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\u0002\u0010\"J\u0016\u0010#\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u001e\u001a\u00020\bJ\u000e\u0010$\u001a\u00020\b2\u0006\u0010\u001e\u001a\u00020\bJ\u0010\u0010%\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u001e\u001a\u00020\bJ\u000e\u0010&\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u0011J\u0010\u0010\'\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001e\u001a\u00020\bJ\u0018\u0010(\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\bJ\u0010\u0010)\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001e\u001a\u00020\bJ\u0010\u0010*\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001e\u001a\u00020\bJ\u0018\u0010+\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001e\u001a\u00020\b2\u0006\u0010,\u001a\u00020\bJ\u0018\u0010-\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001e\u001a\u00020\b2\u0006\u0010,\u001a\u00020\bJ\u001a\u0010.\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\bJ\"\u0010/\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001e\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001f\u001a\u00020\u0017R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0086T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2 = {"Lcom/momokotle/agent/utils/FormValidators;", "", "()V", "DATE_FORMATTER", "Ljava/time/format/DateTimeFormatter;", "kotlin.jvm.PlatformType", "DOCUMENT_EXTENSIONS", "", "", "GHANA_CARD_PATTERN", "Lkotlin/text/Regex;", "GHANA_PHONE_PREFIXES", "MAX_UPLOAD_BYTES", "", "SIGNATURE_EXTENSIONS", "filterInput", "field", "Lcom/momokotle/agent/data/form/FormTextField;", "raw", "formatDate", "date", "Ljava/time/LocalDate;", "isFieldValid", "", "rawValue", "isUploadValid", "slot", "Lcom/momokotle/agent/data/form/FormUploadSlot;", "localPath", "isUploadedDocumentValid", "value", "isUploading", "maxLengthFor", "", "(Lcom/momokotle/agent/data/form/FormTextField;)Ljava/lang/Integer;", "normalizeField", "normalizeName", "parseDate", "placeholderFor", "validateDateOfBirth", "validateField", "validateGhanaCard", "validateGhanaPhone", "validateName", "label", "validatePlace", "validateUpload", "validateUploadedDocument", "app_debug"})
public final class FormValidators {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Set<java.lang.String> GHANA_PHONE_PREFIXES = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex GHANA_CARD_PATTERN = null;
    private static final java.time.format.DateTimeFormatter DATE_FORMATTER = null;
    public static final long MAX_UPLOAD_BYTES = 10485760L;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Set<java.lang.String> SIGNATURE_EXTENSIONS = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Set<java.lang.String> DOCUMENT_EXTENSIONS = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.utils.FormValidators INSTANCE = null;
    
    private FormValidators() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String filterInput(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.form.FormTextField field, @org.jetbrains.annotations.NotNull()
    java.lang.String raw) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String normalizeField(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.form.FormTextField field, @org.jetbrains.annotations.NotNull()
    java.lang.String value) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String validateField(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.form.FormTextField field, @org.jetbrains.annotations.NotNull()
    java.lang.String rawValue) {
        return null;
    }
    
    public final boolean isFieldValid(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.form.FormTextField field, @org.jetbrains.annotations.NotNull()
    java.lang.String rawValue) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String validateUpload(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.form.FormUploadSlot slot, @org.jetbrains.annotations.Nullable()
    java.lang.String localPath) {
        return null;
    }
    
    public final boolean isUploadValid(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.form.FormUploadSlot slot, @org.jetbrains.annotations.Nullable()
    java.lang.String localPath) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String validateUploadedDocument(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.form.FormUploadSlot slot, @org.jetbrains.annotations.Nullable()
    java.lang.String value, boolean isUploading) {
        return null;
    }
    
    public final boolean isUploadedDocumentValid(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.form.FormUploadSlot slot, @org.jetbrains.annotations.Nullable()
    java.lang.String value, boolean isUploading) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String validateGhanaPhone(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String validateGhanaCard(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String validateDateOfBirth(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String validateName(@org.jetbrains.annotations.NotNull()
    java.lang.String value, @org.jetbrains.annotations.NotNull()
    java.lang.String label) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String validatePlace(@org.jetbrains.annotations.NotNull()
    java.lang.String value, @org.jetbrains.annotations.NotNull()
    java.lang.String label) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatDate(@org.jetbrains.annotations.NotNull()
    java.time.LocalDate date) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.time.LocalDate parseDate(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String normalizeName(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String placeholderFor(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.form.FormTextField field) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer maxLengthFor(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.form.FormTextField field) {
        return null;
    }
}