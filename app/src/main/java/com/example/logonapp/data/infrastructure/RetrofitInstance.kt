package com.example.logonapp.data.infrastructure

import android.content.Context
import com.example.logonapp.data.datasourse.UserAuth
import com.example.logonapp.data.service.Login.Interface.ILoginService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance{

    private const val BASE_URL = "https://oa.avreenco.com:8080/api/"
    private lateinit var userAuth: UserAuth

    fun initialize(context: Context){
        userAuth = UserAuth(context)
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(
                tokenProvider = { getTokenSync() },
            ))
            .build()
    }

    private fun getTokenSync(): String? {
        return userAuth.getCachedToken()
    }

    private val  retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRetrofitInstance(): Retrofit = retrofit

    val api: ILoginService by lazy {
        retrofit.create(ILoginService::class.java)
    }
}