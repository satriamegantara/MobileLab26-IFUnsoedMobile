package com.getar.app.util

import com.getar.app.data.model.Earthquake

/**
 * Memfilter daftar gempa berdasarkan nama wilayah.
 * Mengabaikan perbedaan huruf besar/kecil (case-insensitive) dan spasi di awal/akhir query.
 */
fun List<Earthquake>.filterByWilayah(query: String): List<Earthquake> {
    val q = query.trim()
    return if (q.isEmpty()) this else filter { it.wilayah.contains(q, ignoreCase = true) }
}

/**
 * Menentukan apakah gempa berpotensi tsunami.
 * Mengembalikan true jika terdapat kata "tsunami" dan tidak mengandung kata "tidak".
 */
fun Earthquake.hasTsunamiPotential(): Boolean =
    potensi.contains("tsunami", ignoreCase = true) &&
        !potensi.contains("tidak", ignoreCase = true)

/**
 * Kategori keparahan magnitudo gempa untuk penentuan warna badge/status.
 */
enum class Severity {
    MINOR,
    MODERATE,
    STRONG,
    MAJOR,
    UNKNOWN
}

/**
 * Mengonversi nilai magnitudo numerik (Double?) ke tingkat Severity.
 * Aman dipanggil pada nilai null.
 */
fun Double?.toSeverity(): Severity = when {
    this == null -> Severity.UNKNOWN
    this < 5.0 -> Severity.MINOR
    this < 6.0 -> Severity.MODERATE
    this < 7.0 -> Severity.STRONG
    else -> Severity.MAJOR
}

/**
 * Mengambil tingkat severity langsung dari objek Earthquake.
 */
fun Earthquake.severity(): Severity = magnitudeValue.toSeverity()
