package com.example.scoresapp.api.responses


data class GetMatchesResponse(
    val response: List<MatchData>
)

data class MatchData(
    val teams: Teams?,
    val league: League?,
    val wscGame: WSCGame?,
    val WSCGameId: String?,
)

data class Teams(
    val away: Team?,
    val home: Team?
)

data class Team(
    val logo: String?,
    val id: Int?,
    val name: String,
    val winner: String?
)

data class League(
    val name: String?,
    val season: Int?,
    val id: Int?,
    val logo: String?,
    val round: String?,
    val flag: String?,
    val country: String?
)

data class WSCGame(
    val name: String?,
    val homeTeamName: String?,
    val gameId: String?,
    val gameTime: String?,
    val primeStory: PrimeStory?
)

data class PrimeStory(
    val storyId: String?,
    val pages: List<Page>?,
    val publishDate: String?,
    val storyThumbnail: String?,
    val title: String?,
    val storyThumbnailSquare: String?
)

data class Page(
    val duration: Int,
    val eventType: String?,
    val paggeId: String,
    val videoUrl: String,
    val period: String?,
    val title: String?,
    val awayScore: Int?,
    val actionType: String?,
    val gameClock: String?,
    val rating: Int?,
    val homeScore: Int?
)
