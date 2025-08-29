package com.deepakjetpackcompose.medicalscan.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deepakjetpackcompose.medicalscan.ui.components.AuthTextFieldComponent
import com.deepakjetpackcompose.medicalscan.ui.components.Loader
import com.deepakjetpackcompose.medicalscan.ui.components.OnBoardingComponent
import com.deepakjetpackcompose.medicalscan.ui.model.getListOfOnBoarding
import com.deepakjetpackcompose.medicalscan.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    authViewModel: AuthViewModel= hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    val isLoading=authViewModel.loading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Forgot Password", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        AuthTextFieldComponent(
            value = email,
            onValueChange = { email = it },
            label = "Enter your email",
            color = MaterialTheme.colorScheme.primary,
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isNotBlank()) {
                    authViewModel.sendPasswordReset(email) { success, msg ->
                        message = msg
                    }
                } else {
                    message = "Please enter a valid email"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset Password")
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Back to Login",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onBackClick() },
            textAlign = TextAlign.Center
        )

        message?.let {
            Spacer(Modifier.height(16.dp))
            Text(text = it, color = if (it.contains("sent")) Color.Green else Color.Red)
        }
    }
    if(isLoading.value){
        Loader(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White.copy(alpha = 0.7f)))
    }
}
