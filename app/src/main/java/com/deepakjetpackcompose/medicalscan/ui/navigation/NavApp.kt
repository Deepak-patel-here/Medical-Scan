package com.deepakjetpackcompose.medicalscan.ui.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deepakjetpackcompose.medicalscan.ui.screens.CameraScreen
import com.deepakjetpackcompose.medicalscan.ui.screens.ForgotPasswordScreen
import com.deepakjetpackcompose.medicalscan.ui.screens.GetStartedScreen
import com.deepakjetpackcompose.medicalscan.ui.screens.HomeScreen
import com.deepakjetpackcompose.medicalscan.ui.screens.LoginScreen
import com.deepakjetpackcompose.medicalscan.ui.screens.SignUpScreen
import com.deepakjetpackcompose.medicalscan.ui.viewmodel.AuthState
import com.deepakjetpackcompose.medicalscan.ui.viewmodel.AuthViewModel
import android.graphics.Bitmap
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.toRoute
import com.deepakjetpackcompose.medicalscan.ui.screens.FormScreen
import com.deepakjetpackcompose.medicalscan.ui.screens.MedicineDetailScreen
import com.deepakjetpackcompose.medicalscan.ui.screens.ProfileScreen
import com.deepakjetpackcompose.medicalscan.ui.util.scheduleMedicineReminders
import com.deepakjetpackcompose.medicalscan.ui.viewmodel.MedicalViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Composable
fun NavApp(modifier: Modifier = Modifier,authViewModel: AuthViewModel= hiltViewModel(),medicalViewModel: MedicalViewModel=hiltViewModel<MedicalViewModel>()) {

    val navController= rememberNavController()
    val authState=authViewModel.authState.collectAsState()
    val start=if(authState.value== AuthState.Success) Routes.Home else Routes.GetStarted
    val context= LocalContext.current
    val isShow=remember { mutableStateOf(false) }
    val userInfo=authViewModel.userinfo.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.getUserInfo()
    }



    NavHost(navController = navController, startDestination = start) {
        composable < Routes.GetStarted>{
            GetStartedScreen(onFinish = {
                navController.navigate(Routes.Login){
                    popUpTo(Routes.GetStarted){inclusive=true}
                }
            })
        }
        composable < Routes.Login>{
            LoginScreen(
                onLoginClick = {email,password->
                authViewModel.login(email = email, password = password)
                },
                onSignUpClick = {navController.navigate(
                Routes.SignUp){
                popUpTo(Routes.Login){inclusive=true} } },
                onForgotPasswordClick = {navController.navigate(Routes.Forget)},
                navController = navController)
        }
        composable < Routes.SignUp>{
            SignUpScreen(onSignUpClick = {name,email,password->
                authViewModel.signUp(name = name, email = email, password = password)
                },
                onLoginClick = {
                navController.navigate(
                    Routes.Login){
                    popUpTo(Routes.SignUp){inclusive=true}
                }
                },
                navController = navController
            )

        }

        composable <Routes.Forget>{
            ForgotPasswordScreen(onBackClick = {navController.popBackStack()})
        }

        composable <Routes.Home>{
            HomeScreen(onScanClick = {
                navController.navigate(Routes.Camera)
            }, onProfileClick = {
                navController.navigate(Routes.ProfileScreen)
            }, onMedicineClick = {id,name,description,dosage,frequencyPerDay,durationDays,startDate,expiryDate,stockCount,nextDoseTime->
                navController.navigate(Routes.MedicineDetails(
                    id = id,
                    name = name,
                    description = description,
                    dosage = dosage,
                    frequencyPerDay = frequencyPerDay,
                    durationDays = durationDays,
                    startDate = startDate,
                    expiryDate = expiryDate,
                    stockCount = stockCount,
                    nextDoseTime = nextDoseTime
                ))
            })
        }

        composable<Routes.Camera> {
            CameraScreen(onImageCaptured = {image->
               imageToText(bitmap = image, onResult = {text->
                   navController.navigate(Routes.Form(scannedText = text))
               }, onError = {error->
                   Toast.makeText(context, error?:"unKnown error occurred", Toast.LENGTH_SHORT).show()
               })

            }, onError = {
                Toast.makeText(context, it?:"unKnown error occurred", Toast.LENGTH_SHORT).show()
            },
                onBack = {
                    navController.popBackStack()
                })
        }

        composable < Routes.Form>{
            val data=it.toRoute<Routes.Form>()
            FormScreen(
                data = data.scannedText,
                id=data.id,
                name = data.name,
                description = data.description,
                dosage = data.dosage,
                frequencyPerDay = data.frequencyPerDay,
                durationDays = data.durationDays,
                startDate = data.startDate,
                expiryDate = data.expiryDate,
                stockCount = data.stockCount,
                onBack = {navController.popBackStack()}, onSave = {medicine->
                try {
                    scheduleMedicineReminders(
                        context = context,
                        medicineName = medicine.name,
                        frequency = medicine.frequencyPerDay
                    )
                    medicalViewModel.addMedicine(medicine)
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Home) { inclusive = true }
                    }
                    Toast.makeText(context, "Medicine saved & reminder set!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Failed to set reminder: ${e.message}", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }


            })
        }


        composable <Routes.MedicineDetails>{
            val data=it.toRoute<Routes.MedicineDetails>()
            MedicineDetailScreen(
                id = data.id?:0,
                name=data.name,
                description = data.description?:"no description about this medicine",
                dosage = data.dosage,
                frequencyPerDay = data.frequencyPerDay?:0,
                durationDays = data.durationDays?:0,
                startDate = data.startDate?:0,
                expiryDate = data.expiryDate?:0,
                stockCount = data.stockCount?:0,
                nextDoseTime = data.nextDoseTime?:0,
                onEdit={medicine->
                    navController.navigate(Routes.Form(
                        scannedText =null,
                        id=medicine.id,
                        name = medicine.name,
                        description = medicine.description,
                        dosage = medicine.dosage,
                        frequencyPerDay = medicine.frequencyPerDay,
                        durationDays = medicine.durationDays,
                        startDate = medicine.startDate,
                        expiryDate = medicine.expiryDate,
                        stockCount = medicine.stockCount
                    ))
                },
                onBack = {navController.popBackStack()},
                onDelete = {id->
                    medicalViewModel.deleteMedicine(id)
                    navController.popBackStack()
                }
            )
        }


        composable <Routes.ProfileScreen>{
            ProfileScreen(
                userName = userInfo.value.name?:"No Name present",
                userEmail = userInfo.value.email?:"No email",
                userImageUrl = userInfo.value.imageUrl,
                phone = userInfo.value.phone?:"No phone number",
                location = userInfo.value.location?:"No location",
                birthday = userInfo.value.birthday?:"No birthday",
                isDialogShow = isShow.value,
                onEditClick = {
                    isShow.value=it
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onLogoutClick = {
                    authViewModel.logout()
                },
                onDismissRequest = {isShow.value=false},
                onSaveClickedInDialog = {name,email,birthday,phone,location,imageUri->

                    authViewModel.saveUserInfo(context = context, name=name,email=email, birthday = birthday, phone = phone, location = location, imageUri =imageUri ,update = true)
                }
            )
        }
    }



}



fun imageToText(bitmap: Bitmap,onResult: (String)->Unit,onError:(String)->Unit){
    val image= InputImage.fromBitmap(bitmap,0)
    val recognizer= TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    recognizer.process(image)
        .addOnSuccessListener { visionText->
            onResult(visionText.text)
        }
        .addOnFailureListener { error->
            onError(error.message?:"unKnown error occurred")
        }

}