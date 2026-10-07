package com.getar.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getar.app.data.model.Earthquake
import com.getar.app.data.repository.GempaRepository
import com.getar.app.ui.state.UiState
import com.getar.app.util.filterByWilayah
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HomeViewModel(
    private val repository: GempaRepository = GempaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val visibleList: StateFlow<List<Earthquake>> = combine(_uiState, _query) { state, q ->
        (state as? UiState.Success)?.data?.filterByWilayah(q).orEmpty()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val earthquakes = repository.getGempaTerkini()
                _uiState.value = UiState.Success(earthquakes)
            } catch (e: IOException) {
                _uiState.value = UiState.Error("Tidak dapat terhubung. Periksa koneksi internet Anda.")
            } catch (e: HttpException) {
                _uiState.value = UiState.Error("Server BMKG bermasalah (kode ${e.code()}).")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Terjadi kesalahan: ${e.message ?: "Tidak diketahui"}")
            }
        }
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun findById(id: Int): Earthquake? {
        return (uiState.value as? UiState.Success)?.data?.find { it.id == id }
    }
}
