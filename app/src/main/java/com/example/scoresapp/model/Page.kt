package com.example.scoresapp.model

data class Page(
    val duration: Int?,
    val paggeId: String?,
    val videoUrl: String?,
    val awayScore: Int?,
    val actionType: String?,
    val homeScore: Int?
) {
    fun isDurationValid(): Boolean {
        return duration != null && duration > 0
    }
}