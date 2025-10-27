package com.example.logonapp.data.model.Terminal


data class TerminalsResponseModel(
    val insId: String,
    val terminalId: String,
    val terminalSerial: String
)

data class TerminalViewResponseModel(
    val result: List<TerminalsResponseModel>
)