package com.momokotle.agent.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0004\u00a8\u0006\t"}, d2 = {"Lcom/momokotle/agent/models/OrderStatusMapper;", "", "()V", "fromStorage", "Lcom/momokotle/agent/models/OrderStatus;", "value", "", "toStorageValue", "status", "app_debug"})
public final class OrderStatusMapper {
    @org.jetbrains.annotations.NotNull()
    public static final com.momokotle.agent.models.OrderStatusMapper INSTANCE = null;
    
    private OrderStatusMapper() {
        super();
    }
    
    /**
     * Persisted in Room as Firestore display string.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String toStorageValue(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.models.OrderStatus status) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.momokotle.agent.models.OrderStatus fromStorage(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
        return null;
    }
}