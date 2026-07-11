package com.arnstudios.capshotai.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("captions")
data class CaptionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("instagram_caption")
    val instagramCaption: String,
    @ColumnInfo("facebook_caption")
    val facebookCaption: String,
    @ColumnInfo("twitter_caption")
    val twitterCaption: String,
    @ColumnInfo("pinterest_caption")
    val pinterestCaption: String,
    @ColumnInfo("linkedin_caption")
    val linkedinCaption: String,
    @ColumnInfo("thread_caption")
    val threadCaption: String,
    @ColumnInfo("snapchat_caption")
    val snapChatCaption: String,
    @ColumnInfo("tiktok_caption")
    val tiktokCaption: String,
    @ColumnInfo("timestamp")
    val timestamp: Long,
    @ColumnInfo("image_uri")
    val imageUri : String
)
