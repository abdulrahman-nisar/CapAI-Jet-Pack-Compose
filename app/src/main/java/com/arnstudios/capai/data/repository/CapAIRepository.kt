package com.arnstudios.capai.data.repository

import android.net.Uri
import androidx.core.net.toUri
import com.arnstudios.capai.data.local.dao.CapAIDao
import com.arnstudios.capai.data.local.entity.CaptionEntity
import com.arnstudios.capai.data.remote.OpenaiApiService
import com.arnstudios.capai.domain.model.CapAI
import com.arnstudios.capai.domain.model.CaptionRequest
import com.arnstudios.capai.domain.repository.CapAiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject

class CapAIRepositoryImpl @Inject constructor(
    private val _openai : OpenaiApiService,
    private val dao : CapAIDao
) : CapAiRepository {

    override suspend fun getImageCaption(request: CaptionRequest) : Response<CapAI> {
        return _openai.openaiImageToCaption(request)
    }

    override suspend fun insertCapAI(capAI: CapAI, imageUri : Uri) {
        dao.addCaption(CaptionEntity(
            imageUri = imageUri.toString(),
            timestamp = System.currentTimeMillis(),
            id = 0,
            instagramCaption = capAI.instagramCaption!!,
            facebookCaption = capAI.facebookCaption!!,
            twitterCaption = capAI.twitterCaption!!,
            pinterestCaption = capAI.pinterestCaption!!,
            linkedinCaption = capAI.linkedinCaption!!,
            threadCaption = capAI.threadCaption!!,
            snapChatCaption = capAI.snapChatCaption!!,
            tiktokCaption = capAI.tiktokCaption!!,
        ))
    }

    override fun getAllCaptions(): Flow<List<CapAI>> {
        return dao.getAllCaptions().map { list ->
            list.map { entity ->
                CapAI(
                    id = entity.id,
                    imageUri = entity.imageUri.toUri(),
                    instagramCaption = entity.instagramCaption,
                    facebookCaption = entity.facebookCaption,
                    twitterCaption = entity.twitterCaption,
                    pinterestCaption = entity.pinterestCaption,
                    linkedinCaption = entity.linkedinCaption,
                    threadCaption = entity.threadCaption,
                    snapChatCaption = entity.snapChatCaption,
                    tiktokCaption = entity.tiktokCaption
                )
            }
        }
    }

    override suspend fun deleteCapAI(capAI: CapAI) {
        dao.deleteCapAI(capAI.id)
    }

}