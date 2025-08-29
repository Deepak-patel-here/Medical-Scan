package com.deepakjetpackcompose.medicalscan.data.remote.dto

data class MedicalInfo(
    val name: String,
    val description: String? = null,
    val dosage: String,
    val frequencyPerDay: Int,
    val expiryDate: Long? = null
)

