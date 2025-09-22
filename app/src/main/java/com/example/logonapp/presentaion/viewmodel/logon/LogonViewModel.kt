package com.example.logonapp.presentaion.viewmodel.logon

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logonapp.data.model.error.LogonResult
import com.example.logonapp.data.repository.Logon.LogonRepository
import kotlinx.coroutines.launch


class LogonViewModel(private val repository: LogonRepository) : ViewModel(){

    var instId by mutableStateOf("")
        private set

    var terminal by mutableStateOf("41112596")
        private set

    var logonResult by mutableStateOf<LogonResult>(LogonResult.Idle)
        private set

    var showSuccessDialog by mutableStateOf(false)
        private set

    fun onSerialChange(newSerial: String){
        instId = newSerial
    }
    fun onTerminalChange(newTerminal: String){
        terminal = newTerminal
    }

    fun dismissSuccessDialog() {
        showSuccessDialog = false
        logonResult = LogonResult.Idle
    }

    fun reInitLogon(){
        logonResult = LogonResult.Loading
        viewModelScope.launch{
                try {
                    val response = repository.logon(terminal,instId)
                    logonResult = response
                    if (response is LogonResult.Success) {
                        try {
                            if (!showSuccessDialog) {
                                showSuccessDialog = true
                            }
                        } catch (e: Exception) {
                            Log.e("LogonViewModel", "Error", e)
                        }
                    }
                }
                catch (e: Exception) {
                    logonResult = LogonResult.Error(e.message ?: "خطایی رخ داد")
                    Log.e("LogonViewModel", "Logon failed: ${e.message}")
                }

        }
    }
}