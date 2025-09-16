package com.example.logonapp.data.datasourse

import android.content.Context
import android.media.session.MediaSession.Token
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn


private val Context.dataStore by preferencesDataStore("user_dataStore")

class UserAuth(private val context: Context) {
    private var cachedToken: String? = null

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    val tokenFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[TOKEN_KEY]
        }
        .onEach {
            cachedToken = it
        }
        .stateIn(CoroutineScope(Dispatchers.IO), SharingStarted.Eagerly, null)

    fun getCachedToken(): String? = cachedToken

    suspend fun saveToken(token: String){
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }

    suspend fun clearToken() {
        context.dataStore.edit { it.remove(TOKEN_KEY) }
    }
}