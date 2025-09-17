package com.example.logonapp.data.repository.Logon

import com.example.logonapp.data.infrastructure.RetrofitInstance
import com.example.logonapp.data.model.error.ErrorModel
import com.example.logonapp.data.model.error.LogonResult
import com.example.logonapp.data.model.logon.LogonRequestModel
import com.example.logonapp.data.model.logon.LogonResponseModel
import com.example.logonapp.data.service.Logon.Interface.ILogonService
import retrofit2.Response

class LogonRepository(
    private val api: ILogonService = RetrofitInstance.getLogonApi()
) {

    suspend fun logon(terminalId: String, instId: String): LogonResult {
        return try {
            val request = LogonRequestModel(terminalId, instId)
            val response: LogonResponseModel = api.Logon(request)
            LogonResult.Success(response)
        } catch (e: retrofit2.HttpException) {
            val error = parseError(e.response())
            LogonResult.Error(error?.errorCode ?: e.message())
        } catch (e: Exception) {
            LogonResult.Error(e.message ?: "خطایی رخ داد")
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
