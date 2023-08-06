package com.example.scoresapp.ui

import com.example.scoresapp.model.MatchData


sealed interface UiState {
    data class Error(val errMsg: String?): UiState
    data class Success(val data: List<MatchData>): UiState
    object Loading: UiState
}