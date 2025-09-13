package com.example.logonapp.presentaion.viewmodel.login

import LoginRepository
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import com.example.logonapp.data.datasourse.UserAuth
import com.example.logonapp.data.model.error.LoginResult
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository = LoginRepository(), private val userAuth: UserAuth) : ViewModel() {

    var username by mutableStateOf("userTest")
        private set

    var password by mutableStateOf("123456")
        private set

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
                loginResult = response

                if (response is LoginResult.Success){
                    userAuth.saveToken(response.data.token)
                }
            }
            catch (e: Exception){
                loginResult = LoginResult.Error(e.message ?: "خطایی رخ داد")
            }
        }
    }
    val tokenFlow = userAuth.tokenFlow
}
