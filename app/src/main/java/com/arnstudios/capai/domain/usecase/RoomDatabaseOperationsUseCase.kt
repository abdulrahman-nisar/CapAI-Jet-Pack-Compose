package com.arnstudios.capshotai.domain.usecase

import android.net.Uri
import com.arnstudios.capshotai.domain.model.CapAI
import com.arnstudios.capshotai.domain.repository.CapAiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomDatabaseOperationsUseCase @Inject constructor(
    val  repository: CapAiRepository
) {
    suspend fun insertCapAI(capAI: CapAI, imageUri: Uri){
        return repository.insertCapAI(capAI,imageUri)
    }
    fun getAllCaptions() : Flow<List<CapAI>>{
        return repository.getAllCaptions()
    }

    suspend fun deleteCapAI(capAI: CapAI){
        repository.deleteCapAI(capAI)
    }
}