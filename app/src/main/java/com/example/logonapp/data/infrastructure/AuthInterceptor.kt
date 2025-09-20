package com.example.logonapp.data.infrastructure

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: () -> String?): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response{
        val originalRequest = chain.request()

        if (originalRequest.url.encodedPath.contains("/ums/usr/login")) {
            return chain.proceed(originalRequest)
        }

        val token = tokenProvider()
        val isValidToken = token != null && isTokenValid(token)
            if(isValidToken){
            Log.e("AuthInterceptor", "Token expired or invalid, proceeding without token")
               return chain.proceed(originalRequest)
        }

        Log.e("AuthInterceptor", "Token in header: $token")

        val requestBuilder = originalRequest.newBuilder()
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
