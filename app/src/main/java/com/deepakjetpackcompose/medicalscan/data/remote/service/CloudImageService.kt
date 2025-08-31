package com.deepakjetpackcompose.medicalscan.data.remote.service

import com.deepakjetpackcompose.medicalscan.data.remote.dto.CloudinaryResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CloudImageService {

    @Multipart
    @POST("image/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("upload_preset") uploadPreset: okhttp3.RequestBody
    ): CloudinaryResponse
}