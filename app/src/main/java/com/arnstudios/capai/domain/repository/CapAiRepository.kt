package com.arnstudios.capai.domain.repository

import android.net.Uri
import com.arnstudios.capai.domain.model.CapAI
import com.arnstudios.capai.domain.model.CaptionRequest
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CapAiRepository {
    suspend fun getImageCaption(request: CaptionRequest) : Response<CapAI>
    suspend fun insertCapAI(capAI: CapAI, imageUri: Uri)
    fun getAllCaptions() : Flow<List<CapAI>>

    suspend fun deleteCapAI(capAI: CapAI)
}