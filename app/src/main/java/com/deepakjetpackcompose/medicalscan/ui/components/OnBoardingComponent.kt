package com.deepakjetpackcompose.medicalscan.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deepakjetpackcompose.medicalscan.R
import com.deepakjetpackcompose.medicalscan.ui.model.OnBoardingModel

@Composable
fun OnBoardingComponent(item: OnBoardingModel, modifier: Modifier = Modifier) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Image(
            painter = painterResource(item.image),
            contentDescription =item.title,
            modifier= Modifier.size(300.dp).weight(0.5f)
        )
        Text(text = item.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center)
        Spacer(modifier = Modifier
            .height(12.dp))
        Text(" \"${item.subtitle}\"",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center)


    }

}