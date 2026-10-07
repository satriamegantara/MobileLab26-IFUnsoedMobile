package com.getar.app.ui.state

import com.getar.app.data.model.Earthquake

/**
 * Representasi seluruh kemungkinan status UI untuk HomeScreen.
 */
sealed interface UiState {
    data object Loading : UiState
    data class Success(val data: List<Earthquake>) : UiState
    data class Error(val message: String) : UiState
}
