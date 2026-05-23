package com.momokotle.agent.presentation.store;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.momokotle.agent.models.StoreCategory;
import com.momokotle.agent.models.StoreItem;
import com.momokotle.agent.utils.InternetMessages;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000(\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a:\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a\u0012\u0010\n\u001a\u00020\u00012\b\b\u0002\u0010\u000b\u001a\u00020\fH\u0007\u00a8\u0006\r"}, d2 = {"StoreItemCard", "", "item", "Lcom/momokotle/agent/models/StoreItem;", "quantity", "", "onQuantityChange", "Lkotlin/Function1;", "onBuy", "Lkotlin/Function0;", "StoreScreen", "viewModel", "Lcom/momokotle/agent/presentation/store/StoreViewModel;", "app_debug"})
public final class StoreScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void StoreScreen(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.store.StoreViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void StoreItemCard(com.momokotle.agent.models.StoreItem item, int quantity, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onQuantityChange, kotlin.jvm.functions.Function0<kotlin.Unit> onBuy) {
    }
}