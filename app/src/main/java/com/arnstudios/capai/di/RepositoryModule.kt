package com.arnstudios.capshotai.di

import com.arnstudios.capshotai.data.repository.CapAIRepositoryImpl
import com.arnstudios.capshotai.domain.repository.CapAiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindCapAiRepository(
        impl : CapAIRepositoryImpl
    ): CapAiRepository

}