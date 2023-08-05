package com.example.scoresapp.repository

import com.example.scoresapp.api.MatchServiceApi
import com.example.scoresapp.api.responses.MatchData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchRepository @Inject constructor(
    private val serviceApi: MatchServiceApi
) {

    suspend fun getMatches(): List<MatchData> = withContext(Dispatchers.IO) {
        val result = serviceApi.getMatches()
        val data = result?.response?.filter { it.wscGame?.primeStory?.pages != null } ?: emptyList()
        // TODO: handle first duration different
        data.forEach {
            it.wscGame?.primeStory?.pages?.firstOrNull()?.duration = 3000
        }
        return@withContext data
    }
}