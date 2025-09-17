package com.example.logonapp.data.model.error

import com.example.logonapp.data.model.logon.LogonResponseModel

sealed class LogonResult {
    data class Success(val data: LogonResponseModel) : LogonResult()
    data class Error(val message: String) : LogonResult()
    object Loading : LogonResult()
    object Idle : LogonResult()
}
