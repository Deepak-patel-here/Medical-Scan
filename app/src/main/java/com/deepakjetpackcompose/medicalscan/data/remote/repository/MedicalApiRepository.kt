package com.deepakjetpackcompose.medicalscan.data.remote.repository

import android.util.Log
import com.deepakjetpackcompose.medicalscan.BuildConfig
import com.deepakjetpackcompose.medicalscan.data.remote.dto.GeminiContent
import com.deepakjetpackcompose.medicalscan.data.remote.dto.GeminiPart
import com.deepakjetpackcompose.medicalscan.data.remote.dto.GeminiRequest
import com.deepakjetpackcompose.medicalscan.data.remote.dto.MedicalInfo
import com.deepakjetpackcompose.medicalscan.data.remote.service.MedicineService
import com.google.gson.Gson
import javax.inject.Inject


class MedicalApiRepository @Inject constructor(private val service: MedicineService) {

    suspend fun getData(text:String): MedicalInfo?{
        val request= GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(
                        GeminiPart(
                            "You are a medical text parser. Extract structured information from the following medicine strip text. \n" +
                                    "Return JSON with fields:\n" +
                                    "{\n" +
                                    "  \"name\": \"\",\n" +
                                    "  \"description\": \"\",\n" +
                                    "  \"dosage\": \"\",\n" +
                                    "  \"frequencyPerDay\": 0,\n" +
                                    "  \"durationDays\": 0,\n" +
                                    "  \"startDate\": \"\",\n" +
                                    "  \"expiryDate\": \"\",\n" +
                                    "}" + "this is the text $text"
                        )
                    )
                )

            )
        )
        val apiKey= BuildConfig.API_KEY
        val response=service.generateContent(request=request, apiKey = "")
        Log.d("api",response.toString())
        var rawJson=response.candidates[0].content.parts[0].text

        rawJson = rawJson
            .replace("```json", "")
            .replace("```", "")
            .trim()

        Log.d("api-clean", rawJson)
        return try {
            Gson().fromJson(rawJson, MedicalInfo::class.java)
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }
}