package com.deepakjetpackcompose.medicalscan.ui.navigation

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    data object GetStarted : Routes()

    @Serializable
    data object Login : Routes()

    @Serializable
    data object SignUp : Routes()

    @Serializable
    data object Forget : Routes()

    @Serializable
    data object Home : Routes()

    @Serializable
    data object Camera : Routes()

    @Serializable
    data class Form(val scannedText:String?=null,
                    val id:Int?=null,
                    val name: String="",
                    val description: String? = null,
                    val dosage: String="",
                    val frequencyPerDay: Int?=null,
                    val durationDays: Int?=null,
                    val startDate: Long?=null,
                    val expiryDate: Long? = null,
                    val stockCount: Int? = null) : Routes()

    @Serializable
    data class MedicineDetails(
        val id:Int?=null,
        val name: String="",
        val description: String? = null,
        val dosage: String="",
        val frequencyPerDay: Int?=null,
        val durationDays: Int?=null,
        val startDate: Long?=null,
        val expiryDate: Long? = null,
        val stockCount: Int? = null,
        val nextDoseTime: Long? = null
    )
}
