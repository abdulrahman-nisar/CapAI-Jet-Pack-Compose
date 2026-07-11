package com.arnstudios.capshotai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arnstudios.capshotai.data.local.entity.CaptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CapAIDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addCaption(entity : CaptionEntity)

    @Query("SELECT * FROM captions ORDER BY timestamp DESC")
    abstract fun getAllCaptions() : Flow<List<CaptionEntity>>

    @Query("DELETE FROM captions WHERE id = :id")
    abstract suspend fun deleteCapAI(id: Int)
}