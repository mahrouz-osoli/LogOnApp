package com.example.logonapp.data.datasourse

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull


private val Context.dataStore by preferencesDataStore("user_dataStore")

class UserAuth(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val TOKEN_TIME_KEY = longPreferencesKey("token_saved_time")
    }

    val token = context.dataStore.data
        .map { preferences -> preferences[TOKEN_KEY] }
        .filterNotNull()
        .flowOn(Dispatchers.IO)

 val username = context.dataStore.data
        .map { preferences -> preferences[USERNAME_KEY] }
        .filterNotNull()
        .flowOn(Dispatchers.IO)

 val password = context.dataStore.data
        .map { preferences -> preferences[PASSWORD_KEY] }
        .filterNotNull()
        .flowOn(Dispatchers.IO)

    val tokenSavedTime = context.dataStore.data
        .map { preferences -> preferences[TOKEN_TIME_KEY] }
        .filterNotNull()
        .flowOn(Dispatchers.IO)

    suspend fun getCachedToken(): String? = token.firstOrNull()
    suspend fun getUserName(): String? = username.firstOrNull()
    suspend fun getPassword(): String? = password.firstOrNull()
    suspend fun getTokenSavedTime(): Long? = tokenSavedTime.firstOrNull()

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }
    suspend fun saveUser(username: String, password: String){
        context.dataStore.edit {
            it[USERNAME_KEY] = username
            it[PASSWORD_KEY] = password
        }
    }

    suspend fun saveTokenTime(timeMillis: Long) {
        context.dataStore.edit {
            it[TOKEN_TIME_KEY] = timeMillis
        }
    }
}