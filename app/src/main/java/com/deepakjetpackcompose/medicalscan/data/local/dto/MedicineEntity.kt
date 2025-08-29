package com.deepakjetpackcompose.medicalscan.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val name: String?=null,            // "Paracetamol 500mg"
    val description: String? = null, // optional (like "For fever & headache")
    val dosage: String?=null,          // "1 tablet"
    val frequencyPerDay: Int?=null,    // e.g. 3 times a day
    val durationDays: Int?=null,       // e.g. 5 days
    val startDate: Long?=null,         // store as timestamp (System.currentTimeMillis)
    val expiryDate: Long? = null,  // optional, from strip or manual input
    val stockCount: Int? = null,   // optional, number of tablets available
    val nextDoseTime: Long? = null, // next reminder timestamp
    val isActive: Boolean = true,   // active/inactive medicine schedule
)