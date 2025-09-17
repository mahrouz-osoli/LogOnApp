package com.example.logonapp.data.service.Logon.Interface

import com.example.logonapp.data.model.logon.LogonRequestModel
import com.example.logonapp.data.model.logon.LogonResponseModel
import retrofit2.http.POST
import retrofit2.http.Body

interface ILogonService{
    @POST("mms/bo/terminal/reInitLogon")
    suspend fun Logon(
        @Body request: LogonRequestModel
    ): LogonResponseModel
}