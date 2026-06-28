package com.arnstudios.capai.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arnstudios.capai.data.local.dao.CapAIDao
import com.arnstudios.capai.data.local.entity.CaptionEntity

@Database(
    entities = [CaptionEntity::class],
    version = 1,
)
abstract class CapAIDataBase : RoomDatabase() {
    abstract fun capAIDao() : CapAIDao
}