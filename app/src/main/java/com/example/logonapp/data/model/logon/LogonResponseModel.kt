package com.example.logonapp.data.model.logon

data class LogonResponseModel(
    val creationDate: String,
    val creatorUserId: String,
    val lastUpdate: String,
    val updaterUserId: String,
    val insId: String,
    val terminalId: String,
    val terminalType: String,
    val terminalStatus: Int,
    val terminalSerial: String,
    val project: String,
    val latLon: String,
    val centerTerminalId: String,
    val logonStatus: Int,
    val lastLogon: String,
    val forceLogon: Boolean
)