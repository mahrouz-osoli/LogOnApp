package com.example.logonapp.presentaion.login

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
import com.example.logonapp.presentaion.login.LoginViewModel
//import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Login(viewModel: LoginViewModel = viewModel()) {

//    var username by remember {
//        mutableStateOf("")
//    }
//    var password by remember {
//        mutableStateOf("")
//    }

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
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height((16.dp)))

        TextField(
            value = viewModel.password,
            onValueChange = {viewModel.onPasswordChange(it)},
            label = { Text("Please enter password")
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = {},
            modifier = Modifier

                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Login", color = Color.White, fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview(){
    Login()
}

