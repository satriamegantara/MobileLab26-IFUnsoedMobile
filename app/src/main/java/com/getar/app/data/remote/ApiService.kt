package com.getar.app.data.remote

import com.getar.app.data.model.GempaResponseDto
import retrofit2.http.GET

interface ApiService {
    @GET("DataMKG/TEWS/gempaterkini.json")
    suspend fun getGempaTerkini(): GempaResponseDto
}
