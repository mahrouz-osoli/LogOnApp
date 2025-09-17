package com.example.logonapp.data.infrastructure

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import kotlin.math.log

class AuthInterceptor(private val tokenProvider: () -> String?): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response{
        val originalRequest = chain.request()
        val token = tokenProvider()

        Log.e("AuthInterceptor", "Token in header: $token")

        val requestBuilder = originalRequest.newBuilder()
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
