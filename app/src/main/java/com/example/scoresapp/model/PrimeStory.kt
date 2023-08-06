package com.example.scoresapp.model


data class PrimeStory(
    val storyId: String?,
    var pages: MutableList<Page>?,
    val publishDate: String?,
    val storyThumbnail: String?,
    val title: String?,
    val storyThumbnailSquare: String?
)
