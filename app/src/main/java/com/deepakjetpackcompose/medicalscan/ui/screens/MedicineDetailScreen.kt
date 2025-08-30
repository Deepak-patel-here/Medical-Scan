package com.deepakjetpackcompose.medicalscan.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deepakjetpackcompose.medicalscan.ui.model.Medicine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineDetailScreen(
    id:Int,
    name:String,
    description: String,
    dosage: String,
    frequencyPerDay: Int,
    durationDays: Int,
    startDate: Long,
    expiryDate: Long,
    stockCount: Int,
    nextDoseTime: Long,
    onEdit: (Medicine) -> Unit,
    onDelete: (Int) -> Unit,
    onBack: () -> Unit
) {

    val medicine= Medicine(
        name = name,
        description = description,
        dosage = dosage,
        frequencyPerDay = frequencyPerDay,
        durationDays = durationDays,
        startDate = startDate,
        expiryDate = expiryDate,
        stockCount = stockCount,
        nextDoseTime = nextDoseTime
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Medicine Details") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Medicine Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Divider()
                    Text(
                        text = " Description : $description",
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Frequency per day: ${frequencyPerDay}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Dosage: ${dosage}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Stock Count: ${stockCount}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Start Date: ${formatMillisToDate(startDate)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "End Date: ${formatMillisToDate(expiryDate)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onEdit(medicine) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                    Spacer(Modifier.width(8.dp))
                    Text("Edit")
                }
                OutlinedButton(
                    onClick = { onDelete(id) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                    Spacer(Modifier.width(8.dp))
                    Text("Delete")
                }
            }
        }
    }
}




