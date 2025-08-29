package com.deepakjetpackcompose.medicalscan.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.deepakjetpackcompose.medicalscan.ui.components.AuthTextFieldComponent
import com.deepakjetpackcompose.medicalscan.R
import com.deepakjetpackcompose.medicalscan.ui.components.Loader
import com.deepakjetpackcompose.medicalscan.ui.navigation.Routes
import com.deepakjetpackcompose.medicalscan.ui.viewmodel.AuthState
import com.deepakjetpackcompose.medicalscan.ui.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(
    onSignUpClick: (String, String, String) -> Unit, // name, email, password
    onLoginClick: () -> Unit,
    navController: NavController,
    authViewModel: AuthViewModel= hiltViewModel<AuthViewModel>()


) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val authState=authViewModel.authState.collectAsState()
    val context= LocalContext.current

    LaunchedEffect(authState.value) {
        when (val state = authState.value) {
            is AuthState.Success -> {
                navController.navigate(Routes.Home) {
                    popUpTo(Routes.SignUp) { inclusive = true }
                }
            }
            is AuthState.Error -> {
                Toast.makeText(context, state.msg, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Signup Here",
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "create account to continue!",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Full Name
        AuthTextFieldComponent(
            value = name,
            onValueChange = { name = it },
            label = "Full Name",
            color = MaterialTheme.colorScheme.primary,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email
        AuthTextFieldComponent(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            color = MaterialTheme.colorScheme.primary,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        AuthTextFieldComponent(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            color = MaterialTheme.colorScheme.primary,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(if (passwordVisible) R.drawable.show else R.drawable.hidden),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        modifier = Modifier.size(23.dp)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password
        AuthTextFieldComponent(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirm Password",
            color = MaterialTheme.colorScheme.primary,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
            },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        painter = painterResource(if (passwordVisible) R.drawable.show else R.drawable.hidden),
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                        modifier = Modifier.size(23.dp)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Sign Up Button
        Button(
            onClick = {
                if (name.isNotBlank() && email.isNotBlank() && password == confirmPassword) {
                    authViewModel.signUp(email = email,password=password,name=name)
                }else{
                    Toast.makeText(context, "please fill the field correctly.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Sign Up")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Already have an account? Login
        Row {
            Text(text = "Already have an account? ")
            Text(
                text = "Login",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable { onLoginClick() }
            )
        }
    }
    if(authState.value== AuthState.Loading){
        Loader(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White.copy(alpha = 0.7f)))
    }
}
