package com.example.scoresapp.api

import com.example.scoresapp.api.responses.GetMatchesResponse

interface MatchServiceApi {
    suspend fun getMatches(): GetMatchesResponse?
}