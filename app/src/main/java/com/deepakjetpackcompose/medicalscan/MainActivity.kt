package com.deepakjetpackcompose.medicalscan

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deepakjetpackcompose.medicalscan.ui.components.CameraPermissionTextProvider
import com.deepakjetpackcompose.medicalscan.ui.components.PermissionDialog
import com.deepakjetpackcompose.medicalscan.ui.navigation.NavApp
import com.deepakjetpackcompose.medicalscan.ui.theme.MedicalScanTheme
import com.deepakjetpackcompose.medicalscan.ui.viewmodel.PermissionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val multiplePermission=arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.POST_NOTIFICATIONS
    )

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MedicalScanTheme {
                val viewModel= viewModel<PermissionViewModel>()
                val dialogQueue=viewModel.visiblePermissionDialogQueue
                val context= LocalContext.current



                val multiplePermissionResultLauncher= rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = {perms->
                        multiplePermission.forEach {permission->
                            viewModel.onPermissionResult(
                                permission = permission,
                                isGranted = perms[permission] == true
                            )
                        }

                    }
                )
                LaunchedEffect(Unit) {
                    multiplePermissionResultLauncher.launch(
                        multiplePermission
                    )
                }
                dialogQueue.reversed().forEach { permission ->
                    PermissionDialog(
                        permissionTextProvider = when (permission) {
                            Manifest.permission.CAMERA -> {
                                CameraPermissionTextProvider()
                            }

                            Manifest.permission.POST_NOTIFICATIONS -> {
                                CameraPermissionTextProvider()
                            }

                            else -> return@forEach
                        },
                        onDismiss = viewModel::dismissDialog,
                        onOkClick = {
                            viewModel.dismissDialog()
                            multiplePermissionResultLauncher.launch(
                                arrayOf(
                                    permission
                                )
                            )
                        },
                        onGoToAppSettingsClick = { openAppSettings(context) },
                        isPermanentlyDeclined = !shouldShowRequestPermissionRationale(permission)
                    )
                }



                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavApp(modifier = Modifier.padding(innerPadding))
                }

            }
        }
    }

    private fun openAppSettings(context: Context) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )
        context.startActivity(intent)
    }
}




