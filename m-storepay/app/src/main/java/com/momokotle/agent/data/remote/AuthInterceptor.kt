package com.momokotle.agent.data.remote

import com.momokotle.agent.data.datastore.AuthTokenDataStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authTokenDataStore: AuthTokenDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        if (path.contains("/api/auth/login") ||
            path.contains("/api/auth/register") ||
            path.contains("/api/referrals/validate/")
        ) {
            return chain.proceed(request)
        }

        val token = authTokenDataStore.currentToken()
        val authenticatedRequest = if (token.isNullOrBlank()) {
            request
        } else {
            request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }

        return chain.proceed(authenticatedRequest)
    }
}
