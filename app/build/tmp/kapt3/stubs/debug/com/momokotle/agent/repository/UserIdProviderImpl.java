package com.momokotle.agent.repository;

import com.momokotle.agent.data.datastore.UserAccountDataStore;
import com.momokotle.agent.data.remote.FormSubmitRequest;
import com.momokotle.agent.data.remote.MStorepayApiService;
import com.momokotle.agent.data.remote.OrderSubmitRequest;
import com.momokotle.agent.database.dao.FormSubmissionDao;
import com.momokotle.agent.database.dao.StoreOrderDao;
import com.momokotle.agent.domain.repository.FormSubmissionRepository;
import com.momokotle.agent.domain.repository.StoreRepository;
import com.momokotle.agent.domain.repository.UserIdProvider;
import com.momokotle.agent.models.FormSubmission;
import com.momokotle.agent.models.FormSubmissionStatus;
import com.momokotle.agent.models.OrderStatus;
import com.momokotle.agent.models.StoreOrder;
import com.momokotle.agent.utils.NetworkMonitor;
import okhttp3.MultipartBody;
import java.io.File;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/momokotle/agent/repository/UserIdProviderImpl;", "Lcom/momokotle/agent/domain/repository/UserIdProvider;", "userAccountDataStore", "Lcom/momokotle/agent/data/datastore/UserAccountDataStore;", "(Lcom/momokotle/agent/data/datastore/UserAccountDataStore;)V", "getUserId", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class UserIdProviderImpl implements com.momokotle.agent.domain.repository.UserIdProvider {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.data.datastore.UserAccountDataStore userAccountDataStore = null;
    
    @javax.inject.Inject()
    public UserIdProviderImpl(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.datastore.UserAccountDataStore userAccountDataStore) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getUserId(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
}