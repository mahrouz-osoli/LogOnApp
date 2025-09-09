package com.example.logonapp.presentaion.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

class LoginViewModel : ViewModel() {
    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun onUsernameChange(newUsername: String){
        username = newUsername
    }

    fun onPasswordChange(newPassword: String){
        password = newPassword
    }

}