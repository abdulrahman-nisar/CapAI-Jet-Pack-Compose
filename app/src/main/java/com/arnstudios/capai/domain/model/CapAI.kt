package com.arnstudios.capshotai.domain.model

import android.graphics.Bitmap
import android.net.Uri
import com.google.gson.annotations.SerializedName

data class CapAI(
    var id: Int = 0,
    var image: Bitmap? = null,

    @SerializedName("instagram")
    var instagramCaption: String? = "",
    @SerializedName("facebook")
    var facebookCaption: String? = "",
    @SerializedName("twitter")
    var twitterCaption: String? = "",
    @SerializedName("pinterest")
    var pinterestCaption: String? = "",
    @SerializedName("linkedin")
    var linkedinCaption: String? = "",
    @SerializedName("thread")
    var threadCaption: String? = "",
    @SerializedName("snapchat")
    var snapChatCaption: String? = "",
    @SerializedName("tiktok")
    var tiktokCaption: String? = "",

    var imageUri : Uri? = null,
    var isSuccess: Boolean = false,
    var errorMessage: String? = null
)