package com.example.scoresapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoresapp.api.responses.GetMatchesResponse
import com.example.scoresapp.api.responses.MatchData
import com.example.scoresapp.api.responses.Page
import com.example.scoresapp.constants.Constants.APP_DEBUG
import com.example.scoresapp.constants.Constants.RESPONSE_JSON_FILE_NAME
import com.example.scoresapp.ui.UiState
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import kotlin.time.Duration.Companion.seconds

class MainViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    var storyPages: List<Page> = emptyList()

    fun readDataFromJson(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val json: String
            try {
                val inputStream: InputStream = context.assets.open(RESPONSE_JSON_FILE_NAME)
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
                // TODO: handle first duration different
                data.forEach {
                    it.wscGame?.primeStory?.pages?.firstOrNull()?.duration = 3000
                }
                _uiState.emit(UiState.Success(data))
            } catch (e: IOException) {
                Timber.tag(APP_DEBUG).e("MatchListViewModel: readDataFromJson: IOException = $e")
                _uiState.emit(UiState.Error("Failed retrieving data"))
            } catch (e: JsonDataException) {
                Timber.tag(APP_DEBUG).e("MatchListViewModel: readDataFromJson: JsonDataException = $e")
                _uiState.emit(UiState.Error("Failed retrieving data"))
            }
        }
    }
}


