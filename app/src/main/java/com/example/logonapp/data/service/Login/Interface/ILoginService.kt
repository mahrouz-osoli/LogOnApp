package com.example.logonapp.data.service.Login.Interface

import com.example.logonapp.data.model.login.LoginRequestModel
import com.example.logonapp.data.model.login.LoginResponseModel
import retrofit2.http.Body
import retrofit2.http.POST

interface ILoginService {
    @POST("ums/usr/login")
    suspend fun loginUser(
        @Body request: LoginRequestModel
    ): LoginResponseModel
}