package com.example.scoresapp.extensions

import androidx.media3.common.MediaItem
import com.example.scoresapp.model.Page

fun List<Page>.toDurationArray(): LongArray {
    return this.map { it.duration?.toLong() ?: 0L }.toLongArray()
}

fun List<Page>.toMediaItems(): List<MediaItem> {
    return this.map { MediaItem.fromUri(it.videoUrl ?: "") }
}