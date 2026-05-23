package com.momokotle.agent.data.remote;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ,\u0010\n\u001a\u00020\u00032\b\b\u0001\u0010\u000b\u001a\u00020\f2\b\b\u0001\u0010\r\u001a\u00020\u000e2\b\b\u0001\u0010\u000f\u001a\u00020\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011\u00a8\u0006\u0012"}, d2 = {"Lcom/momokotle/agent/data/remote/MStorepayApiService;", "", "submitFormMetadata", "Lcom/momokotle/agent/data/remote/ApiResponse;", "request", "Lcom/momokotle/agent/data/remote/FormSubmitRequest;", "(Lcom/momokotle/agent/data/remote/FormSubmitRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "submitOrder", "Lcom/momokotle/agent/data/remote/OrderSubmitRequest;", "(Lcom/momokotle/agent/data/remote/OrderSubmitRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "uploadFormDocument", "submissionId", "", "slotId", "Lokhttp3/RequestBody;", "file", "Lokhttp3/MultipartBody$Part;", "(Ljava/lang/String;Lokhttp3/RequestBody;Lokhttp3/MultipartBody$Part;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface MStorepayApiService {
    
    @retrofit2.http.POST(value = "api/v1/orders")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object submitOrder(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.remote.OrderSubmitRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.data.remote.ApiResponse> $completion);
    
    @retrofit2.http.POST(value = "api/v1/forms")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object submitFormMetadata(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.momokotle.agent.data.remote.FormSubmitRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.data.remote.ApiResponse> $completion);
    
    @retrofit2.http.Multipart()
    @retrofit2.http.POST(value = "api/v1/forms/{submissionId}/documents")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object uploadFormDocument(@retrofit2.http.Path(value = "submissionId")
    @org.jetbrains.annotations.NotNull()
    java.lang.String submissionId, @retrofit2.http.Part(value = "slotId")
    @org.jetbrains.annotations.NotNull()
    okhttp3.RequestBody slotId, @retrofit2.http.Part()
    @org.jetbrains.annotations.NotNull()
    okhttp3.MultipartBody.Part file, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.momokotle.agent.data.remote.ApiResponse> $completion);
}