package com.momokotle.agent.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface MStorepayApiService {
    @POST("api/v1/orders")
    suspend fun submitOrder(@Body request: OrderSubmitRequest): ApiResponse

    @POST("api/v1/forms")
    suspend fun submitFormMetadata(@Body request: FormSubmitRequest): ApiResponse

    @Multipart
    @POST("api/v1/forms/{submissionId}/documents")
    suspend fun uploadFormDocument(
        @Path("submissionId") submissionId: String,
        @Part("slotId") slotId: RequestBody,
        @Part file: MultipartBody.Part
    ): ApiResponse
}
