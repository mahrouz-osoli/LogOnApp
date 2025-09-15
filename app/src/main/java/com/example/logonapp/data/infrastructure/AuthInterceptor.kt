package com.example.logonapp.data.infrastructure

import com.example.logonapp.data.datasourse.UserAuth
import com.example.logonapp.data.service.Login.Interface.ILoginService
import okhttp3.Interceptor
import okhttp3.Response
import com.example.logonapp.data.model.login.LoginRequestModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import android.util.Log

class AuthInterceptor(private val tokenProvider: () -> String?,private val userAuth: UserAuth, private val loginApi: ILoginService): Interceptor{
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


