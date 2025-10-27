package com.example.logonapp.presentaion.viewmodel.logon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logonapp.data.datasourse.UserAuth
import com.example.logonapp.data.model.error.LogonResult
import com.example.logonapp.data.repository.Logon.LogonRepository
import com.example.logonapp.data.repository.Terminal.TerminalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class LogonViewModel(
    private val repository: LogonRepository,
    private val terminalRepository: TerminalRepository,
    private val userAuth: UserAuth
) : ViewModel(){

    private val _terminal = MutableStateFlow("")
    val terminal: StateFlow<String> get() = _terminal

    private val _serial = MutableStateFlow("")
    val serial: StateFlow<String> get() = _serial

    private val _radioSelected = MutableStateFlow("serial")
    val radioSelected: StateFlow<String> get() = _radioSelected

    private val _logonResult = MutableStateFlow<LogonResult>(LogonResult.Idle)
    val logonResult: StateFlow<LogonResult> get() = _logonResult

    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog: StateFlow<Boolean> get() = _showSuccessDialog

    private val instId = "581672081"

    fun selectRadio(type: String) {
        _radioSelected.value = type
    }

    fun onSerialChange(newSerial: String) {
        _serial.value = newSerial
    }
    fun onTerminalChange(newTerminal: String) {
        _terminal.value = newTerminal
    }

    fun dismissSuccessDialog() {
        _showSuccessDialog.value = false
        _logonResult.value = LogonResult.Idle
    }

    fun reInitLogon() {
        _logonResult.value = LogonResult.Loading
        viewModelScope.launch {
            try {
                val selectedTerminal =
                    if (radioSelected.value == "serial") {

                        val serial = serial.value
                        if (serial.isBlank()) {
                            _logonResult.value = LogonResult.Error("سریال را وارد کنید.")
                            return@launch
                        }
                        val terminalResult = terminalRepository.getTerminalBySerial(serial)
                        if (terminalResult.isSuccess) {
                            terminalResult.getOrNull()!!
                        } else {
                            _logonResult.value = LogonResult.Error(terminalResult.exceptionOrNull()?.message ?: "ترمینال یافت نشد.")
                            return@launch
                        }
                    } else {
                        val terminal = terminal.value
                        if (terminal.isBlank()) {
                            _logonResult.value = LogonResult.Error("ترمینال را وارد کنید.")
                            return@launch
                        }
                        terminal
                    }

                val response = repository.logon(selectedTerminal, instId)
                _logonResult.value = response
                if (response is LogonResult.Success) {
                    _showSuccessDialog.value = true
                }
            } catch (e: Exception) {
                _logonResult.value = LogonResult.Error(e.message ?: "خطایی رخ داد")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userAuth.clearCredentials()
        }
    }
}

