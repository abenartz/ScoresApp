package com.example.scoresapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoresapp.Constants.APP_DEBUG
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class MatchListViewModel: ViewModel() {

    val matches = MutableStateFlow(emptyList<MatchData>())


    fun readDataFromJson(context: Context) {
        Timber.tag(APP_DEBUG).d("MatchListViewModel: MatchListViewModel:")
        viewModelScope.launch(Dispatchers.IO) {
            val json: String
            try {
                val inputStream: InputStream = context.assets.open("response.json.txt")
                val size: Int = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = buffer.toString(Charset.defaultCharset())
                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                val adapter: JsonAdapter<GetMatchesResponse> = moshi.adapter(GetMatchesResponse::class.java)
                val data: List<MatchData> = adapter.fromJson(json)?.response?.filter { it.wscGame != null } ?: emptyList()
                matches.emit(data)
            } catch (e: IOException) {
                Timber.tag(APP_DEBUG).e("MatchListViewModel: readDataFromJson: IOException = $e")
            } catch (e: JsonDataException) {
                Timber.tag(APP_DEBUG).e("MatchListViewModel: readDataFromJson: JsonDataException = $e")
            }
        }
    }
}

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
