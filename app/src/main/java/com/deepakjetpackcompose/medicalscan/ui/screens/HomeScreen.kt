package com.deepakjetpackcompose.medicalscan.ui.screens



import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deepakjetpackcompose.medicalscan.R
import com.deepakjetpackcompose.medicalscan.ui.viewmodel.MedicalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    medicines: List<String> = listOf("Paracetamol", "Ibuprofen", "Cough Syrup"),
    onScanClick: () -> Unit,
    onProfileClick: () -> Unit,
    onMedicineClick: (Int,String,String,String,Int,Int,Long,Long,Int,Long) -> Unit,
    medicalViewModel: MedicalViewModel= hiltViewModel<MedicalViewModel>()
) {

    val medicineList=medicalViewModel.medicineList.collectAsState()
    LaunchedEffect(Unit) {
        medicalViewModel.getAllMedicine()
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("MedicalScan", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color(0xFF1976D2) // medical blue
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onScanClick,
                containerColor = Color(0xFF1976D2),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Scan Medicine",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Hero Scan Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFBBDEFB) // light blue
                ),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Scan a New Medicine",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D47A1)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Quickly scan and save your medicines for easy tracking.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onScanClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1976D2)
                            ),
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White)
                            Spacer(Modifier.width(6.dp))
                            Text("Scan Now", color = Color.White)
                        }
                    }
                    Image(
                        painter = painterResource(id = R.drawable.scan),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(start = 8.dp)
                    )
                }
            }

            // History Section
            Text(
                text = "Previously Scanned",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
            )

            if (medicineList.value.isEmpty()) {
                // Empty State
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No medicines scanned yet.", color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = onScanClick,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Start Scanning")
                    }
                }
            } else {
                // List of Medicines
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(medicineList.value) { med ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(med.name, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                                    Text("Scanned on ${formatMillisToDate(med.startDate)}", fontSize = 12.sp, color = Color.Gray)
                                }
                                IconButton(onClick = { onMedicineClick(
                                    med.id?:0,
                                    med.name,
                                    med.description ?: "",
                                    med.dosage,
                                    med.frequencyPerDay,
                                    med.durationDays,
                                    med.startDate,
                                    med.expiryDate?:0,
                                    med.stockCount?:0,
                                    med.nextDoseTime?:0
                                ) }) {
                                    Icon(
                                        imageVector = Icons.Default.MedicalServices,
                                        contentDescription = "Details",
                                        tint = Color(0xFF1976D2)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



fun formatMillisToDate(millis: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return sdf.format(Date(millis))
}

