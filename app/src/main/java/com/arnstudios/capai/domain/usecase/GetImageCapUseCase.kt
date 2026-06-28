package com.arnstudios.capai.domain.usecase

import android.content.Context
import android.net.Uri
import com.arnstudios.capai.domain.model.CapAI
import com.arnstudios.capai.domain.model.CaptionRequest
import com.arnstudios.capai.domain.model.Length
import com.arnstudios.capai.domain.repository.CapAiRepository
import com.arnstudios.capai.utils.toBase64
import com.arnstudios.capai.utils.toBitmap
import javax.inject.Inject

class GetImageCapUseCase @Inject constructor(
   val  repository: CapAiRepository
){
    suspend operator fun invoke(uri: Uri? , length: Length , context: Context): CapAI{
        try {
            val request = CaptionRequest(
                imageBase64 = uri?.toBase64(context) ?: throw IllegalArgumentException("Uri cannot be null"),
                promptLength = length
            )
            val response = repository.getImageCaption(request)
            if (response.isSuccessful) {
                val captions = response.body()
                return CapAI(
                    image = uri.toBitmap(context),
                    instagramCaption = captions?.instagramCaption,
                    facebookCaption = captions?.facebookCaption,
                    twitterCaption = captions?.twitterCaption,
                    pinterestCaption = captions?.pinterestCaption,
                    linkedinCaption = captions?.linkedinCaption,
                    threadCaption = captions?.threadCaption,
                    snapChatCaption = captions?.snapChatCaption,
                    tiktokCaption = captions?.tiktokCaption,
                    isSuccess = true
                )
            } else {
                return CapAI(
                    isSuccess = false,
                    errorMessage = "Our AI servers are currently busy. Please try again in a moment."
                )
            }
        } catch (e: Exception) {
            val userFriendlyMessage = when {
                e.message?.contains("Unable to resolve host") == true -> 
                    "No internet connection. Please check your network and try again."
                e.message?.contains("timeout") == true -> 
                    "The connection timed out. Our AI is taking longer than usual to respond."
                else -> "Oops! We encountered an unexpected error while crafting your captions. Please try again."
            }
            return CapAI(
                errorMessage = userFriendlyMessage,
                isSuccess = false
            )
        }
    }
}