package com.momokotle.agent.presentation.store;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.layout.ContentScale;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import com.momokotle.agent.models.StoreCategory;
import com.momokotle.agent.models.StoreProduct;
import com.momokotle.agent.ui.components.AppButtonStyle;
import com.momokotle.agent.ui.components.AppStatusTone;
import com.momokotle.agent.ui.theme.AppDimens;
import com.momokotle.agent.utils.InternetMessages;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00006\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u001a\u001a\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0003\u001aB\u0010\u0005\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000fH\u0003\u001a\"\u0010\u0010\u001a\u00020\u00012\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\b\b\u0002\u0010\u0012\u001a\u00020\u0013H\u0007\u00a8\u0006\u0014"}, d2 = {"ProductImage", "", "imageUrl", "", "contentDescription", "StoreProductCard", "product", "Lcom/momokotle/agent/models/StoreProduct;", "quantity", "", "isOnline", "", "onQuantityChange", "Lkotlin/Function1;", "onBuy", "Lkotlin/Function0;", "StoreScreen", "onMyOrders", "viewModel", "Lcom/momokotle/agent/presentation/store/StoreViewModel;", "app_debug"})
public final class StoreScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void StoreScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onMyOrders, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.store.StoreViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void StoreProductCard(com.momokotle.agent.models.StoreProduct product, int quantity, boolean isOnline, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onQuantityChange, kotlin.jvm.functions.Function0<kotlin.Unit> onBuy) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ProductImage(java.lang.String imageUrl, java.lang.String contentDescription) {
    }
}