package com.arnstudios.capshotai.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private fun getClient() : OkHttpClient{
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }
    private fun getInstance(): Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://us-central1-capai-2dff5.cloudfunctions.net/caption/")
            .client(getClient())
            .build()
    }

    fun getOpenaiApiService() : OpenaiApiService{
        return getInstance().create(OpenaiApiService::class.java)
    }
}