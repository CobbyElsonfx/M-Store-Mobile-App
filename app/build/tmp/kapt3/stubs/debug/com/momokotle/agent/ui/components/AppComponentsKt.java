package com.momokotle.agent.ui.components;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.layout.ColumnScope;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.FilterChipDefaults;
import androidx.compose.material3.NavigationBarItemDefaults;
import androidx.compose.material3.OutlinedTextFieldDefaults;
import androidx.compose.material3.TopAppBarDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.VisualTransformation;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;
import com.momokotle.agent.ui.theme.AppColors;
import com.momokotle.agent.ui.theme.AppDimens;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u0088\u0001\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u001f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0019\u001aR\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\f\u001a\u00020\t2\b\b\u0002\u0010\r\u001a\u00020\tH\u0007\u001a&\u0010\u000e\u001a\u00020\u00012\u001c\u0010\u000f\u001a\u0018\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00010\u0010\u00a2\u0006\u0002\b\u0012\u00a2\u0006\u0002\b\u0013H\u0007\u001a<\u0010\u0014\u001a\u00020\u00012\u001e\u0010\u0015\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\t0\u00170\u00162\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00010\u0010H\u0007\u001a<\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\u001c\u001a\u00020\u001dH\u0007\u001a$\u0010\u001e\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u00032\b\b\u0002\u0010\u001f\u001a\u00020 H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b!\u0010\"\u001aB\u0010#\u001a\u00020\u00012\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u0010\b\u0002\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u00052\u001c\u0010\u000f\u001a\u0018\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00010\u0010\u00a2\u0006\u0002\b\u0012\u00a2\u0006\u0002\b\u0013H\u0007\u001a\b\u0010$\u001a\u00020%H\u0007\u001a$\u0010&\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\'\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a&\u0010(\u001a\u00020\u00012\u0006\u0010)\u001a\u00020\u00032\n\b\u0002\u0010*\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a\u001a\u0010+\u001a\u00020\u00012\u0006\u0010*\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a0\u0010,\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010-\u001a\u00020\t2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a \u0010.\u001a\u00020\u00012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a\u001a\u0010/\u001a\u00020\u00012\u0006\u0010*\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a\u0012\u00100\u001a\u00020\u00012\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a&\u00101\u001a\u00020\u00012\u0006\u0010)\u001a\u00020\u00032\n\b\u0002\u00102\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a>\u00103\u001a\u00020\u00012\u0006\u0010)\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u00102\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u00104\u001a\u00020\tH\u0007\u001a{\u00105\u001a\u00020\u00012\u0006\u00106\u001a\u00020\u00032\u0006\u00107\u001a\u00020\u00032\u0006\u00108\u001a\u00020\u00032\b\u00109\u001a\u0004\u0018\u00010\u00032\u0006\u0010:\u001a\u00020\u00192\u0006\u0010;\u001a\u00020\t2\u0012\u0010<\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00010\u00102\f\u0010=\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u0013\b\u0002\u0010>\u001a\r\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b\u0012H\u0007\u001a\u00ac\u0001\u0010?\u001a\u00020\u00012\n\b\u0002\u0010)\u001a\u0004\u0018\u00010\u00032\u0010\b\u0002\u0010@\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u00052\u0013\b\u0002\u0010A\u001a\r\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b\u00122\u0013\b\u0002\u0010B\u001a\r\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b\u00122\u0013\b\u0002\u0010C\u001a\r\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b\u00122\u0013\b\u0002\u0010D\u001a\r\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b\u00122\b\b\u0002\u0010\u0006\u001a\u00020\u00072&\u0010\u000f\u001a\"\u0012\u0013\u0012\u00110E\u00a2\u0006\f\bF\u0012\b\b6\u0012\u0004\b\b(G\u0012\u0004\u0012\u00020\u00010\u0010\u00a2\u0006\u0002\b\u0012H\u0007\u001a8\u0010H\u001a\u00020\u00012\u0006\u0010\'\u001a\u00020\u00032\u0012\u0010I\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00102\b\b\u0002\u0010J\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a&\u0010K\u001a\u00020\u00012\u0006\u0010)\u001a\u00020\u00032\n\b\u0002\u00102\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a$\u0010L\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u00032\b\b\u0002\u0010M\u001a\u00020N2\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a\u009b\u0001\u0010O\u001a\u00020\u00012\u0006\u0010\'\u001a\u00020\u00032\u0012\u0010I\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00102\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010P\u001a\u00020Q2\b\b\u0002\u0010R\u001a\u00020S2\b\b\u0002\u0010T\u001a\u00020\t2\n\b\u0002\u0010J\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010U\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010V\u001a\u00020\t2\b\b\u0002\u0010W\u001a\u00020\t2\n\b\u0002\u0010X\u001a\u0004\u0018\u00010\u0019H\u0007\u00a2\u0006\u0002\u0010Y\u001a7\u0010Z\u001a\u00020\u00012\u0006\u0010)\u001a\u00020\u00032\u0010\b\u0002\u0010@\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u00052\u0013\b\u0002\u0010A\u001a\r\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b\u0012H\u0007\u001al\u0010[\u001a\u00020\u00012\u0006\u0010\\\u001a\u00020\u00032\b\u0010]\u001a\u0004\u0018\u00010\u00032\u0006\u0010^\u001a\u00020\u00032\b\u0010_\u001a\u0004\u0018\u00010\u00032\u0006\u0010`\u001a\u00020\u00032\u0006\u0010a\u001a\u00020N2\u0006\u0010b\u001a\u00020\u00032\n\b\u0002\u0010c\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u0010\b\u0002\u0010d\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u0005H\u0007\u001aL\u0010e\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010f\u001a\u0004\u0018\u00010\u00032\b\u0010g\u001a\u0004\u0018\u00010\u00032\f\u0010h\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010i\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a\u0010\u0010j\u001a\u00020N2\u0006\u0010k\u001a\u00020\u0003H\u0007\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006l"}, d2 = {"AppActionGridButton", "", "label", "", "onClick", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "enabled", "", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "highlighted", "showCustomBadge", "AppAuthCard", "content", "Lkotlin/Function1;", "Landroidx/compose/foundation/layout/ColumnScope;", "Landroidx/compose/runtime/Composable;", "Lkotlin/ExtensionFunctionType;", "AppBottomNav", "items", "", "Lkotlin/Triple;", "onItemClick", "", "AppButton", "text", "style", "Lcom/momokotle/agent/ui/components/AppButtonStyle;", "AppButtonLabel", "color", "Landroidx/compose/ui/graphics/Color;", "AppButtonLabel-4WTKRHQ", "(Ljava/lang/String;J)V", "AppCard", "AppCardDefaults", "Landroidx/compose/material3/CardColors;", "AppDetailRow", "value", "AppEmptyState", "title", "message", "AppErrorBanner", "AppFilterChip", "selected", "AppIconActionFab", "AppInfoBanner", "AppLoadingState", "AppLogoHeader", "subtitle", "AppMenuListItem", "showArrow", "AppProductCard", "name", "price", "stockLabel", "imageUrl", "quantity", "isOnline", "onQuantityChange", "onBuy", "imageContent", "AppScaffold", "onBack", "actions", "bottomBar", "floatingActionButton", "snackbarHost", "Landroidx/compose/foundation/layout/PaddingValues;", "Lkotlin/ParameterName;", "padding", "AppSearchField", "onValueChange", "placeholder", "AppSectionHeader", "AppStatusBadge", "tone", "Lcom/momokotle/agent/ui/components/AppStatusTone;", "AppTextField", "keyboardOptions", "Landroidx/compose/foundation/text/KeyboardOptions;", "visualTransformation", "Landroidx/compose/ui/text/input/VisualTransformation;", "singleLine", "errorText", "isValid", "showValidation", "maxLength", "(Ljava/lang/String;Lkotlin/jvm/functions/Function1;Ljava/lang/String;Landroidx/compose/ui/Modifier;ZLandroidx/compose/foundation/text/KeyboardOptions;Landroidx/compose/ui/text/input/VisualTransformation;ZLjava/lang/String;Ljava/lang/String;ZZLjava/lang/Integer;)V", "AppTopBar", "AppTransactionCard", "typeLabel", "phoneNumber", "amount", "profit", "statusLabel", "statusTone", "dateTime", "flowName", "onRepeat", "AppUploadBox", "instruction", "selectedFileName", "onPick", "onRemove", "statusToneForLabel", "status", "app_debug"})
public final class AppComponentsKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void AppScaffold(@org.jetbrains.annotations.Nullable()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> actions, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> bottomBar, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> floatingActionButton, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> snackbarHost, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super androidx.compose.foundation.layout.PaddingValues, kotlin.Unit> content) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void AppTopBar(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> actions) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppBottomNav(@org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Triple<java.lang.String, androidx.compose.ui.graphics.vector.ImageVector, java.lang.Boolean>> items, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onItemClick) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void AppCard(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super androidx.compose.foundation.layout.ColumnScope, kotlin.Unit> content) {
    }
    
    @androidx.compose.runtime.Composable()
    @org.jetbrains.annotations.NotNull()
    public static final androidx.compose.material3.CardColors AppCardDefaults() {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppButton(@org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, boolean enabled, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.ui.components.AppButtonStyle style) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppTextField(@org.jetbrains.annotations.NotNull()
    java.lang.String value, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValueChange, @org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, boolean enabled, @org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.text.KeyboardOptions keyboardOptions, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.text.input.VisualTransformation visualTransformation, boolean singleLine, @org.jetbrains.annotations.Nullable()
    java.lang.String placeholder, @org.jetbrains.annotations.Nullable()
    java.lang.String errorText, boolean isValid, boolean showValidation, @org.jetbrains.annotations.Nullable()
    java.lang.Integer maxLength) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppStatusBadge(@org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.ui.components.AppStatusTone tone, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppSectionHeader(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitle, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppEmptyState(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppLoadingState(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppUploadBox(@org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.Nullable()
    java.lang.String instruction, @org.jetbrains.annotations.Nullable()
    java.lang.String selectedFileName, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onPick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRemove, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppActionGridButton(@org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, boolean enabled, @org.jetbrains.annotations.Nullable()
    androidx.compose.ui.graphics.vector.ImageVector icon, boolean highlighted, boolean showCustomBadge) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppTransactionCard(@org.jetbrains.annotations.NotNull()
    java.lang.String typeLabel, @org.jetbrains.annotations.Nullable()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String amount, @org.jetbrains.annotations.Nullable()
    java.lang.String profit, @org.jetbrains.annotations.NotNull()
    java.lang.String statusLabel, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.ui.components.AppStatusTone statusTone, @org.jetbrains.annotations.NotNull()
    java.lang.String dateTime, @org.jetbrains.annotations.Nullable()
    java.lang.String flowName, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRepeat) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppProductCard(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String price, @org.jetbrains.annotations.NotNull()
    java.lang.String stockLabel, @org.jetbrains.annotations.Nullable()
    java.lang.String imageUrl, int quantity, boolean isOnline, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onQuantityChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBuy, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> imageContent) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppSearchField(@org.jetbrains.annotations.NotNull()
    java.lang.String value, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValueChange, @org.jetbrains.annotations.NotNull()
    java.lang.String placeholder, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppFilterChip(@org.jetbrains.annotations.NotNull()
    java.lang.String label, boolean selected, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppMenuListItem(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitle, boolean showArrow) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppLogoHeader(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitle, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppErrorBanner(@org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppInfoBanner(@org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppDetailRow(@org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.Nullable()
    java.lang.String value, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppAuthCard(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super androidx.compose.foundation.layout.ColumnScope, kotlin.Unit> content) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AppIconActionFab(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.ui.components.AppStatusTone statusToneForLabel(@org.jetbrains.annotations.NotNull()
    java.lang.String status) {
        return null;
    }
}