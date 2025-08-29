package com.deepakjetpackcompose.medicalscan.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deepakjetpackcompose.medicalscan.ui.components.Loader
import com.deepakjetpackcompose.medicalscan.ui.model.Medicine
import com.deepakjetpackcompose.medicalscan.ui.viewmodel.ApiState
import com.deepakjetpackcompose.medicalscan.ui.viewmodel.MedicalViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    data:String?=null,
    id:Int?=null,
    name:String?=null,
    description:String?=null,
    dosage:String?=null,
    frequencyPerDay:Int?=null,
    durationDays:Int?=null,
    startDate:Long?=null,
    expiryDate:Long?=null,
    stockCount:Int?=null,
    onBack: () -> Unit,
    onSave: (Medicine) -> Unit,
    medicalViewModel: MedicalViewModel= hiltViewModel<MedicalViewModel>()
) {
    val context = LocalContext.current
    val apiState= medicalViewModel.medicalData.collectAsState()

    var name by remember { mutableStateOf(name?:"") }
    var description by remember { mutableStateOf(description?:"") }
    var dosage by remember { mutableStateOf(dosage?:"") }
    var frequencyPerDay by remember { mutableStateOf(frequencyPerDay?.toString()?:"") }
    var durationDays by remember { mutableStateOf(durationDays?.toString()?:"") }
    var startDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var expiryDate by remember { mutableStateOf<Long?>(expiryDate) }
    var stockCount by remember { mutableStateOf(stockCount?.toString()?:"") }

    LaunchedEffect(Unit) {
        if(data!=null) medicalViewModel.getData(data)
    }

    LaunchedEffect(apiState.value) {
        when (val state = apiState.value) {
            is ApiState.Success -> {
                name = state.name
                description = state.description ?: ""
                dosage = state.dosage
                frequencyPerDay=state.frequencyPerDay.toString()
                expiryDate=state.expiryDate
            }
            is ApiState.Error -> {
                Toast.makeText(context, state.msg, Toast.LENGTH_SHORT).show()

            }
            else->{}
        }
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Medicine") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Medicine Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true

            )

            OutlinedTextField(
                value = dosage,
                onValueChange = { dosage = it },
                label = { Text("Dosage (e.g. 1 tablet)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true

            )

            OutlinedTextField(
                value = frequencyPerDay,
                onValueChange = { frequencyPerDay=it },
                label = { Text("Frequency per day") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true

            )

            OutlinedTextField(
                value = durationDays,
                onValueChange = { durationDays = it },
                label = { Text("Duration (days)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true

            )

            DatePickerField(
                label = "Start Date",
                initialDate = startDate,
                onDateSelected = { startDate = it }
            )

            DatePickerField(
                label = "Expiry Date (Optional)",
                initialDate = expiryDate,
                onDateSelected = { expiryDate = it }
            )

            OutlinedTextField(
                value = stockCount,
                onValueChange = { stockCount = it },
                label = { Text("Stock Count (optional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (name.isBlank() || dosage.isBlank() ||
                        frequencyPerDay.isBlank() || durationDays.isBlank()
                    ) {
                        Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if(id==null) {
                        val medicine = Medicine(
                            name = name,
                            description = description.ifBlank { null },
                            dosage = dosage,
                            frequencyPerDay = frequencyPerDay.toIntOrNull() ?: 1,
                            durationDays = durationDays.toIntOrNull() ?: 1,
                            startDate = startDate,
                            expiryDate = expiryDate,
                            stockCount = stockCount.toIntOrNull()
                        )
                        onSave(medicine)
                        Toast.makeText(context, "Medicine Saved & Reminder Set!", Toast.LENGTH_SHORT).show()
                    }else{
                        val medicine = Medicine(
                            id=id,
                            name = name,
                            description = description.ifBlank { null },
                            dosage = dosage,
                            frequencyPerDay = frequencyPerDay.toIntOrNull() ?: 1,
                            durationDays = durationDays.toIntOrNull() ?: 1,
                            startDate = startDate,
                            expiryDate = expiryDate,
                            stockCount = stockCount.toIntOrNull()
                        )
                        onSave(medicine)
                        Toast.makeText(context, "Medicine Updated & Reminder Set!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Save & Set Reminder")
            }
        }
    }
    if(apiState.value== ApiState.Loading){
        Loader(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White.copy(alpha = 0.7f)))
    }
}

@Composable
fun DatePickerField(
    label: String,
    initialDate: Long?,
    onDateSelected: (Long) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply {
        timeInMillis = initialDate ?: System.currentTimeMillis()
    }

    val dateText = remember(initialDate) {
        initialDate?.let {
            java.text.SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it))
        } ?: "Select Date"
    }

    OutlinedButton(
        onClick = {
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val cal = Calendar.getInstance()
                    cal.set(year, month, dayOfMonth)
                    onDateSelected(cal.timeInMillis)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("$label: $dateText")
    }
}


