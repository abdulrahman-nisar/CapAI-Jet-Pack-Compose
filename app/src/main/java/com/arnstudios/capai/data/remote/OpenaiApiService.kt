package com.arnstudios.capai.data.remote

import com.arnstudios.capai.domain.model.CapAI
import com.arnstudios.capai.domain.model.CaptionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenaiApiService {

    @POST(".")
    suspend fun openaiImageToCaption(
        @Body request: CaptionRequest
    ) : Response<CapAI>
}