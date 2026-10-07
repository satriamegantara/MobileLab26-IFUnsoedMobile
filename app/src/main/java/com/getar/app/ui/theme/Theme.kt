package com.getar.app.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.getar.app.util.Severity

// Skema Warna Terang (Light)
private val LightColors = lightColorScheme(
    primary = GetarPrimaryLight,
    onPrimary = GetarOnPrimaryLight,
    primaryContainer = GetarPrimaryContainerLight,
    onPrimaryContainer = GetarOnPrimaryContainerLight,
    secondary = GetarSecondaryLight,
    onSecondary = GetarOnSecondaryLight,
    secondaryContainer = GetarSecondaryContainerLight,
    onSecondaryContainer = GetarOnSecondaryContainerLight,
    tertiary = GetarTertiaryLight,
    onTertiary = GetarOnTertiaryLight,
    tertiaryContainer = GetarTertiaryContainerLight,
    onTertiaryContainer = GetarOnTertiaryContainerLight,
    background = GetarBackgroundLight,
    onBackground = GetarOnBackgroundLight,
    surface = GetarSurfaceLight,
    onSurface = GetarOnSurfaceLight,
    surfaceVariant = GetarSurfaceVariantLight,
    onSurfaceVariant = GetarOnSurfaceVariantLight,
    outline = GetarOutlineLight,
    outlineVariant = GetarOutlineVariantLight,
    error = GetarErrorLight,
    onError = GetarOnErrorLight,
    errorContainer = GetarErrorContainerLight,
    onErrorContainer = GetarOnErrorContainerLight
)

// Skema Warna Gelap (Dark)
private val DarkColors = darkColorScheme(
    primary = GetarPrimaryDark,
    onPrimary = GetarOnPrimaryDark,
    primaryContainer = GetarPrimaryContainerDark,
    onPrimaryContainer = GetarOnPrimaryContainerDark,
    secondary = GetarSecondaryDark,
    onSecondary = GetarOnSecondaryDark,
    secondaryContainer = GetarSecondaryContainerDark,
    onSecondaryContainer = GetarOnSecondaryContainerDark,
    tertiary = GetarTertiaryDark,
    onTertiary = GetarOnTertiaryDark,
    tertiaryContainer = GetarTertiaryContainerDark,
    onTertiaryContainer = GetarOnTertiaryContainerDark,
    background = GetarBackgroundDark,
    onBackground = GetarOnBackgroundDark,
    surface = GetarSurfaceDark,
    onSurface = GetarOnSurfaceDark,
    surfaceVariant = GetarSurfaceVariantDark,
    onSurfaceVariant = GetarOnSurfaceVariantDark,
    outline = GetarOutlineDark,
    outlineVariant = GetarOutlineVariantDark,
    error = GetarErrorDark,
    onError = GetarOnErrorDark,
    errorContainer = GetarErrorContainerDark,
    onErrorContainer = GetarOnErrorContainerDark
)

// Penampung Warna Kustom Severity
@Immutable
data class SeverityColors(
    val minor: Color,
    val onMinor: Color,
    val moderate: Color,
    val onModerate: Color,
    val strong: Color,
    val onStrong: Color,
    val major: Color,
    val onMajor: Color,
    val unknown: Color,
    val onUnknown: Color
)

private val LightSeverity = SeverityColors(
    minor = SeverityMinorLight,
    onMinor = OnSeverityLight,
    moderate = SeverityModerateLight,
    onModerate = OnSeverityLight,
    strong = SeverityStrongLight,
    onStrong = OnSeverityLight,
    major = SeverityMajorLight,
    onMajor = OnSeverityLight,
    unknown = SeverityUnknownLight,
    onUnknown = OnSeverityLight
)

private val DarkSeverity = SeverityColors(
    minor = SeverityMinorDark,
    onMinor = OnSeverityMinorDark,
    moderate = SeverityModerateDark,
    onModerate = OnSeverityModerateDark,
    strong = SeverityStrongDark,
    onStrong = OnSeverityStrongDark,
    major = SeverityMajorDark,
    onMajor = OnSeverityMajorDark,
    unknown = SeverityUnknownDark,
    onUnknown = OnSeverityUnknownDark
)

// CompositionLocal untuk SeverityColors
val LocalSeverityColors = staticCompositionLocalOf { LightSeverity }

/**
 * Extension property agar SeverityColors dapat diakses lewat MaterialTheme.severity
 */
val MaterialTheme.severity: SeverityColors
    @Composable
    get() = LocalSeverityColors.current

// Helper fungsi untuk mendapatkan warna latar badge berdasarkan enum Severity
fun SeverityColors.colorFor(severity: Severity): Color = when (severity) {
    Severity.MINOR -> minor
    Severity.MODERATE -> moderate
    Severity.STRONG -> strong
    Severity.MAJOR -> major
    Severity.UNKNOWN -> unknown
}

// Helper fungsi untuk mendapatkan warna teks di atas badge berdasarkan enum Severity
fun SeverityColors.onColorFor(severity: Severity): Color = when (severity) {
    Severity.MINOR -> onMinor
    Severity.MODERATE -> onModerate
    Severity.STRONG -> onStrong
    Severity.MAJOR -> onMajor
    Severity.UNKNOWN -> onUnknown
}

// Token Bentuk (Shapes)
val GetarShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp)
)

/**
 * Tema Utama Getar. Dynamic color dinonaktifkan agar konsistensi identitas visual terjaga.
 */
@Composable
fun GetarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val severityColors = if (darkTheme) DarkSeverity else LightSeverity

    CompositionLocalProvider(
        LocalSeverityColors provides severityColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = GetarTypography,
            shapes = GetarShapes,
            content = content
        )
    }
}

// Preview contoh Typography dan Swatch Warna Severity
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ThemeShowcasePreview() {
    GetarTheme {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Getar Theme Preview",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Gempa Terkini BMKG • 06 Okt 2026",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Pusat gempa berada di laut 98 km BaratLaut Tual",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Kedalaman: 10 km • Koordinat: -6.12, 128.45",
                    style = MonoValue,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Severity Badges:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val severityList = listOf(
                        Severity.MINOR to "4.8",
                        Severity.MODERATE to "5.4",
                        Severity.STRONG to "6.2",
                        Severity.MAJOR to "7.5",
                        Severity.UNKNOWN to "-"
                    )

                    severityList.forEach { (sev, label) ->
                        val bgColor = MaterialTheme.severity.colorFor(sev)
                        val textColor = MaterialTheme.severity.onColorFor(sev)

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(bgColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                style = MonoBadge,
                                color = textColor
                            )
                        }
                    }
                }
            }
        }
    }
}