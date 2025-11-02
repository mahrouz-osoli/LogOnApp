package com.example.logonapp.presentaion.logon

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collectLatest
import com.example.logonapp.presentaion.viewmodel.logon.LogonViewModel

@Composable
fun Logon(
    viewModel: LogonViewModel,
    onLogonSuccess: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    val isLoading by viewModel.isLoading.collectAsState()
    val showSuccessDialog by viewModel.showSuccessDialog.collectAsState()
    val terminal by viewModel.terminal.collectAsState()
    val serial by viewModel.serial.collectAsState()
    val radioSelected by viewModel.radioSelected.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiMessage.collectLatest { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
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
            .padding(10.dp)
    ) {
        TextButton(
            onClick = { onLogout() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 12.dp, start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ExitToApp,
                contentDescription = "خروج",
                modifier = Modifier.size(28.dp),
                tint = Color(0xFFe6eef8)
            )
            Spacer(Modifier.width(6.dp))
//            Text(
//                text = "خروج",
//                fontSize = 16.sp,
//                color = Color(0xFFe6eef8)
//            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.95f),
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 10.dp,
            color = Color(0xFFeaf2ff).copy(alpha = 0.08f)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 26.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = ":برای راه‌اندازی اولیه، یکی از روش‌ها را انتخاب کنید",
                    fontSize = 14.sp,
                    color = Color(0xFFc9d6ea),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 0.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Text(
                            text = "سریال دستگاه",
                            modifier = Modifier.clickable { viewModel.selectRadio("serial") },
                            color = Color(0xFFe6eef8),
                            fontSize = 15.sp,
                        )
                        RadioButton(
                            selected = radioSelected == "serial",
                            onClick = { viewModel.selectRadio("serial") }
                        )

                    }

                    Spacer(modifier = Modifier.width(18.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Text(
                            text = "ترمینال",
                            modifier = Modifier.clickable { viewModel.selectRadio("terminal") },
                            color = Color(0xFFe6eef8),
                            fontSize = 15.sp
                        )
                        RadioButton(
                            selected = radioSelected == "terminal",
                            onClick = { viewModel.selectRadio("terminal") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (radioSelected == "serial") {
                    OutlinedTextField(
                        value = serial,
                        onValueChange = { viewModel.onSerialChange(it) },
                        label = { Text("شماره سریال") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = Color(0xFF82B1FF),
                            unfocusedIndicatorColor = Color(0xFF33506E),
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color(0xFFc9d6ea)
                        )
                    )
                } else {
                    OutlinedTextField(
                        value = terminal,
                        onValueChange = { viewModel.onTerminalChange(it) },
                        label = { Text("شماره ترمینال") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = Color(0xFF82B1FF),
                            unfocusedIndicatorColor = Color(0xFF33506E),
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color(0xFFc9d6ea)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.reInitLogon() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9D9D9))
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("در حال پردازش...", color = Color.DarkGray)
                    } else {
                        Text("راه‌اندازی اولیه", color = Color.DarkGray, fontSize = 16.sp)
                    }
                }
            }
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissSuccessDialog() },
                title = { Text("موفقیت ✅") },
                text = { Text("راه‌اندازی با موفقیت انجام شد!") },
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
    }
}
