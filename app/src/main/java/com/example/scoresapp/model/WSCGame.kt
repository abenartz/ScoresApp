package com.example.scoresapp.model


data class WSCGame(
    val name: String?,
    val homeTeamName: String?,
    val gameId: String?,
    val gameTime: String?,
    val primeStory: PrimeStory?
)
