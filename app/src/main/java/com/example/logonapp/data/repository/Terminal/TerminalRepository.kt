package com.example.logonapp.data.repository.Terminal

import com.example.logonapp.data.model.Terminal.TerminalsRequestModel
import com.example.logonapp.data.service.Terminal.Interfaces.ITerminalService

class TerminalRepository(
    private val api: ITerminalService
){
    suspend fun getTerminalBySerial(serial: String): Result<String>
    {
        return try {
            val body = TerminalsRequestModel(terminalSerial = serial)
            val response = api.getTerminals(body)
            val terminalId = response.result.firstOrNull()?.terminalId
            if (terminalId.isNullOrEmpty()){
                Result.failure(Exception("ترمینالی با این سریال پیدا نشد..."))
            }else{
                Result.success(terminalId)
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}