package com.example.logonapp.presentaion.logon

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.logonapp.data.model.error.LogonResult
import com.example.logonapp.presentaion.viewmodel.logon.LogonViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign


@Composable
fun Logon(
    viewModel: LogonViewModel,
    onLogonSuccess: () -> Unit,
    onLogout: () -> Unit
){
    val logonState by viewModel.logonResult.collectAsState()
    val showSuccessDialog by viewModel.showSuccessDialog.collectAsState()
    val terminal by viewModel.terminal.collectAsState()
    val serial by viewModel.serial.collectAsState()
    val radioSelected by viewModel.radioSelected.collectAsState()

    Box(modifier = Modifier.fillMaxSize()){
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "پروفایل",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            OutlinedButton(
                onClick = { onLogout() },
                modifier = Modifier.height(32.dp),
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "خروج",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("خروج", fontSize = 14.sp)
            }
        }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissSuccessDialog() },
            title = { Text("پیام موفق") },
            text = { Text("راه اندازی اولیه با موفقیت انجام شد!") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.dismissSuccessDialog()
                    onLogonSuccess()
                }) {
                    Text("باشه")
                }
            }
        )
    }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 72.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "به صفحه راه اندازی اولیه خوش آمدید",
                fontSize = 19.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                    "سریال دستگاه",
                    modifier = Modifier
                        .clickable { viewModel.selectRadio("serial") }
//                        .padding(start = 2.dp, end = 4.dp)
                    )
                    RadioButton(
                        selected = radioSelected == "serial",
                        onClick = { viewModel.selectRadio("serial") }
                    )


                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        "ترمینال",
                        modifier = Modifier
                            .clickable { viewModel.selectRadio("terminal") }
//                            .padding(start = 2.dp)
                    )
                    RadioButton(
                        selected = radioSelected == "terminal",
                        onClick = { viewModel.selectRadio("terminal") }
                    )

                }

                Spacer(modifier = Modifier.height(12.dp))

                if (radioSelected == "serial") {
                    TextField(
                        value = serial,
                        onValueChange = { viewModel.onSerialChange(it) },
                        label = { Text("شماره سریال را وارد کنید") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                } else if (radioSelected == "terminal") {
                    TextField(
                        value = terminal,
                        onValueChange = { viewModel.onTerminalChange(it) },
                        label = { Text("شماره ترمینال را وارد کنید") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

    Button(
        onClick = {
            viewModel.reInitLogon()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
    ) {
        Text("راه اندازی اولیه", color = Color.White, fontSize = 20.sp)
    }

    Spacer(modifier = Modifier.height(18.dp))

    when (logonState) {
        is LogonResult.Error -> {
            Text(
                text = (logonState as LogonResult.Error).message
                    ?: "خطا: ترمینالی یافت نشد یا سرور پاسخ نداد.",
                color = Color.Red,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        is LogonResult.Success -> {
            Text(
                text = "ورود موفق!",
                color = Color.Green,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        is LogonResult.Loading -> {
            Text(
                text = "در حال پردازش...",
                color = Color.Blue,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        else -> {}
    }
}}}