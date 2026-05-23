package com.momokotle.agent.presentation.registration;

import android.net.Uri;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.momokotle.agent.data.form.AgentFormDefinitions;
import com.momokotle.agent.data.form.FormSection;
import com.momokotle.agent.data.form.FormUploadSlot;
import com.momokotle.agent.models.AgentFormType;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a \u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a\u001c\u0010\u0006\u001a\u00020\u00012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\bH\u0007\u001az\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000f0\u000e2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000f0\u000e2\u0018\u0010\u0011\u001a\u0014\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u00122\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a6\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u00142\b\u0010\u0018\u001a\u0004\u0018\u00010\u000f2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u00a8\u0006\u001b"}, d2 = {"AgentFormScreen", "", "onBack", "Lkotlin/Function0;", "viewModel", "Lcom/momokotle/agent/presentation/registration/AgentFormViewModel;", "AgentRegistrationScreen", "onFormClick", "Lkotlin/Function1;", "Lcom/momokotle/agent/models/AgentFormType;", "FormSectionContent", "section", "Lcom/momokotle/agent/data/form/FormSection;", "fieldValues", "", "", "uploadFileNames", "onFieldChange", "Lkotlin/Function2;", "onPickUpload", "Lcom/momokotle/agent/data/form/FormUploadSlot;", "onRemoveUpload", "UploadSlot", "slot", "fileName", "onPick", "onRemove", "app_debug"})
public final class RegistrationScreensKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void AgentRegistrationScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.momokotle.agent.models.AgentFormType, kotlin.Unit> onFormClick) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void AgentFormScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.registration.AgentFormViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FormSectionContent(com.momokotle.agent.data.form.FormSection section, java.util.Map<java.lang.String, java.lang.String> fieldValues, java.util.Map<java.lang.String, java.lang.String> uploadFileNames, kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onFieldChange, kotlin.jvm.functions.Function1<? super com.momokotle.agent.data.form.FormUploadSlot, kotlin.Unit> onPickUpload, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onRemoveUpload) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void UploadSlot(com.momokotle.agent.data.form.FormUploadSlot slot, java.lang.String fileName, kotlin.jvm.functions.Function0<kotlin.Unit> onPick, kotlin.jvm.functions.Function0<kotlin.Unit> onRemove) {
    }
}