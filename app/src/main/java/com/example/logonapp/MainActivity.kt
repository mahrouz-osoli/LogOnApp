package com.example.logonapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.logonapp.data.datasourse.UserAuth
import com.example.logonapp.data.infrastructure.RetrofitInstance
import com.example.logonapp.presentaion.logon.Logon
import com.example.logonapp.presentaion.login.Login
import com.example.logonapp.presentaion.viewmodel.login.LoginViewModel
import com.example.logonapp.ui.theme.LogOnAppTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.logonapp.data.repository.Logon.LogonRepository
import com.example.logonapp.data.repository.Login.LoginRepository
import com.example.logonapp.presentaion.viewmodel.logon.LogonViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        RetrofitInstance.initialize(applicationContext)
        val userAuth = UserAuth(applicationContext)
        val loginRepository = LoginRepository(userAuth)
        val loginViewModel = LoginViewModel(loginRepository, userAuth)
        val logonRepository = LogonRepository()
        val logonViewModel = LogonViewModel(logonRepository)

        setContent {
            LogOnAppTheme {
            val navController = rememberNavController()
                val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (isLoggedIn) "logon" else "login",
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable("login"){
                            Login(viewModel = loginViewModel, onLoginSuccess = {
                                navController.navigate("logon"){
                                    popUpTo("login"){inclusive = true}
                                }
                            })
                        }
                        composable("logon"){
                            Logon(viewModel = logonViewModel, onLogonSuccess =
                                {
                                    navController.navigate("logon"){

                                    }
                                })
                        }
                }
            }
        }
    }
    }
}




