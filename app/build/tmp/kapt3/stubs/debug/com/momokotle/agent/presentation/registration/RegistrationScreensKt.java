package com.momokotle.agent.presentation.registration;

import android.net.Uri;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.KeyboardType;
import com.momokotle.agent.data.form.AgentFormDefinitions;
import com.momokotle.agent.data.form.FormFieldKind;
import com.momokotle.agent.data.form.FormSection;
import com.momokotle.agent.data.form.FormTextField;
import com.momokotle.agent.data.form.FormUploadKind;
import com.momokotle.agent.data.form.FormUploadSlot;
import com.momokotle.agent.models.AgentFormType;
import com.momokotle.agent.ui.components.AppButtonStyle;
import com.momokotle.agent.ui.theme.AppDimens;
import com.momokotle.agent.utils.FormValidators;
import androidx.compose.foundation.text.KeyboardOptions;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0007\n\u0002\u0010\b\n\u0000\n\u0002\u0010\"\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a \u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a\u001c\u0010\u0006\u001a\u00020\u00012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\bH\u0007\u001av\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u001a\u008e\u0003\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\u00192\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e0\u001b2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e0\u001b2\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e0\u001b2\u0012\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e0\u001b2\u0012\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00110\u001b2\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e0\u001b2\u0012\u0010!\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00110\u001b2\u0012\u0010\"\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020#0\u001b2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u000e0%2\u0012\u0010&\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e0\u001b2\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u000e0%2\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u000e0%2\u0006\u0010)\u001a\u00020\u00112\u0018\u0010*\u001a\u0014\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010+2\u0018\u0010\u0014\u001a\u0014\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010+2\u0012\u0010,\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010-\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010.\u001a\u000e\u0012\u0004\u0012\u00020/\u0012\u0004\u0012\u00020\u00010\b2\u0012\u00100\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\b2\u0012\u00101\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a\u007f\u00102\u001a\u00020\u00012\u0006\u00103\u001a\u00020/2\b\u00104\u001a\u0004\u0018\u00010\u000e2\b\u00105\u001a\u0004\u0018\u00010\u000e2\b\u0010\"\u001a\u0004\u0018\u00010#2\u0006\u00106\u001a\u00020\u00112\b\u0010\u000f\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\f\u00107\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u00108\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u00a2\u0006\u0002\u00109\u001a\u0010\u0010:\u001a\u00020\u000e2\u0006\u0010;\u001a\u00020<H\u0002\u001a\u0018\u0010=\u001a\u00020\u00112\u0006\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020\u000eH\u0002\u001a\u0018\u0010A\u001a\u00020\u00112\u0006\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020\u000eH\u0002\u00a8\u0006B"}, d2 = {"AgentFormScreen", "", "onBack", "Lkotlin/Function0;", "viewModel", "Lcom/momokotle/agent/presentation/registration/AgentFormViewModel;", "AgentRegistrationScreen", "onFormClick", "Lkotlin/Function1;", "Lcom/momokotle/agent/models/AgentFormType;", "FormFieldInput", "field", "Lcom/momokotle/agent/data/form/FormTextField;", "value", "", "errorText", "isValid", "", "showValidation", "onValueChange", "onDateSelected", "onBlur", "onTouched", "FormSectionContent", "section", "Lcom/momokotle/agent/data/form/FormSection;", "fieldValues", "", "uploadFileNames", "uploads", "fieldErrors", "fieldValid", "uploadErrors", "uploadValid", "uploadProgress", "", "uploadingSlots", "", "previewPaths", "touchedFields", "touchedUploads", "validationAttempted", "onFieldChange", "Lkotlin/Function2;", "onFieldBlur", "onFieldTouched", "onPickUpload", "Lcom/momokotle/agent/data/form/FormUploadSlot;", "onRemoveUpload", "onUploadTouched", "UploadSlot", "slot", "fileName", "localPath", "isUploading", "onPick", "onRemove", "(Lcom/momokotle/agent/data/form/FormUploadSlot;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;ZLjava/lang/String;ZZLkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;)V", "mimeTypeFor", "kind", "Lcom/momokotle/agent/data/form/FormUploadKind;", "shouldShowFieldValidation", "uiState", "Lcom/momokotle/agent/presentation/registration/AgentFormUiState;", "key", "shouldShowUploadValidation", "app_debug"})
public final class RegistrationScreensKt {
    
    @androidx.compose.runtime.Composable()
    public static final void AgentRegistrationScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.momokotle.agent.models.AgentFormType, kotlin.Unit> onFormClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AgentFormScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.registration.AgentFormViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FormFieldInput(com.momokotle.agent.data.form.FormTextField field, java.lang.String value, java.lang.String errorText, boolean isValid, boolean showValidation, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValueChange, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDateSelected, kotlin.jvm.functions.Function0<kotlin.Unit> onBlur, kotlin.jvm.functions.Function0<kotlin.Unit> onTouched) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FormSectionContent(com.momokotle.agent.data.form.FormSection section, java.util.Map<java.lang.String, java.lang.String> fieldValues, java.util.Map<java.lang.String, java.lang.String> uploadFileNames, java.util.Map<java.lang.String, java.lang.String> uploads, java.util.Map<java.lang.String, java.lang.String> fieldErrors, java.util.Map<java.lang.String, java.lang.Boolean> fieldValid, java.util.Map<java.lang.String, java.lang.String> uploadErrors, java.util.Map<java.lang.String, java.lang.Boolean> uploadValid, java.util.Map<java.lang.String, java.lang.Integer> uploadProgress, java.util.Set<java.lang.String> uploadingSlots, java.util.Map<java.lang.String, java.lang.String> previewPaths, java.util.Set<java.lang.String> touchedFields, java.util.Set<java.lang.String> touchedUploads, boolean validationAttempted, kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onFieldChange, kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onDateSelected, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onFieldBlur, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onFieldTouched, kotlin.jvm.functions.Function1<? super com.momokotle.agent.data.form.FormUploadSlot, kotlin.Unit> onPickUpload, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onRemoveUpload, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onUploadTouched) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void UploadSlot(com.momokotle.agent.data.form.FormUploadSlot slot, java.lang.String fileName, java.lang.String localPath, java.lang.Integer uploadProgress, boolean isUploading, java.lang.String errorText, boolean isValid, boolean showValidation, kotlin.jvm.functions.Function0<kotlin.Unit> onPick, kotlin.jvm.functions.Function0<kotlin.Unit> onRemove, kotlin.jvm.functions.Function0<kotlin.Unit> onTouched) {
    }
    
    private static final boolean shouldShowFieldValidation(com.momokotle.agent.presentation.registration.AgentFormUiState uiState, java.lang.String key) {
        return false;
    }
    
    private static final boolean shouldShowUploadValidation(com.momokotle.agent.presentation.registration.AgentFormUiState uiState, java.lang.String key) {
        return false;
    }
    
    private static final java.lang.String mimeTypeFor(com.momokotle.agent.data.form.FormUploadKind kind) {
        return null;
    }
}