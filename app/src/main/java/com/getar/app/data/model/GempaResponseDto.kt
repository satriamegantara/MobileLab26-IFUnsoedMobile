package com.getar.app.data.model

import com.google.gson.annotations.SerializedName

data class GempaResponseDto(
    @SerializedName("Infogempa")
    val infogempa: InfogempaDto? = null
)

data class InfogempaDto(
    @SerializedName("gempa")
    val gempa: List<GempaDto>? = null
)

data class GempaDto(
    @SerializedName("Tanggal")
    val tanggal: String? = null,
    @SerializedName("Jam")
    val jam: String? = null,
    @SerializedName("Coordinates")
    val coordinates: String? = null,
    @SerializedName("Magnitude")
    val magnitude: String? = null,
    @SerializedName("Kedalaman")
    val kedalaman: String? = null,
    @SerializedName("Wilayah")
    val wilayah: String? = null,
    @SerializedName("Potensi")
    val potensi: String? = null
)
