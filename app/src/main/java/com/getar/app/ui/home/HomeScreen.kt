package com.getar.app.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.getar.app.R
import com.getar.app.data.model.Earthquake
import com.getar.app.ui.component.EarthquakeItem
import com.getar.app.ui.component.EmptyView
import com.getar.app.ui.component.ErrorView
import com.getar.app.ui.component.GetarTopBar
import com.getar.app.ui.component.LoadingView
import com.getar.app.ui.component.SearchField
import com.getar.app.ui.state.UiState
import com.getar.app.ui.theme.GetarTheme

/**
 * HomeScreen (Stateful Composable):
 * Bertanggung jawab mengamati state dari ViewModel dan meneruskan event ke ViewModel.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onItemClick: (Earthquake) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()
    val visibleList by viewModel.visibleList.collectAsState()

    HomeContent(
        uiState = uiState,
        query = query,
        visibleList = visibleList,
        onQueryChange = viewModel::onQueryChange,
        onRetry = viewModel::load,
        onClearQuery = { viewModel.onQueryChange("") },
        onItemClick = onItemClick,
        modifier = modifier
    )
}

/**
 * HomeContent (Stateless Composable):
 * Murni mengelola tata letak dan UI tanpa ketergantungan pada ViewModel.
 * Sangat mudah diuji dan ditampilkan di Compose Preview.
 */
@Composable
fun HomeContent(
    uiState: UiState,
    query: String,
    visibleList: List<Earthquake>,
    onQueryChange: (String) -> Unit,
    onRetry: () -> Unit,
    onClearQuery: () -> Unit,
    onItemClick: (Earthquake) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            GetarTopBar(
                title = stringResource(R.string.app_name),
                subtitle = stringResource(R.string.app_subtitle)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Kolom Pencarian Wilayah
            SearchField(
                query = query,
                onQueryChange = onQueryChange,
                enabled = uiState is UiState.Success,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Percabangan Tampilan Berdasarkan Status UI
            when (uiState) {
                is UiState.Loading -> {
                    LoadingView(
                        modifier = Modifier.weight(1f)
                    )
                }

                is UiState.Error -> {
                    ErrorView(
                        message = uiState.message,
                        onRetry = onRetry,
                        modifier = Modifier.weight(1f)
                    )
                }

                is UiState.Success -> {
                    if (visibleList.isEmpty()) {
                        EmptyView(
                            query = query,
                            onClear = onClearQuery,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        LazyColumn(
                            state = listState,
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(
                                items = visibleList,
                                key = { it.id }
                            ) { earthquake ->
                                EarthquakeItem(
                                    earthquake = earthquake,
                                    onClick = onItemClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Data Tiruan untuk Preview
private val PreviewEarthquakes = listOf(
    Earthquake(
        id = 0,
        tanggal = "06 Okt 2026",
        jam = "14:22:10 WIB",
        coordinates = "-6.12, 128.45",
        magnitude = "5.4",
        kedalaman = "10 km",
        wilayah = "Pusat gempa berada di laut 98 km BaratLaut Tual-Maluku Tenggara",
        potensi = "Gempa ini tidak berpotensi tsunami"
    ),
    Earthquake(
        id = 1,
        tanggal = "05 Okt 2026",
        jam = "08:15:00 WIB",
        coordinates = "-8.20, 115.10",
        magnitude = "6.1",
        kedalaman = "15 km",
        wilayah = "Pusat gempa berada di darat 12 km BaratDaya Tabanan-Bali",
        potensi = "Tidak berpotensi tsunami"
    ),
    Earthquake(
        id = 2,
        tanggal = "04 Okt 2026",
        jam = "02:00:00 WIB",
        coordinates = "0.50, 122.10",
        magnitude = "7.2",
        kedalaman = "20 km",
        wilayah = "Pusat gempa berada di laut 70 km TimurLaut Gorontalo",
        potensi = "Gempa ini berpotensi tsunami"
    )
)

// --- PREVIEW 4 STATE (Light & Dark) ---

@Preview(name = "Success - Light", showBackground = true)
@Preview(name = "Success - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun HomeContentSuccessPreview() {
    GetarTheme {
        HomeContent(
            uiState = UiState.Success(PreviewEarthquakes),
            query = "",
            visibleList = PreviewEarthquakes,
            onQueryChange = {},
            onRetry = {},
            onClearQuery = {},
            onItemClick = {}
        )
    }
}

@Preview(name = "Loading - Light", showBackground = true)
@Preview(name = "Loading - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun HomeContentLoadingPreview() {
    GetarTheme {
        HomeContent(
            uiState = UiState.Loading,
            query = "",
            visibleList = emptyList(),
            onQueryChange = {},
            onRetry = {},
            onClearQuery = {},
            onItemClick = {}
        )
    }
}

@Preview(name = "Empty - Light", showBackground = true)
@Preview(name = "Empty - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun HomeContentEmptyPreview() {
    GetarTheme {
        HomeContent(
            uiState = UiState.Success(PreviewEarthquakes),
            query = "Papua",
            visibleList = emptyList(),
            onQueryChange = {},
            onRetry = {},
            onClearQuery = {},
            onItemClick = {}
        )
    }
}

@Preview(name = "Error - Light", showBackground = true)
@Preview(name = "Error - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun HomeContentErrorPreview() {
    GetarTheme {
        HomeContent(
            uiState = UiState.Error("Tidak dapat terhubung. Periksa koneksi internet Anda."),
            query = "",
            visibleList = emptyList(),
            onQueryChange = {},
            onRetry = {},
            onClearQuery = {},
            onItemClick = {}
        )
    }
}
