package com.arnstudios.capai.domain.model

import com.google.gson.annotations.SerializedName

data class CaptionRequest(
    @SerializedName("imageBase64") val imageBase64: String,
    @SerializedName("mimeType") val mimeType: String = "image/jpeg",
    @SerializedName("promptLength") val promptLength: Length = Length.SHORT
)
