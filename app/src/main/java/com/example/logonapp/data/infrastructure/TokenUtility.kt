package com.example.logonapp.data.infrastructure

import com.auth0.android.jwt.JWT


fun isTokenValid(token: String?): Boolean {
    if (token.isNullOrBlank()) return false
    return try {
        val jwt = JWT(token)
        !jwt.isExpired(0)
    } catch (e: Exception) {
        false
    }
}
fun getExpiresIn(token: String?): Long? {
    if (token.isNullOrBlank()) return null
    return try {
        val jwt = JWT(token)
        val now = System.currentTimeMillis()
        val exp = jwt.expiresAt?.time ?: return null
        (exp - now) / 1000
    } catch (e: Exception) {
        null
    }
}


