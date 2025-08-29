package com.deepakjetpackcompose.medicalscan.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.deepakjetpackcompose.medicalscan.R

@Composable
fun TopAppBarHome(modifier: Modifier = Modifier) {
    Row (modifier = modifier.fillMaxWidth()){
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(R.drawable.left_arrow),
                contentDescription = null
            )
        }
        Spacer(Modifier.weight(1f))

    }
}