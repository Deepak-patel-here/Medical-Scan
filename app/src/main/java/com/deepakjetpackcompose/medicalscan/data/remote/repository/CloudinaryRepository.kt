package com.deepakjetpackcompose.medicalscan.data.remote.repository

import android.content.Context
import android.net.Uri
import com.deepakjetpackcompose.medicalscan.data.remote.service.CloudImageService
import com.deepakjetpackcompose.medicalscan.domain.util.FileUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class CloudinaryRepository @Inject constructor(private val client: CloudImageService) {

    suspend fun uploadImage(context: Context,imageUri: Uri):String{

        val file= FileUtils.getFromUri(context,imageUri)
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val preset = RequestBody.create("text/plain".toMediaTypeOrNull(), "Medical_profile")

        val response = client.uploadImage(body, preset)
        return response.secure_url

    }


}