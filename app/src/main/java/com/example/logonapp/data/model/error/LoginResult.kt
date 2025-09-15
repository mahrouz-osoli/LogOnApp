package com.example.logonapp.data.model.error

import com.example.logonapp.data.model.login.LoginResponseModel

sealed class LoginResult{
    data class Success(val data: LoginResponseModel) : LoginResult()
    data class Error(val message: String) : LoginResult()
    data object Loading : LoginResult()
    data object Idle : LoginResult()
}