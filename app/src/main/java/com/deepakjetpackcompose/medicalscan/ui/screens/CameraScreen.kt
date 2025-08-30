package com.deepakjetpackcompose.medicalscan.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.MeteringPointFactory
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.deepakjetpackcompose.medicalscan.ui.components.ShutterEffect
import com.deepakjetpackcompose.medicalscan.ui.util.playShutterSound
import java.io.File

@Composable
fun CameraScreen(
    onImageCaptured: (Bitmap) -> Unit,
    onError: (String) -> Unit,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var showFlash by remember { mutableStateOf(false) }

    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var camera: Camera? by remember { mutableStateOf(null) }


    val imageLauncher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri->
            uri?.let{
                try{
                    val inputStream=context.contentResolver.openInputStream(it)
                    val bitmap=BitmapFactory.decodeStream(inputStream)
                    onImageCaptured(bitmap)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

        }
    )
    Box(modifier = Modifier.fillMaxSize()) {
        // Camera Preview
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    imageCapture = ImageCapture.Builder()
                        .setTargetRotation(previewView.display.rotation)
                        .build()

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageCapture
                    )
                }, ContextCompat.getMainExecutor(ctx))

                previewView.setOnTouchListener { _, event ->
                    if (event.action == android.view.MotionEvent.ACTION_UP) {
                        val factory: MeteringPointFactory = previewView.meteringPointFactory
                        val point = factory.createPoint(event.x, event.y)

                        val action = FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
                            .setAutoCancelDuration(3, java.util.concurrent.TimeUnit.SECONDS)
                            .build()

                        camera?.cameraControl?.startFocusAndMetering(action)
                    }
                    true
                }

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay (for better visibility of controls)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f)),
                        startY = 600f
                    )
                )
        )

        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 30.dp, horizontal = 16.dp)
                .align(Alignment.TopStart),

            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(Modifier.width(8.dp))
            Text("Scan Medicine", color = Color.White, style = MaterialTheme.typography.titleMedium)
        }

        ShutterEffect(trigger = showFlash) {
            showFlash = false
        }



        // Capture Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                    playShutterSound()
                    // Trigger flash
                    showFlash = true
                    val file = File(
                        context.cacheDir,
                        "medicine_${System.currentTimeMillis()}.jpg"
                    )
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                    imageCapture?.takePicture(
                        outputOptions,
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                                onImageCaptured(bitmap)
                            }

                            override fun onError(exc: ImageCaptureException) {
                                onError(exc.message ?: "Capture failed")
                            }
                        }
                    )
                },
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.White, CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.Black, CircleShape)
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(bottom = 40.dp, end = 32.dp)
                .align(Alignment.BottomEnd), // 👈 direct alignment inside parent
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                    imageLauncher.launch("image/*")

                },
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Photo,
                    contentDescription = "Open Gallery",
                    modifier = Modifier.size(40.dp),
                    tint = Color.Black
                )
            }
        }


    }


}
