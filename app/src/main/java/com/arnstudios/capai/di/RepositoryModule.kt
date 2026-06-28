package com.arnstudios.capai.di

import com.arnstudios.capai.data.remote.OpenaiApiService
import com.arnstudios.capai.data.remote.RetrofitInstance
import com.arnstudios.capai.data.repository.CapAIRepositoryImpl
import com.arnstudios.capai.domain.repository.CapAiRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindCapAiRepository(
        impl : CapAIRepositoryImpl
    ): CapAiRepository

}