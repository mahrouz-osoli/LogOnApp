package com.example.logonapp.data.datasourse

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.Preferences

private val Context.dataStore by preferencesDataStore("user_dataStore")

class UserAuth(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val TOKEN_TIME_KEY = longPreferencesKey("token_saved_time")
        private val EXPLICIT_LOGOUT_KEY = booleanPreferencesKey("explicit_logout")
    }

    val token: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[TOKEN_KEY] }
        .flowOn(Dispatchers.IO)

    val username: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USERNAME_KEY] }
        .flowOn(Dispatchers.IO)

    val password: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[PASSWORD_KEY] }
        .flowOn(Dispatchers.IO)

    suspend fun getCachedToken(): String? = token.firstOrNull()
    suspend fun getUserName(): String? = username.firstOrNull()
    suspend fun getPassword(): String? = password.firstOrNull()

    suspend fun saveToken(token: String) {
        context.dataStore.edit {
            it[TOKEN_KEY] = token
            it[EXPLICIT_LOGOUT_KEY] = false
        }
    }
    suspend fun saveUser(username: String, password: String){
        context.dataStore.edit {
            it[USERNAME_KEY] = username
            it[PASSWORD_KEY] = password
            it[EXPLICIT_LOGOUT_KEY] = false
        }
    }

    suspend fun saveTokenTime(timeMillis: Long) {
        context.dataStore.edit {
            it[TOKEN_TIME_KEY] = timeMillis
        }
    }

    suspend fun clearCredentials() {
        context.dataStore.edit {
            it.remove(TOKEN_KEY)
            it.remove(USERNAME_KEY)
            it.remove(PASSWORD_KEY)
            it.remove(TOKEN_TIME_KEY)
            it[EXPLICIT_LOGOUT_KEY] = true
        }
    }

    fun isLoggedInFlow(): Flow<Boolean> {
        return context.dataStore.data
            .map { prefs ->
                val t = prefs[TOKEN_KEY]
                val u = prefs[USERNAME_KEY]
                val p = prefs[PASSWORD_KEY]
                val explicitLogout = prefs[EXPLICIT_LOGOUT_KEY] ?: false
                !t.isNullOrEmpty() && !u.isNullOrEmpty() && !p.isNullOrEmpty() && !explicitLogout
            }
            .flowOn(Dispatchers.IO)
    }
}
