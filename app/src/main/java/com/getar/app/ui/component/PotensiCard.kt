package com.getar.app.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.getar.app.ui.theme.GetarTheme

@Composable
fun PotensiCard(
    potensi: String,
    hasTsunamiPotential: Boolean,
    modifier: Modifier = Modifier
) {
    val containerColor = if (hasTsunamiPotential) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.tertiaryContainer
    }

    val contentColor = if (hasTsunamiPotential) {
        MaterialTheme.colorScheme.onErrorContainer
    } else {
        MaterialTheme.colorScheme.onTertiaryContainer
    }

    val icon = if (hasTsunamiPotential) {
        Icons.Default.Warning
    } else {
        Icons.Default.Check
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = contentColor
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = potensi,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = contentColor
            )
        }
    }
}

@Preview(name = "Aman - Light", showBackground = true)
@Preview(name = "Aman - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PotensiCardAmanPreview() {
    GetarTheme {
        PotensiCard(
            potensi = "Gempa ini tidak berpotensi tsunami",
            hasTsunamiPotential = false
        )
    }
}

@Preview(name = "Bahaya - Light", showBackground = true)
@Preview(name = "Bahaya - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PotensiCardBahayaPreview() {
    GetarTheme {
        PotensiCard(
            potensi = "Peringatan! Gempa ini berpotensi tsunami",
            hasTsunamiPotential = true
        )
    }
}
