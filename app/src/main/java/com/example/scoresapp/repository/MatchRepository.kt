package com.example.scoresapp.repository

import com.example.scoresapp.api.MatchServiceApi
import com.example.scoresapp.model.MatchData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchRepository @Inject constructor(
    private val serviceApi: MatchServiceApi
) {

    suspend fun getMatches(): List<MatchData> = withContext(Dispatchers.IO) {
        val result = serviceApi.getMatches()
        val onlyWscMatches = result?.response?.filter { it.isValidData() } ?: emptyList()
        onlyWscMatches.forEach { data ->
            data.wscGame?.primeStory?.pages?.retainAll { it.isDurationValid() }
        }
        return@withContext onlyWscMatches
    }
}