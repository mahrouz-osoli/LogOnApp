package com.example.logonapp.presentaion.viewmodel.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import com.example.logonapp.data.datasourse.UserAuth
import com.example.logonapp.data.model.error.LoginResult
import kotlinx.coroutines.launch
import android.util.Log
import com.example.logonapp.data.infrastructure.isTokenValid
import com.example.logonapp.data.model.login.LoginResponseModel
import com.example.logonapp.data.repository.Login.LoginRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class LoginViewModel(
    private val repository: LoginRepository,
    private val userAuth: UserAuth
) : ViewModel() {

    val isLoggedIn = userAuth.isLoggedInFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    var username by mutableStateOf("sayan_dev")
        private set

    var password by mutableStateOf("Sayan@123")
        private set

    var loginResult by mutableStateOf<LoginResult>(LoginResult.Idle)
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            val savedUsername = userAuth.getUserName()
            val savedPassword = userAuth.getPassword()
            if (!savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                username = savedUsername
                password = savedPassword
            }
        }
    }

    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun login() {
        loginResult = LoginResult.Loading
        isLoading = true

        viewModelScope.launch {
            try {
                val response = repository.login(username, password)
                loginResult = response

                if (response is LoginResult.Success) {
                    try {
                        userAuth.saveToken(response.data.token)
                        userAuth.saveUser(username, password)
                        userAuth.saveTokenTime(System.currentTimeMillis())
                        Log.d("LoginViewModel", "Token saved: ${response.data.token}")
                    } catch (e: Exception) {
                        Log.e("LoginViewModel", "Error saving token", e)
                    }
                }
            } catch (e: Exception) {
                loginResult = LoginResult.Error(e.message ?: "خطایی رخ داد")
                Log.e("LoginViewModel", "Login failed: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun resetState() {
        loginResult = LoginResult.Idle
        isLoading = false
    }

}
