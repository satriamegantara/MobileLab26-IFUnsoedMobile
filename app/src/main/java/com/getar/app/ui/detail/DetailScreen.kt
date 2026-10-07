package com.getar.app.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.getar.app.R
import com.getar.app.data.model.Earthquake
import com.getar.app.ui.component.EmptyView
import com.getar.app.ui.component.GetarTopBar
import com.getar.app.ui.component.InfoRow
import com.getar.app.ui.component.PotensiCard
import com.getar.app.ui.theme.GetarTheme
import com.getar.app.ui.theme.colorFor
import com.getar.app.ui.theme.severity
import com.getar.app.util.Severity
import com.getar.app.util.hasTsunamiPotential
import com.getar.app.util.severity

@Composable
fun DetailScreen(
    earthquake: Earthquake?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            GetarTopBar(
                title = stringResource(R.string.detail_title),
                onBack = onBack
            )
        }
    ) { innerPadding ->
        if (earthquake == null) {
            EmptyView(
                query = "",
                onClear = onBack,
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Hero Card: Magnitudo Besar & Kategori Keparahan
                val severity = earthquake.severity()
                val severityColor = MaterialTheme.severity.colorFor(severity)
                val categoryName = when (severity) {
                    Severity.MINOR -> "Ringan"
                    Severity.MODERATE -> "Sedang"
                    Severity.STRONG -> "Kuat"
                    Severity.MAJOR -> "Sangat kuat"
                    Severity.UNKNOWN -> "Tidak diketahui"
                }

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp, horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = earthquake.magnitude,
                            style = MaterialTheme.typography.displayMedium,
                            color = severityColor
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${stringResource(R.string.label_magnitudo)} • $categoryName",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // 2. Kartu Potensi Tsunami
                PotensiCard(
                    potensi = earthquake.potensi,
                    hasTsunamiPotential = earthquake.hasTsunamiPotential()
                )

                // 3. Kartu Detail 5 Informasi Gempa Lainnya
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        InfoRow(
                            label = stringResource(R.string.label_tanggal),
                            value = earthquake.tanggal
                        )

                        InfoRow(
                            label = stringResource(R.string.label_jam),
                            value = earthquake.jam
                        )

                        InfoRow(
                            label = stringResource(R.string.label_coordinates),
                            value = earthquake.coordinates,
                            mono = true
                        )

                        InfoRow(
                            label = stringResource(R.string.label_kedalaman),
                            value = earthquake.kedalaman,
                            mono = true
                        )

                        InfoRow(
                            label = stringResource(R.string.label_wilayah),
                            value = earthquake.wilayah,
                            showDivider = false
                        )
                    }
                }
            }
        }
    }
}

// Data Tiruan untuk Preview
private val PreviewEarthquakeDetail = Earthquake(
    id = 0,
    tanggal = "06 Okt 2026",
    jam = "14:22:10 WIB",
    coordinates = "-6.12, 128.45",
    magnitude = "5.4",
    kedalaman = "10 km",
    wilayah = "Pusat gempa berada di laut 98 km BaratLaut Tual-Maluku Tenggara",
    potensi = "Gempa ini tidak berpotensi tsunami"
)

@Preview(name = "Detail - Light", showBackground = true)
@Preview(name = "Detail - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DetailScreenPreview() {
    GetarTheme {
        DetailScreen(
            earthquake = PreviewEarthquakeDetail,
            onBack = {}
        )
    }
}

@Preview(name = "Detail Null - Light", showBackground = true)
@Preview(name = "Detail Null - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DetailScreenNullPreview() {
    GetarTheme {
        DetailScreen(
            earthquake = null,
            onBack = {}
        )
    }
}
