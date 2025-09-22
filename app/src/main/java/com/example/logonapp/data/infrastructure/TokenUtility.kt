package com.example.logonapp.data.infrastructure

import com.example.logonapp.data.datasourse.UserAuth


suspend fun isTokenValid(userAuth: UserAuth): Boolean {
    val token = userAuth.getCachedToken()
    val tokenTime = userAuth.getTokenSavedTime()

    if (token.isNullOrEmpty() || tokenTime == null) return false

    val currentTime = System.currentTimeMillis()
    val validDuration = 5 * 60 * 1000
    return (currentTime - tokenTime) < validDuration
}

