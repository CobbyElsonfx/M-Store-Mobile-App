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
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ$\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\f\u001a\u00020\u000bH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\r\u0010\u000eJ\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0012"}, d2 = {"Lcom/momokotle/agent/repository/FormSubmissionRepositoryImpl;", "Lcom/momokotle/agent/domain/repository/FormSubmissionRepository;", "formSubmissionDao", "Lcom/momokotle/agent/database/dao/FormSubmissionDao;", "api", "Lcom/momokotle/agent/data/remote/MStorepayApiService;", "networkMonitor", "Lcom/momokotle/agent/utils/NetworkMonitor;", "(Lcom/momokotle/agent/database/dao/FormSubmissionDao;Lcom/momokotle/agent/data/remote/MStorepayApiService;Lcom/momokotle/agent/utils/NetworkMonitor;)V", "createAndSubmitForm", "Lkotlin/Result;", "Lcom/momokotle/agent/models/FormSubmission;", "submission", "createAndSubmitForm-gIAlu-s", "(Lcom/momokotle/agent/models/FormSubmission;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "guessMediaType", "", "fileName", "app_debug"})
public final class FormSubmissionRepositoryImpl implements com.momokotle.agent.domain.repository.FormSubmissionRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.database.dao.FormSubmissionDao formSubmissionDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.data.remote.MStorepayApiService api = null;
    @org.jetbrains.annotations.NotNull()
    private final com.momokotle.agent.utils.NetworkMonitor networkMonitor = null;
    
    @javax.inject.Inject()
    public FormSubmissionRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.momokotle.agent.database.dao.FormSubmissionDao formSubmissionDao, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.remote.MStorepayApiService api, @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.utils.NetworkMonitor networkMonitor) {
        super();
    }
    
    private final java.lang.String guessMediaType(java.lang.String fileName) {
        return null;
    }
}