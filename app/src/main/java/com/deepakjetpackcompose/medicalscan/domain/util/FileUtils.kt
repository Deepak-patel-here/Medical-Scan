package com.deepakjetpackcompose.medicalscan.domain.util

import android.content.Context
import android.net.Uri
import java.io.File

object FileUtils {
    fun getFromUri(context: Context,imageUri: Uri): File{
        val inputStream=context.contentResolver.openInputStream(imageUri)
        val file= File(context.cacheDir,"upload_${System.currentTimeMillis()}.jpg")
        file.outputStream().use{
            inputStream?.copyTo(it)
        }
        return file
    }
}