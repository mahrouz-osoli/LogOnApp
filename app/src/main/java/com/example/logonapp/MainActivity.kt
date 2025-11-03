package com.example.logonapp

import androidx.compose.material3.*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logonapp.data.datasourse.UserAuth
import com.example.logonapp.data.infrastructure.RetrofitInstance
import com.example.logonapp.presentaion.logon.Logon
import com.example.logonapp.presentaion.login.Login
import com.example.logonapp.presentaion.login.viewModel.LoginViewModel
import com.example.logonapp.ui.theme.LogOnAppTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.logonapp.data.repository.Logon.LogonRepository
import com.example.logonapp.data.repository.Login.LoginRepository
import com.example.logonapp.data.repository.Terminal.TerminalRepository
import com.example.logonapp.presentaion.logon.viewModel.LogonViewModel
import com.example.logonapp.presentaion.mainMenu.MainMenuScreenContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        RetrofitInstance.initialize(applicationContext)

        val userAuth = UserAuth(applicationContext)
        val loginRepository = LoginRepository(userAuth)
        val loginViewModel = LoginViewModel(loginRepository, userAuth)
        val logonRepository = LogonRepository()
        val terminalRepository = TerminalRepository(RetrofitInstance.getTerminalViewApi())
        val logonViewModel = LogonViewModel(logonRepository, terminalRepository, userAuth)

        setContent {
            LogOnAppTheme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                val isLoggedIn by loginViewModel.isLoggedIn.collectAsState(initial = false)
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStackEntry?.destination?.route ?: ""

                val isDrawerEnabled = currentDestination != "mainMenu" && currentDestination != "login"

                LaunchedEffect(currentDestination) {
                    drawerState.close()
                }

                val menuItems = listOf(
                    "راه اندازی لاگان" to "logon",
                    "لاگان اجباری" to "mandatoryLogon",
                    "بایند سریال" to "bindSerial",
                    "آنبایند سریال" to "unbindSerial",
                    "بایند سیم کارت" to "bindSim",
                    "آنبایند سیم کارت" to "unbindSim",
                    "بایند کد شاپرکی" to "bindShaparak",
                    "آنبایند کد شاپرکی" to "unbindShaparak",
                    "فعال کردن ترمینال" to "activateTerminal",
                    "غیرفعال کردن ترمینال" to "deactivateTerminal"
                )

                val pageTitle = when(currentDestination) {
                    "login" -> "به اپلیکیشن راه‌اندازی اولیه خوش آمدید"
                    "mainMenu" -> "منوی اصلی"
                    "logon" -> "راه اندازی اولیه"
                    "mandatoryLogon" -> "لاگان اجباری"
                    "bindSerial" -> "بایند سریال دستگاه"
                    "unbindSerial" -> "آنبایند سریال دستگاه"
                    "bindSim" -> "بایند سیم کارت دستگاه"
                    "unbindSim" -> "آنبایند سیم کارت دستگاه"
                    "bindShaparak" -> "بایند کد شاپرکی"
                    "unbindShaparak" -> "آنبایند کد شاپرکی"
                    "activateTerminal" -> "فعال کردن ترمینال"
                    "deactivateTerminal" -> "غیرفعال کردن ترمینال"
                    else -> ""
                }

//                val isDrawerEnabled = currentDestination != "mainMenu" && isLoggedIn

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = isDrawerEnabled,
                    drawerContent = {
                        if (isDrawerEnabled) {
                            ModalDrawerSheet(
                                modifier = Modifier
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(Color(0xFF0F172A), Color(0xFF0B2545))
                                        )
                                    )
                            )
                            {
                                Text(
                                text = "منوی اپلیکیشن",
                                fontSize = 22.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(20.dp)
                                )
                                menuItems.forEach { (title, route) ->
                                    NavigationDrawerItem(
                                        label = { Text(title) },
                                        selected = currentDestination == route,
                                        onClick = {
                                            scope.launch { drawerState.close() }
                                            navController.navigate(route) {
                                                launchSingleTop = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = { Text(pageTitle, color = Color(0xFFE6EEF8)) },
                                navigationIcon = {
                                    if (isDrawerEnabled) {
                                        IconButton(onClick = { scope.launch { drawerState.open() }}) {
                                            Icon(Icons.Default.Menu, contentDescription = "منو", tint = Color(0xFFE6EEF8))
                                        }
                                    } else if (isLoggedIn && currentDestination != "login") {
//                                        IconButton(onClick = { navController.popBackStack() }) {
//                                            Icon(Icons.Default.ArrowBack, contentDescription = "بازگشت")
//                                        }
                                    }
                                },
                                actions = {
                                    if (isLoggedIn && currentDestination != "login") {
                                        IconButton(onClick = {
                                            scope.launch {
                                                logonViewModel.logout()
                                                navController.navigate("login") {
                                                    popUpTo(navController.graph.startDestinationId){ inclusive = true }
                                                }
                                            }
                                        }) {
                                            Icon(Icons.Default.ExitToApp, contentDescription = "خروج", tint = Color(0xFFE6EEF8))
                                        }
                                    }
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = Color(0xFF0F172A)
                                )
                            )
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = if (isLoggedIn) "mainMenu" else "login",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("login") {
                                Login(
                                    viewModel = loginViewModel,
                                    onLoginSuccess = {
                                        navController.navigate("mainMenu") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("mainMenu") {
                                MainMenuScreenContent(
                                    menuItems = menuItems,
                                    onItemClicked = { route ->
                                        scope.launch {
                                            drawerState.close()
                                            navController.navigate(route) { launchSingleTop = true }
                                        }
                                    }
                                )
                            }


                            composable("logon") {
                                Logon(
                                    viewModel = logonViewModel,
                                    onLogonSuccess = {},
                                    onLogout = {
                                        scope.launch {
                                            logonViewModel.logout()
                                            navController.navigate("login") {
                                                popUpTo("logon") { inclusive = true }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }}}



