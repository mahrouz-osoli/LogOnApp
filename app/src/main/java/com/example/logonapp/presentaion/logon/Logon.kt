package com.example.logonapp.presentaion.logon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.logonapp.data.model.error.LogonResult
import com.example.logonapp.presentaion.viewmodel.logon.LogonViewModel


@Composable
fun Logon(viewModel: LogonViewModel,onLogonSuccess: () -> Unit){
    val logonState by remember { derivedStateOf { viewModel.logonResult} }
    val instId = viewModel.instId
    val terminal = viewModel.terminal

Column (
    modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
    verticalArrangement = Arrangement.Center
) {
    TextField(
        value = instId,
        onValueChange = { viewModel.onSerialChange(it) },
        label = { Text("Please Enter Serial") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(20.dp))

    TextField(
        value = terminal,
        onValueChange = { viewModel.onTerminalChange(it) },
        label = { Text("Please Enter Terminal") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(64.dp))

    Button(
      onClick = {viewModel.reInitLogon()},
        modifier = Modifier
                .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
    ){
        Text("Logon", color = Color.White, fontSize = 20.sp)
    }
    Spacer(modifier = Modifier.height(16.dp))

    when(logonState){
        is LogonResult.Error -> {
            Text(
                text = (logonState as LogonResult.Error).message,
                color = Color.Red,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
        is LogonResult.Success -> {
            LaunchedEffect(Unit){
                onLogonSuccess()
            }
            Text(
                text = "Logon successful!",
                color = Color.Green,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
        is LogonResult.Loading -> {
            Text(
                text = "Logon...",
                color = Color.Blue,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
        else -> {}

    }
}

}