package com.arnstudios.capai.ui.screen.components

import android.content.Context
import android.content.Intent
import android.net.Uri

fun ShareImageAndCaption(
    context: Context,
    imageUri: Uri,
    caption: String
) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, imageUri)
        putExtra(Intent.EXTRA_TEXT, caption)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(
        Intent.createChooser(shareIntent, "Share via")
    )
}
