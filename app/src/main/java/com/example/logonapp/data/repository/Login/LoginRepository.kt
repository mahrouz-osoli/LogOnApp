package com.example.logonapp.data.repository.Login

import com.example.logonapp.data.datasourse.UserAuth
import retrofit2.Response
import com.example.logonapp.data.model.error.ErrorModel
import com.example.logonapp.data.model.error.LoginResult
import com.example.logonapp.data.model.login.LoginRequestModel
import com.example.logonapp.data.model.login.LoginResponseModel
import com.example.logonapp.data.infrastructure.RetrofitInstance

class LoginRepository(private val userAuth: UserAuth) {

    suspend fun login(username: String, password: String): LoginResult {
        return try {
            val request = LoginRequestModel(username, password)
            val response: LoginResponseModel = RetrofitInstance.getApi().loginUser(request)
            LoginResult.Success(response)
        } catch (e: retrofit2.HttpException) {
            val error = parseError(e.response())
            LoginResult.Error(error?.errorCode ?: e.message())
        } catch (e: Exception) {
            LoginResult.Error(e.message ?: "خطایی رخ داد")
        }
    }

    private fun parseError(response: Response<*>?): ErrorModel? {
        val converter = RetrofitInstance.getRetrofitInstance()
            .responseBodyConverter<ErrorModel>(ErrorModel::class.java, arrayOf())
        return try {
            response?.errorBody()?.let {
                converter.convert(it)
            }
        } catch (ex: Exception) {
            null
        }
    }
}
