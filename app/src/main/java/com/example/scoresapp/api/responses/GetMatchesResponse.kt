package com.example.scoresapp.api.responses

import com.example.scoresapp.model.MatchData


data class GetMatchesResponse(
    val response: List<MatchData>
)

