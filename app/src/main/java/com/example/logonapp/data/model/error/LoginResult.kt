package com.example.logonapp.data.model.error

import LoginResponseModel

sealed class LoginResult{
    data class Success(val data: LoginResponseModel) : LoginResult()
    data class Error(val message: String) : LoginResult()
    object Loading : LoginResult()
    object Idle : LoginResult()
}