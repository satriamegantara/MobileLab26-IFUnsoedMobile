package com.getar.app.navigation

/**
 * Definisi rute navigasi aplikasi Getar.
 */
object Routes {
    const val HOME = "home"
    const val DETAIL = "detail/{id}"

    fun detail(id: Int): String = "detail/$id"
}
