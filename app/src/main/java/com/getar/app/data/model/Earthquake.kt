package com.getar.app.data.model

data class Earthquake(
    val id: Int,
    val tanggal: String,
    val jam: String,
    val coordinates: String,
    val magnitude: String,
    val kedalaman: String,
    val wilayah: String,
    val potensi: String
) {
    val magnitudeValue: Double?
        get() = magnitude.toDoubleOrNull()
}
