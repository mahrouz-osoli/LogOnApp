package com.example.logonapp.data.infrastructure

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: () -> String?): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response{
        val originalRequest = chain.request()
        val token = tokenProvider()


        // TODO : Check if response code is 401, request for new access token

        val requestBuilder = originalRequest.newBuilder()
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
