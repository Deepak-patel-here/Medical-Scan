package com.deepakjetpackcompose.medicalscan.data.remote.service

import com.deepakjetpackcompose.medicalscan.data.remote.dto.GeminiRequest
import com.deepakjetpackcompose.medicalscan.data.remote.dto.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MedicineService {

    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    suspend fun generateContent(
        @Body request: GeminiRequest,
        @Header("X-goog-api-key") apiKey: String
    ): GeminiResponse

}