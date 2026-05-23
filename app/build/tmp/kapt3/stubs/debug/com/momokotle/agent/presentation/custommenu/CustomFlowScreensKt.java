package com.momokotle.agent.presentation.custommenu;

import android.Manifest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import com.momokotle.agent.models.AutomationStatus;
import com.momokotle.agent.models.CustomFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000>\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u001a:\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a\u00c2\u0001\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u0003\u001a \u0010\u0019\u001a\u00020\u00012\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u001b\u001a\u00020\u001cH\u0007\u00a8\u0006\u001d"}, d2 = {"CustomFlowCard", "", "flow", "Lcom/momokotle/agent/models/CustomFlow;", "onRun", "Lkotlin/Function0;", "onEdit", "onDelete", "CustomFlowEditForm", "formState", "Lcom/momokotle/agent/presentation/custommenu/CustomFlowFormState;", "onNameChange", "Lkotlin/Function1;", "", "onUssdCodeChange", "onStep1Change", "onStep2Change", "onStep3Change", "onDynamicPhoneChange", "", "onDynamicAmountChange", "onSave", "onCancel", "modifier", "Landroidx/compose/ui/Modifier;", "CustomFlowListScreen", "onBack", "viewModel", "Lcom/momokotle/agent/presentation/custommenu/CustomFlowViewModel;", "app_debug"})
public final class CustomFlowScreensKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void CustomFlowListScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.custommenu.CustomFlowViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CustomFlowEditForm(com.momokotle.agent.presentation.custommenu.CustomFlowFormState formState, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onNameChange, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onUssdCodeChange, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onStep1Change, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onStep2Change, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onStep3Change, kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onDynamicPhoneChange, kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onDynamicAmountChange, kotlin.jvm.functions.Function0<kotlin.Unit> onSave, kotlin.jvm.functions.Function0<kotlin.Unit> onCancel, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CustomFlowCard(com.momokotle.agent.models.CustomFlow flow, kotlin.jvm.functions.Function0<kotlin.Unit> onRun, kotlin.jvm.functions.Function0<kotlin.Unit> onEdit, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
}