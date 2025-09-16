package com.example.logonapp.data.datasourse

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.firstOrNull


private val Context.dataStore by preferencesDataStore("user_dataStore")

class UserAuth(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val PASSWORD_KEY = stringPreferencesKey("password")
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

    fun getCachedToken(): String? = token.toString()
    suspend fun getUserName(): String? = username.firstOrNull()
    suspend fun getPassword(): String? = password.firstOrNull()

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }
    suspend fun saveUser(username: String, password: String){
        context.dataStore.edit {
            it[USERNAME_KEY] = username
            it[PASSWORD_KEY] = password
        }
    }

//        suspend fun saveUsername(username: String) {
//        context.dataStore.edit { it[USERNAME_KEY] = username }
//    }
//    suspend fun savePassword(password: String) {
//        context.dataStore.edit { it[PASSWORD_KEY] = password }
//    }

    suspend fun clearToken() {
        context.dataStore.edit { it.remove(TOKEN_KEY) }
    }
    suspend fun clearUserAndPass() {
        context.dataStore.edit {
            it.remove(USERNAME_KEY)
            it.remove(PASSWORD_KEY)
        }
    }
}