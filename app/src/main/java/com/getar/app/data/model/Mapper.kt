package com.getar.app.data.model

fun GempaDto.toEarthquake(id: Int): Earthquake = Earthquake(
    id = id,
    tanggal = tanggal ?: "-",
    jam = jam ?: "-",
    coordinates = coordinates ?: "-",
    magnitude = magnitude ?: "-",
    kedalaman = kedalaman ?: "-",
    wilayah = wilayah ?: "-",
    potensi = potensi ?: "-"
)
