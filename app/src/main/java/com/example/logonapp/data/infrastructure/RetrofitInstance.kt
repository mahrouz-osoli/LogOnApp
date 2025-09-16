package com.example.logonapp.data.infrastructure

import android.content.Context
import com.example.logonapp.data.datasourse.UserAuth
import com.example.logonapp.data.service.Login.Interface.ILoginService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

object RetrofitInstance {
    private const val BASE_URL = "https://oa.avreenco.com:8080/api/"

    private lateinit var userAuth: UserAuth
    private lateinit var retrofit: Retrofit
    private lateinit var api: ILoginService

    fun initialize(context: Context) {
        userAuth = UserAuth(context)
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ILoginService::class.java)

        val okHttpClient = OkHttpClient.Builder()
            .authenticator(TokenAuthenticator(userAuth, api))
            .addInterceptor(AuthInterceptor { getTokenSync() })
            .build()

        retrofit = retrofit.newBuilder()
            .client(okHttpClient)
            .build()
    }

    private fun getTokenSync(): String? = runBlocking {
        userAuth.token.firstOrNull()
    }

    fun getRetrofitInstance(): Retrofit = retrofit

    fun getApi(): ILoginService = api
}
