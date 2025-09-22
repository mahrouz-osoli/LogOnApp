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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class LoginViewModel(private val repository: LoginRepository, private val userAuth: UserAuth) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = combine(
        userAuth.token.map { token -> isTokenValid(userAuth) },
        userAuth.username,
        userAuth.password
    ) { tokenValid, username, password ->
        tokenValid && !username.isNullOrEmpty() && !password.isNullOrEmpty()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)


    var username by mutableStateOf("sayan_dev")
        private set

    var password by mutableStateOf("Sayan@123")
        private set

    var loginResult by mutableStateOf<LoginResult>(LoginResult.Idle)
        private set

    init {
        checkSavedCredentials()
    }

    private fun checkSavedCredentials() {
        viewModelScope.launch {
            val tokenValid = isTokenValid(userAuth)

            val savedUsername = userAuth.getUserName()
            val savedPassword = userAuth.getPassword()
            val savedToken = userAuth.getCachedToken()

            if (tokenValid && !savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty() && !savedToken.isNullOrEmpty()) {
                username = savedUsername
                password = savedPassword
                loginResult = LoginResult.Success(
                    LoginResponseModel(token = savedToken, name = savedUsername)
                )
            } else {
                loginResult = LoginResult.Idle
            }
        }
    }
        fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

//    suspend fun checkAndRefreshToken(): Boolean {
//        return repository.checkAndRefreshToken()
//    }


    fun login() {
        loginResult = LoginResult.Loading
        viewModelScope.launch {
            try {
                val response = repository.login(username, password)
                loginResult = response

                if (response is LoginResult.Success) {
                    try {
                        userAuth.saveToken(response.data.token)
                        userAuth.saveUser(username, password)
                        val savedToken = userAuth.getCachedToken()
                        userAuth.saveTokenTime(System.currentTimeMillis())
                        Log.d("LoginViewModel", "Token after save: $savedToken")
                    } catch (e: Exception) {
                        Log.e("LoginViewModel", "Error saving token", e)
                    }
                }
            } catch (e: Exception) {
                loginResult = LoginResult.Error(e.message ?: "خطایی رخ داد")
                Log.e("LoginViewModel", "Login failed: ${e.message}")
            }
        }
    }

    fun checkLoginStatus() {
        viewModelScope.launch {
            val valid = isTokenValid(userAuth)
            val savedUsername = userAuth.getUserName()
            val savedPassword = userAuth.getPassword()
            if (valid && !savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                loginResult = LoginResult.Success(LoginResponseModel(token = userAuth.getCachedToken() ?: "", name = savedUsername))
            } else {
                loginResult = LoginResult.Idle
            }
        }
    }

}
