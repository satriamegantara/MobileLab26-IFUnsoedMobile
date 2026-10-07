package com.getar.app.util

import com.getar.app.data.model.Earthquake
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExtensionsTest {

    private val sampleEarthquakes = listOf(
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
            magnitude = "4.8",
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

    // --- 1. Uji filterByWilayah ---

    @Test
    fun filterByWilayah_emptyQuery_returnsAllItems() {
        val result = sampleEarthquakes.filterByWilayah("")
        assertEquals(3, result.size)
    }

    @Test
    fun filterByWilayah_spacesOnlyQuery_returnsAllItems() {
        val result = sampleEarthquakes.filterByWilayah("   ")
        assertEquals(3, result.size)
    }

    @Test
    fun filterByWilayah_caseInsensitive_matchesCorrectly() {
        // Query huruf kecil "maluku" harus cocok dengan "Tual-Maluku Tenggara"
        val result = sampleEarthquakes.filterByWilayah("maluku")
        assertEquals(1, result.size)
        assertEquals("Pusat gempa berada di laut 98 km BaratLaut Tual-Maluku Tenggara", result[0].wilayah)
    }

    @Test
    fun filterByWilayah_withSurroundingSpaces_trimmedAndMatched() {
        val result = sampleEarthquakes.filterByWilayah("  bali  ")
        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
    }

    @Test
    fun filterByWilayah_noMatch_returnsEmptyList() {
        val result = sampleEarthquakes.filterByWilayah("Aceh")
        assertTrue(result.isEmpty())
    }

    // --- 2. Uji toSeverity dan severity() ---

    @Test
    fun toSeverity_boundaryValues() {
        assertEquals(Severity.MINOR, 4.9.toSeverity())
        assertEquals(Severity.MODERATE, 5.0.toSeverity())
        assertEquals(Severity.MODERATE, 5.9.toSeverity())
        assertEquals(Severity.STRONG, 6.0.toSeverity())
        assertEquals(Severity.MAJOR, 7.0.toSeverity())
        assertEquals(Severity.MAJOR, 8.5.toSeverity())
        assertEquals(Severity.UNKNOWN, null.toSeverity())
    }

    @Test
    fun severity_onEarthquakeObject() {
        assertEquals(Severity.MODERATE, sampleEarthquakes[0].severity()) // 5.4
        assertEquals(Severity.MINOR, sampleEarthquakes[1].severity())    // 4.8
        assertEquals(Severity.MAJOR, sampleEarthquakes[2].severity())    // 7.2
    }

    // --- 3. Uji hasTsunamiPotential ---

    @Test
    fun hasTsunamiPotential_tidakBerpotensi_returnsFalse() {
        val earthquake = sampleEarthquakes[0].copy(potensi = "Gempa ini tidak berpotensi tsunami")
        assertFalse(earthquake.hasTsunamiPotential())
    }

    @Test
    fun hasTsunamiPotential_berpotensi_returnsTrue() {
        val earthquake = sampleEarthquakes[2].copy(potensi = "Gempa ini berpotensi tsunami")
        assertTrue(earthquake.hasTsunamiPotential())
    }
}
