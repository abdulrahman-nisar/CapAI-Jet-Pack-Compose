package com.arnstudios.capai.di

import android.content.Context
import androidx.room.Room
import com.arnstudios.capai.data.local.database.CapAIDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context : Context) : CapAIDataBase{
        return Room.databaseBuilder(
            context,
            CapAIDataBase::class.java,
            "cap_ai_db"
        ).build()
    }

    @Provides
    fun provideCapAIDao(database: CapAIDataBase) = database.capAIDao()
}