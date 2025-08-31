package com.deepakjetpackcompose.medicalscan.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.deepakjetpackcompose.medicalscan.ui.components.EditProfileComponent
import com.deepakjetpackcompose.medicalscan.ui.components.Loader
import com.deepakjetpackcompose.medicalscan.ui.viewmodel.AuthViewModel

import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userName: String = "Deepak Sharma",
    userEmail: String = "deepak@example.com",
    userImageUrl: String? = null,
    phone: String = "+91 9876543210",
    birthday: String = "12 Aug 2000",
    location: String = "New Delhi, India",
    isDialogShow: Boolean? = false,
    onEditClick: (Boolean) -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onBackClick: () -> Unit = {}, // 👈 back handler
    onDismissRequest: () -> Unit,
    onSaveClickedInDialog: (String, String, String, String, String, Uri?) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val colors = MaterialTheme.colorScheme
    val loader = authViewModel.firestoreLoader.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding) // respect scaffold padding
                .background(colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Profile picture
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(colors.surfaceVariant.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (userImageUrl != null) {
                    AsyncImage(
                        model = userImageUrl,
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .border(3.dp, colors.primary, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Icon",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        tint = colors.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User Info
            Text(userName, style = MaterialTheme.typography.headlineSmall, color = colors.onBackground)
            Text(userEmail, style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant)

            Spacer(modifier = Modifier.height(30.dp))

            // Card for details
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    ProfileRow("📱", "Phone", phone)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ProfileRow("🎂", "Birthday", birthday)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ProfileRow("📍", "Location", location)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Buttons
            Button(
                onClick = { onEditClick(true) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
            ) {
                Text("Edit Profile", color = colors.onPrimary)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.primary)
            ) {
                Text("Logout")
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        if (isDialogShow == true) {
            EditProfileComponent(
                initialName = userName,
                initialEmail = userEmail,
                initialBirthday = birthday,
                initialPhone = phone,
                initialLocation = location,
                initialImageUri = null,
                onDismissRequest = onDismissRequest,
                onSaveClick = { name, email, birthday, phone, location, imageUri ->
                    onSaveClickedInDialog(name, email, birthday, phone, location, imageUri)
                })
        }

        if (loader.value) {
            Loader(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White.copy(alpha = 0.7f))
            )
        }
    }
}


@Composable
fun ProfileRow(icon: String, title: String, value: String) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = MaterialTheme.typography.titleLarge.fontSize)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyLarge, color = colors.onSurface)
        }
    }
}

