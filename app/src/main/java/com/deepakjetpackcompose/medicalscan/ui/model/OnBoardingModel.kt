package com.deepakjetpackcompose.medicalscan.ui.model

import com.deepakjetpackcompose.medicalscan.R

data class OnBoardingModel(
    val title:String,
    val subtitle:String,
    val image:Int
)

fun getListOfOnBoarding(): List<OnBoardingModel>{
    val list=listOf<OnBoardingModel>(
        OnBoardingModel(
            title = "Quick Scan",
            subtitle = "Scan medicines instantly using your camera or barcode",
            image = R.drawable.scan
        ),
        OnBoardingModel(
            title = "Stay Organized",
            subtitle = "Track expiry dates and manage your medicines safely",
            image = R.drawable.medicine
        ),
        OnBoardingModel(
            title = "Health First",
            subtitle = "Get dosage, usage & side-effect details in seconds",
            image = R.drawable.health
        )
    )

    return list
}
