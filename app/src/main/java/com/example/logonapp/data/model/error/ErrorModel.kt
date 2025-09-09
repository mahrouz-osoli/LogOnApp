package com.example.logonapp.data.model.error

data class ErrorModel(
    val errorId: String?,
    val errorCode: String?,
    val errorArgs: List<String>?,
    val httpStatus: String?
)
