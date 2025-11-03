package com.example.logonapp.presentaion.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logonapp.data.model.error.LoginResult
import com.example.logonapp.presentaion.login.viewModel.LoginViewModel

@Composable
fun Login(viewModel: LoginViewModel = viewModel(), onLoginSuccess: () -> Unit) {
    val context = LocalContext.current

    val loginState = viewModel.loginResult
    val isLoading = viewModel.isLoading
    val username = viewModel.username
    val password = viewModel.password

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginResult.Success -> {
                Toast.makeText(context, "ورود موفق ✅", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
                onLoginSuccess()
            }

            is LoginResult.Error -> {
                Toast.makeText(context, (loginState as LoginResult.Error).message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF0B2545))
                )
            )
            .padding(horizontal = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 55.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "",
                fontSize = 20.sp,
                color = Color(0xFFe6eef8),
                modifier = Modifier.padding(bottom = 15.dp)
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFeaf2ff).copy(alpha = 0.08f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ورود به حساب کاربری",
                        fontSize = 18.sp,
                        color = Color(0xFFe6eef8),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = username,
                        onValueChange = { viewModel.onUsernameChange(it) },
                        label = { Text("نام کاربری") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = Color(0xFFe6eef8),
                            unfocusedTextColor = Color(0xFFe6eef8),
                            focusedIndicatorColor = Color(0xFF82B1FF),
                            unfocusedIndicatorColor = Color(0xFF33506E),
                            focusedLabelColor = Color(0xFFe6eef8),
                            unfocusedLabelColor = Color(0xFFc9d6ea)
                        )
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { viewModel.onPasswordChange(it) },
                        label = { Text("رمز عبور") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = Color(0xFFe6eef8),
                            unfocusedTextColor = Color(0xFFe6eef8),
                            focusedIndicatorColor = Color(0xFF82B1FF),
                            unfocusedIndicatorColor = Color(0xFF33506E),
                            focusedLabelColor = Color(0xFFe6eef8),
                            unfocusedLabelColor = Color(0xFFc9d6ea)
                        )
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Button(
                        onClick = { viewModel.login() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.DarkGray,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        } else {
                            Text("ورود", color = Color.DarkGray, fontSize = 18.sp)
                        }
                    }

                    if (loginState is LoginResult.Error) {
                        Text(
                            text = (loginState as LoginResult.Error).message
                                ?: "نام کاربری یا رمز اشتباه است!",
                            color = Color(0xFFFF6B6B),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }
        }
    }
}
