package com.deepakjetpackcompose.medicalscan.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deepakjetpackcompose.medicalscan.ui.components.OnBoardingComponent
import com.deepakjetpackcompose.medicalscan.ui.model.getListOfOnBoarding
import kotlinx.coroutines.launch


@Composable
fun GetStartedScreen(onFinish: () -> Unit, modifier: Modifier = Modifier) {
    val listOfItem = getListOfOnBoarding()
    val pager = rememberPagerState(pageCount = { listOfItem.size })
    val scope = rememberCoroutineScope() // ✅ Coroutine scope for animations

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Pager
        HorizontalPager(
            state = pager,
            modifier = Modifier.weight(1f)
        ) { page ->
            val item = listOfItem[page]
            OnBoardingComponent(item = item)
        }

        // Indicators + Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dots indicator
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(listOfItem.size) { index ->
                    val selected = pager.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (selected) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected) MaterialTheme.colorScheme.primary
                                else Color.Gray.copy(alpha = 0.5f)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Next / Get Started Button
            Button(
                onClick = {
                    if (pager.currentPage == listOfItem.size - 1) {
                        onFinish()
                    } else {
                        scope.launch { // ✅ wrap suspend call here
                            pager.animateScrollToPage(pager.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(
                    text = if (pager.currentPage == listOfItem.size - 1) "Get Started"
                    else "Next"
                )
            }
        }
    }
}

