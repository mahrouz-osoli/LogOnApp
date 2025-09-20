package com.example.logonapp.data.infrastructure

import android.content.Context
import android.util.Log
import com.example.logonapp.data.datasourse.UserAuth
import com.example.logonapp.data.service.Login.Interface.ILoginService
import com.example.logonapp.data.service.Logon.Interface.ILogonService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitInstance {
    private const val BASE_URL = "https://oa.avreenco.com:8080/api/"

    private lateinit var userAuth: UserAuth
    private lateinit var retrofit: Retrofit
    private lateinit var api: ILoginService
    private lateinit var logonApi: ILogonService

    fun initialize(context: Context) {
        userAuth = UserAuth(context)

        val okHttpClient = createOkHttpClient()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ILoginService::class.java)
        logonApi = retrofit.create(ILogonService::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .authenticator(TokenAuthenticator(userAuth, getLoginApiTemp()))
            .addInterceptor(AuthInterceptor { getTokenSync() })
            .cookieJar(CookieHandler())
            .addInterceptor(createLoggingInterceptor())
            .build()
    }

    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("OkHttp", message)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    private fun getLoginApiTemp(): ILoginService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ILoginService::class.java)
    }

    private fun getTokenSync(): String? = runBlocking {
        userAuth.token.firstOrNull()
    }

    fun getRetrofitInstance(): Retrofit = retrofit

    fun getApi(): ILoginService = api

    fun getLogonApi(): ILogonService = logonApi
}
