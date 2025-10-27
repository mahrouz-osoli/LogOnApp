package com.example.logonapp.data.service.Terminal.Interfaces

import com.example.logonapp.data.model.Terminal.TerminalViewResponseModel
import com.example.logonapp.data.model.Terminal.TerminalsRequestModel
import retrofit2.http.POST
import retrofit2.http.Body

interface ITerminalService{
    @POST("mms/bo/terminal/viewTerminals")
    suspend fun getTerminals(
        @Body request: TerminalsRequestModel
    ):TerminalViewResponseModel
}