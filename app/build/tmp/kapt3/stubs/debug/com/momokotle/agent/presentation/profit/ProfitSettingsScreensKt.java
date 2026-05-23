package com.momokotle.agent.presentation.profit;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.momokotle.agent.models.ProfitCalculationMode;
import com.momokotle.agent.models.ProfitRule;
import com.momokotle.agent.utils.AmountFormatter;
import com.momokotle.agent.utils.PercentFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000n\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a$\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a \u0010\u0006\u001a\u00020\u00012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a \u0010\u000b\u001a\u00020\u00012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\fH\u0007\u001a~\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a2\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u00162\u0012\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a \u0010 \u001a\u00020\u00012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\t\u001a\u00020!H\u0007\u001aL\u0010\"\u001a\u00020\u00012\u0006\u0010#\u001a\u00020\u00162\u0006\u0010$\u001a\u00020\u00162\u0006\u0010%\u001a\u00020\u00162\u0012\u0010&\u001a\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\b\u0010(\u001a\u0004\u0018\u00010)H\u0003\u001a&\u0010*\u001a\u00020\u00012\u0006\u0010#\u001a\u00020\u00162\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\u0006\u0010\t\u001a\u00020+H\u0003\u001a#\u0010,\u001a\u00020\u00012\u0006\u0010#\u001a\u00020\u00162\u0011\u0010-\u001a\r\u0012\u0004\u0012\u00020\u00010\b\u00a2\u0006\u0002\b.H\u0003\u001a\u0010\u0010/\u001a\u00020\u00162\u0006\u00100\u001a\u00020\u001aH\u0002\u00a8\u00061"}, d2 = {"CalculationModeSection", "", "selectedMode", "Lcom/momokotle/agent/models/ProfitCalculationMode;", "onModeSelected", "Lkotlin/Function1;", "CashInSettingsScreen", "onBack", "Lkotlin/Function0;", "viewModel", "Lcom/momokotle/agent/presentation/profit/CashInSettingsViewModel;", "CashOutSettingsScreen", "Lcom/momokotle/agent/presentation/profit/CashOutSettingsViewModel;", "CustomizeSettingsSection", "enabled", "", "draftRule", "Lcom/momokotle/agent/presentation/profit/DraftProfitRule;", "pendingRules", "", "Lcom/momokotle/agent/models/ProfitRule;", "onDraftAmountChange", "", "onDraftProfitPercentChange", "onAddRule", "onRemoveRule", "", "onSaveSettings", "DefaultSettingsSection", "percentInput", "onPercentChange", "onSaveDefaultPercent", "ProfitCalculationScreen", "Lcom/momokotle/agent/presentation/profit/ProfitCalculationViewModel;", "ProfitCalculatorSection", "title", "modeLabel", "amount", "onAmountChange", "onCalculate", "result", "Lcom/momokotle/agent/models/ProfitResult;", "ProfitTypeSettingsScreen", "Lcom/momokotle/agent/presentation/profit/ProfitTypeSettingsViewModel;", "SettingsBlock", "content", "Landroidx/compose/runtime/Composable;", "formatProfit", "value", "app_debug"})
public final class ProfitSettingsScreensKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ProfitCalculationScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.profit.ProfitCalculationViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ProfitCalculatorSection(java.lang.String title, java.lang.String modeLabel, java.lang.String amount, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onAmountChange, kotlin.jvm.functions.Function0<kotlin.Unit> onCalculate, com.momokotle.agent.models.ProfitResult result) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void CashInSettingsScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.profit.CashInSettingsViewModel viewModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void CashOutSettingsScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.profit.CashOutSettingsViewModel viewModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void ProfitTypeSettingsScreen(java.lang.String title, kotlin.jvm.functions.Function0<kotlin.Unit> onBack, com.momokotle.agent.presentation.profit.ProfitTypeSettingsViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CalculationModeSection(com.momokotle.agent.models.ProfitCalculationMode selectedMode, kotlin.jvm.functions.Function1<? super com.momokotle.agent.models.ProfitCalculationMode, kotlin.Unit> onModeSelected) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DefaultSettingsSection(java.lang.String percentInput, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onPercentChange, kotlin.jvm.functions.Function0<kotlin.Unit> onSaveDefaultPercent) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CustomizeSettingsSection(boolean enabled, com.momokotle.agent.presentation.profit.DraftProfitRule draftRule, java.util.List<com.momokotle.agent.models.ProfitRule> pendingRules, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDraftAmountChange, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDraftProfitPercentChange, kotlin.jvm.functions.Function0<kotlin.Unit> onAddRule, kotlin.jvm.functions.Function1<? super java.lang.Double, kotlin.Unit> onRemoveRule, kotlin.jvm.functions.Function0<kotlin.Unit> onSaveSettings) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SettingsBlock(java.lang.String title, kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
    
    private static final java.lang.String formatProfit(double value) {
        return null;
    }
}