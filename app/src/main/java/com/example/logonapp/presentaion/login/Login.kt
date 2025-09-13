package com.example.logonapp.presentaion.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logonapp.data.model.error.LoginResult
import com.example.logonapp.presentaion.viewmodel.login.LoginViewModel


@Composable
fun Login(viewModel: LoginViewModel = viewModel()) {

val loginState by remember { derivedStateOf { viewModel.loginResult } }
val token by viewModel.tokenFlow.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = viewModel.username,
            onValueChange = { viewModel.onUsernameChange(it)
            },
            label = {
                Text(
                    text = "Please enter username"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is LoginResult.Loading
        )

        Spacer(modifier = Modifier.height((16.dp)))

        TextField(
            value = viewModel.password,
            onValueChange = {viewModel.onPasswordChange(it)},
            label = { Text("Please enter password")
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            enabled = loginState !is LoginResult.Loading
        )

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = {viewModel.login()},
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
            enabled = loginState !is LoginResult.Loading
        ) {
            Text("Login", color = Color.White, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loginState is LoginResult.Error){
            Text(
                text = (loginState as LoginResult.Error).message,
                color = Color.Red,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if(loginState is LoginResult.Success){
            Text(
                text = "Login successful!",
                color = Color.Green,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview(){
    Login()
}

