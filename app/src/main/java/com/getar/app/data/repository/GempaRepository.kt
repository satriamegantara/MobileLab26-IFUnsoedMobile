package com.getar.app.data.repository

import com.getar.app.data.model.Earthquake
import com.getar.app.data.model.toEarthquake
import com.getar.app.data.remote.ApiService
import com.getar.app.data.remote.RetrofitInstance

class GempaRepository(
    private val apiService: ApiService = RetrofitInstance.api
) {
    suspend fun getGempaTerkini(): List<Earthquake> =
        apiService.getGempaTerkini()
            .infogempa?.gempa.orEmpty()
            .mapIndexed { index, dto -> dto.toEarthquake(id = index) }
}
