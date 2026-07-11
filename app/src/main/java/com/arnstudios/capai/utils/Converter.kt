package com.arnstudios.capai.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64

fun Uri.toBase64(context: Context): String {
    val bytes = context.contentResolver.openInputStream(this)?.use {
        it.readBytes()
    } ?: throw IllegalArgumentException("Unable to read Uri")

    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}

fun Uri.toBitmap(context: Context): Bitmap {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(context.contentResolver, this)
        ImageDecoder.decodeBitmap(source)
    } else {
        @Suppress("DEPRECATION")
        MediaStore.Images.Media.getBitmap(context.contentResolver, this)
    }
}