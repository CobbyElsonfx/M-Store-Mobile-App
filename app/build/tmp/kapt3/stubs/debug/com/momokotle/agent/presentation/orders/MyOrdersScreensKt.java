package com.momokotle.agent.presentation.orders;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.momokotle.agent.models.OrderStatus;
import com.momokotle.agent.models.StoreOrder;
import com.momokotle.agent.ui.components.AppStatusTone;
import com.momokotle.agent.ui.theme.AppDimens;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000L\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0003\u001a4\u0010\u0005\u001a\u00020\u00012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\t2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0007\u001a(\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u00032\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\n\u001a\u00020\u000eH\u0007\u001a\u001e\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a\u0010\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u0015H\u0007\u001a\u0010\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0011H\u0003\u001a\u0010\u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\u0019H\u0002\u001a\u001c\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u001c0\u001b2\u0006\u0010\u0014\u001a\u00020\u0015H\u0003\u00a8\u0006\u001d"}, d2 = {"DetailRow", "", "label", "", "value", "MyOrdersScreen", "onBack", "Lkotlin/Function0;", "onOrderClick", "Lkotlin/Function1;", "viewModel", "Lcom/momokotle/agent/presentation/orders/MyOrdersViewModel;", "OrderDetailScreen", "orderId", "Lcom/momokotle/agent/presentation/orders/OrderDetailViewModel;", "OrderListCard", "order", "Lcom/momokotle/agent/models/StoreOrder;", "onClick", "OrderStatusBadge", "status", "Lcom/momokotle/agent/models/OrderStatus;", "RowWithBadge", "formatOrderDate", "timestamp", "", "statusColors", "Lkotlin/Pair;", "Landroidx/compose/ui/graphics/Color;", "app_debug"})
public final class MyOrdersScreensKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void MyOrdersScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOrderClick, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.orders.MyOrdersViewModel viewModel) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void OrderDetailScreen(@org.jetbrains.annotations.NotNull()
    java.lang.String orderId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.presentation.orders.OrderDetailViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void OrderListCard(com.momokotle.agent.models.StoreOrder order, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void RowWithBadge(com.momokotle.agent.models.StoreOrder order) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void OrderStatusBadge(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.OrderStatus status) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final kotlin.Pair<androidx.compose.ui.graphics.Color, androidx.compose.ui.graphics.Color> statusColors(com.momokotle.agent.models.OrderStatus status) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DetailRow(java.lang.String label, java.lang.String value) {
    }
    
    private static final java.lang.String formatOrderDate(long timestamp) {
        return null;
    }
}