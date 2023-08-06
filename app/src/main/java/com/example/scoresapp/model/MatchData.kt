package com.example.scoresapp.model

data class MatchData(
    val teams: Teams?,
    val league: League?,
    val wscGame: WSCGame?,
    val WSCGameId: String?,
) {
    fun isValidData(): Boolean {
        return wscGame?.name != "No story for this game" && !wscGame?.primeStory?.pages.isNullOrEmpty()
    }
}
