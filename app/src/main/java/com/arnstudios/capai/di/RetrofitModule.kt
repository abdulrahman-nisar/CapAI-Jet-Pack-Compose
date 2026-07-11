package com.arnstudios.capshotai.di

import com.arnstudios.capshotai.data.remote.OpenaiApiService
import com.arnstudios.capshotai.data.remote.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun getOpenaiApiService() : OpenaiApiService{
        return RetrofitInstance.getOpenaiApiService()
    }
}