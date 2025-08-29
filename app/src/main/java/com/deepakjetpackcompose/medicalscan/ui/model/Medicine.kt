package com.deepakjetpackcompose.medicalscan.ui.model

// Medicine model
data class Medicine(
    val id:Int?=null,
    val name: String,
    val description: String? = null,
    val dosage: String,
    val frequencyPerDay: Int,
    val durationDays: Int,
    val startDate: Long,
    val expiryDate: Long? = null,
    val stockCount: Int? = null,
    val nextDoseTime: Long? = null
)