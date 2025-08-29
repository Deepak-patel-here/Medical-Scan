package com.deepakjetpackcompose.medicalscan.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepakjetpackcompose.medicalscan.data.local.database.MedicineDAO
import com.deepakjetpackcompose.medicalscan.data.local.dto.MedicineEntity
import com.deepakjetpackcompose.medicalscan.data.remote.repository.MedicalApiRepository
import com.deepakjetpackcompose.medicalscan.ui.model.Medicine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicalViewModel @Inject constructor(private val repository: MedicalApiRepository,private val medicineDAO: MedicineDAO): ViewModel() {

    private val _medicalData= MutableStateFlow<ApiState>(ApiState.Idle)
    val medicalData= _medicalData.asStateFlow()

    private val _medicineList= MutableStateFlow<List<Medicine>>(emptyList())
    val medicineList= _medicineList.asStateFlow()

    private val _loader=MutableStateFlow(false)
    val loader= _loader.asStateFlow()


    fun getData(text:String){
        viewModelScope.launch (Dispatchers.IO){
            _medicalData.value= ApiState.Loading
            try {
                val response= repository.getData(text)
                Log.d("api",response.toString())
                if(response==null){
                    _medicalData.value= ApiState.Error("Something went wrong")
                    return@launch
                }
                _medicalData.value= ApiState.Success(
                        name = response.name,
                        description = response.description,
                        dosage = response.dosage,
                        frequencyPerDay = response.frequencyPerDay,
                        expiryDate = response.expiryDate
                )
            }catch (e: Exception){
                _medicalData.value= ApiState.Error(e.localizedMessage)
                Log.e("api",e.localizedMessage.toString())
            }
        }
    }


    fun addMedicine(medicine: Medicine) {
        viewModelScope.launch(Dispatchers.IO) {
            _loader.value = true
            try {
                val medicineEntity = convertToMedicineDto(medicine).copy(id = medicine.id)
                medicineDAO.addOrUpdateMedicine(medicineEntity)
            } catch (e: Exception) {
                Log.e("api", e.localizedMessage ?: "Unknown error", e)
            } finally {
                _loader.value = false
            }
        }
    }




    fun deleteMedicine(id:Int){
        viewModelScope.launch (Dispatchers.IO){
            _loader.value=true
            try {
                medicineDAO.deleteMedicine(id)
            }catch (e: Exception){
                Log.e("api",e.localizedMessage.toString())
            }finally {
                _loader.value=false
            }
        }
    }

    fun getAllMedicine(){
        viewModelScope.launch (Dispatchers.IO){
            _loader.value=true
            try {
                medicineDAO.getAllMedicines().collect{
                    _medicineList.value=it.map { medicineEntity ->
                        Medicine(
                            id = medicineEntity.id,
                            name = medicineEntity.name?:"unknown",
                            description = medicineEntity.description?:"no description",
                            dosage = medicineEntity.dosage?:"N/A",
                            frequencyPerDay = medicineEntity.frequencyPerDay?:1,
                            durationDays = medicineEntity.durationDays?:1,
                            startDate = medicineEntity.startDate?: System.currentTimeMillis(),
                            expiryDate = medicineEntity.expiryDate,
                            stockCount = medicineEntity.stockCount,
                            nextDoseTime = medicineEntity.nextDoseTime
                        )
                    }
                }
            }catch (e: Exception){
                Log.e("api",e.localizedMessage.toString())
            }finally {
                _loader.value=false
            }
        }
    }



}

fun convertToMedicineDto(medicine: Medicine): MedicineEntity {
    return MedicineEntity(
        name = medicine.name.ifEmpty { "Unnamed" },
        description = medicine.description ?: "No description",
        dosage = medicine.dosage ?: "N/A",
        frequencyPerDay = medicine.frequencyPerDay ?: 0,
        durationDays = medicine.durationDays ?: 0,
        startDate = medicine.startDate ?: System.currentTimeMillis(),
        expiryDate = medicine.expiryDate ?: 0L,
        stockCount = medicine.stockCount ?: 0,
        nextDoseTime = medicine.nextDoseTime ?: 0L
    )
}


sealed class ApiState{
    object Loading: ApiState()
    object Idle: ApiState()
    data class Success(
        val name: String,
        val description: String? = null,
        val dosage: String,
        val frequencyPerDay: Int,
        val expiryDate: Long? = null,
    ): ApiState()
    data class Error(val msg:String): ApiState()

}