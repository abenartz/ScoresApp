package com.example.scoresapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoresapp.constants.Constants.APP_DEBUG
import com.example.scoresapp.model.Page
import com.example.scoresapp.repository.MatchRepository
import com.example.scoresapp.ui.UiState
import com.squareup.moshi.JsonDataException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val repository: MatchRepository
): ViewModel() {

    var currStoryPages: List<Page> = emptyList()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        fetchAllMatchesData()
    }

    private fun fetchAllMatchesData() {
        viewModelScope.launch {
            try {
                val matches = repository.getMatches()
                _uiState.emit(UiState.Success(matches))
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


