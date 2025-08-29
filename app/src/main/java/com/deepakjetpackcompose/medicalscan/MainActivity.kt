package com.deepakjetpackcompose.medicalscan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.deepakjetpackcompose.medicalscan.ui.navigation.NavApp
import com.deepakjetpackcompose.medicalscan.ui.theme.MedicalScanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current

            // 👇 State to hold permission status
            var hasCameraPermission = remember {
                mutableStateOf(
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                )
            }

            // 👇 Register launcher inside Compose using rememberLauncherForActivityResult
            val requestCameraPermission = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { granted ->
                hasCameraPermission.value = granted
                if (granted) {
                    Toast.makeText(context, "Camera permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
                }
            }

            MedicalScanTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (hasCameraPermission.value) {
                        // ✅ Show your app when permission is granted
                        NavApp(modifier = Modifier.padding(innerPadding))
                    } else {
                        // 👇 Ask for permission only when needed
                        requestCameraPermission.launch(Manifest.permission.CAMERA)
                        Text("Requesting camera permission…")
                    }
                }
            }
        }
    }
}


