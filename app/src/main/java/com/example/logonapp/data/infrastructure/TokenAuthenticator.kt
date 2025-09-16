package com.example.logonapp.data.infrastructure

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import com.example.logonapp.data.datasourse.UserAuth
import com.example.logonapp.data.service.Login.Interface.ILoginService
import com.example.logonapp.data.model.login.LoginRequestModel


class TokenAuthenticator(
    private val userAuth: UserAuth,
    private val loginApi: ILoginService
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        return runBlocking {
            val username = userAuth.getUserName() ?: return@runBlocking null
            val password = userAuth.getPassword() ?: return@runBlocking null

            val loginResponse = try {
                loginApi.loginUser(LoginRequestModel(username, password))
            } catch (e: Exception) {
                return@runBlocking null
            }

            val newToken = loginResponse.token ?: return@runBlocking null
            userAuth.saveToken(newToken)
            response.request.newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}
