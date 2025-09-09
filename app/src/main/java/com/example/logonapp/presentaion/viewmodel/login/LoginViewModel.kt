package com.example.logonapp.presentaion.login

import LoginRepository
import LoginResponseModel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import com.example.logonapp.data.model.error.LoginResult
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository = LoginRepository()) : ViewModel() {

    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

//    var errorMessage by mutableStateOf("")
//        private set
//
//    var loginResponse by mutableStateOf<LoginResponseModel?>(null)
//        private set

    var loginResult by mutableStateOf<LoginResult>(LoginResult.Idle)
         private set

    fun onUsernameChange(newUsername: String){
        username = newUsername
    }

    fun onPasswordChange(newPassword: String){
        password = newPassword
    }


    fun login(){
        loginResult = LoginResult.Loading
        viewModelScope.launch {
            try {
            val response = repository.login(username,password)
                loginResult = LoginResult.Success(response)
            }
            catch (e: Exception){
                loginResult = LoginResult.Error(e.message ?: "خطایی رخ داد")
            }
        }
    }

}
