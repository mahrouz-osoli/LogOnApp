package com.example.logonapp.data.infrastructure

import com.example.logonapp.data.datasourse.UserAuth
import com.example.logonapp.data.service.Login.Interface.ILoginService
import okhttp3.Interceptor
import okhttp3.Response
import kotlinx.coroutines.runBlocking

class AuthInterceptor(
    private val userAuth: UserAuth,
    private val loginApi: () -> ILoginService
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (originalRequest.url.encodedPath.contains("/ums/usr/login")) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking { userAuth.getCachedToken() }

        val requestBuilder = originalRequest.newBuilder()
        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
            requestBuilder.addHeader("Cookie", "token=$token")
        }

        var response = chain.proceed(requestBuilder.build())

        if (response.code == 401 || response.code == 403) {
            response.close()

            val newRequest = runBlocking {
                val username = userAuth.getUserName() ?: return@runBlocking null
                val password = userAuth.getPassword() ?: return@runBlocking null
                val loginService = loginApi()
                try {
                    val loginResponse = loginService.loginUser(com.example.logonapp.data.model.login.LoginRequestModel(username, password))
                    val newToken = loginResponse.token ?: return@runBlocking null
                    userAuth.saveToken(newToken)
                    userAuth.saveTokenTime(System.currentTimeMillis())
                    originalRequest.newBuilder()
                        .header("Authorization", "Bearer $newToken")
                        .header("Cookie", "token=$newToken")
                        .build()
                } catch (e: Exception) {
                    null
                }
            }

            if (newRequest != null) {
                response = chain.proceed(newRequest)
            }
        }

        return response
    }
}
