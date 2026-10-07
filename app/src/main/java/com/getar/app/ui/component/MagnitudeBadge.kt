package com.getar.app.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.getar.app.ui.theme.GetarTheme
import com.getar.app.ui.theme.MonoBadge
import com.getar.app.ui.theme.colorFor
import com.getar.app.ui.theme.onColorFor
import com.getar.app.ui.theme.severity
import com.getar.app.util.toSeverity

@Composable
fun MagnitudeBadge(
    magnitude: String,
    modifier: Modifier = Modifier,
    size: Dp = 52.dp
) {
    val severity = magnitude.toDoubleOrNull().toSeverity()
    val backgroundColor = MaterialTheme.severity.colorFor(severity)
    val textColor = MaterialTheme.severity.onColorFor(severity)

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = magnitude,
            style = MonoBadge,
            color = textColor
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun MagnitudeBadgePreview() {
    GetarTheme {
        MagnitudeBadge(magnitude = "5.4")
    }
}
