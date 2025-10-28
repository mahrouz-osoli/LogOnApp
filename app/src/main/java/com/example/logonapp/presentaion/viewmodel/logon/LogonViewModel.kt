package com.example.logonapp.presentaion.viewmodel.logon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logonapp.data.datasourse.UserAuth
import com.example.logonapp.data.model.error.LogonResult
import com.example.logonapp.data.repository.Logon.LogonRepository
import com.example.logonapp.data.repository.Terminal.TerminalRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LogonViewModel(
    private val repository: LogonRepository,
    private val terminalRepository: TerminalRepository,
    private val userAuth: UserAuth
) : ViewModel() {

    private val _terminal = MutableStateFlow("")
    val terminal: StateFlow<String> get() = _terminal

    private val _serial = MutableStateFlow("")
    val serial: StateFlow<String> get() = _serial

    private val _radioSelected = MutableStateFlow("serial")
    val radioSelected: StateFlow<String> get() = _radioSelected

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _uiMessage = MutableSharedFlow<String>(replay = 0)
    val uiMessage = _uiMessage.asSharedFlow()

    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog: StateFlow<Boolean> get() = _showSuccessDialog

    private val _logonResult = MutableStateFlow<LogonResult>(LogonResult.Idle)
    val logonResult: StateFlow<LogonResult> get() = _logonResult

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
        _isLoading.value = true
        _logonResult.value = LogonResult.Idle

        viewModelScope.launch {
            try {
                val selectedTerminal = if (_radioSelected.value == "serial") {
                    val serialVal = _serial.value
                    if (serialVal.isBlank()) {
                        _uiMessage.emit("سریال را وارد کنید.")
                        _isLoading.value = false
                        _logonResult.value = LogonResult.Error("سریال خالی است")
                        return@launch
                    }
                    val terminalResult = terminalRepository.getTerminalBySerial(serialVal)
                    if (terminalResult.isSuccess) {
                        terminalResult.getOrNull()!!
                    } else {
                        val err = terminalResult.exceptionOrNull()?.message ?: "ترمینال یافت نشد."
                        _uiMessage.emit(err)
                        _isLoading.value = false
                        _logonResult.value = LogonResult.Error(err)
                        return@launch
                    }
                } else {
                    val termVal = _terminal.value
                    if (termVal.isBlank()) {
                        _uiMessage.emit("ترمینال را وارد کنید.")
                        _isLoading.value = false
                        _logonResult.value = LogonResult.Error("ترمینال خالی است")
                        return@launch
                    }
                    termVal
                }

                val response = repository.logon(selectedTerminal, instId)
                _logonResult.value = response

                if (response is LogonResult.Success) {
                    _showSuccessDialog.value = true
                    _uiMessage.emit("راه‌اندازی با موفقیت انجام شد.")
                } else if (response is LogonResult.Error) {
                    _uiMessage.emit(response.message ?: "خطا در راه‌اندازی")
                }

            } catch (e: Exception) {
                val message = e.message ?: "خطایی رخ داد"
                _uiMessage.emit(message)
                _logonResult.value = LogonResult.Error(message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetState() {
        _logonResult.value = LogonResult.Idle
        _isLoading.value = false
        _showSuccessDialog.value = false
    }

    fun logout() {
        viewModelScope.launch {
            try {
                userAuth.clearCredentials()
                resetState()
            } catch (_: Exception) { }
        }
    }
}
