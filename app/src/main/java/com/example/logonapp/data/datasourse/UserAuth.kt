package com.example.logonapp.data.datasourse

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first


private val Context.dataStore by preferencesDataStore("user_dataStore")

class UserAuth(private val context: Context) {
    private var cachedToken: String? = null
    private var cachedUsername: String? = null
    private var cachedPassword: String? = null

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val PASSWORD_KEY = stringPreferencesKey("password")
    }

    val tokenFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[TOKEN_KEY]
        }
        .onEach {
            cachedToken = it
        }
        .stateIn(CoroutineScope(Dispatchers.IO), SharingStarted.Eagerly, null)

    val usernameFlow: Flow<String?> = context.dataStore.data
        .map {
            preferences -> preferences[USERNAME_KEY]
        }
        .onEach {
            cachedUsername  = it
        }
        .stateIn(CoroutineScope(Dispatchers.IO), SharingStarted.Eagerly, null)

    val passwordFlow: Flow<String?> = context.dataStore.data
        .map {
                preferences -> preferences[PASSWORD_KEY]
        }
        .onEach {
            cachedPassword  = it
        }
        .stateIn(CoroutineScope(Dispatchers.IO), SharingStarted.Eagerly, null)



    fun getCachedToken(): String? = cachedToken

    suspend fun saveToken(token: String){
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }

    suspend fun clearToken() {
        context.dataStore.edit { it.remove(TOKEN_KEY) }
    }

    fun getUsernameSync(): String? = runBlocking {
        context.dataStore.data.map { it[USERNAME_KEY] }.first()
    }

    fun getPasswordSync(): String? = runBlocking {
        context.dataStore.data.map { it[PASSWORD_KEY] }.first()
    }

    suspend fun saveUser(username: String, password: String)
    {
        context.dataStore.edit {
            it[USERNAME_KEY] = username
            it[PASSWORD_KEY] = password
        }
    }
}