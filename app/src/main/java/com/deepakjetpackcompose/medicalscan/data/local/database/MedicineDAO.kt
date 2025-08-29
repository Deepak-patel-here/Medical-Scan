package com.deepakjetpackcompose.medicalscan.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.deepakjetpackcompose.medicalscan.data.local.dto.MedicineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDAO {

    @Upsert
    suspend fun addOrUpdateMedicine(medicine: MedicineEntity)

    @Query("DELETE FROM medicines WHERE id = :id")
    suspend fun deleteMedicine(id:Int)

    @Query("SELECT * FROM medicines ORDER BY name ASC")
    fun getAllMedicines(): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicines WHERE id = :id LIMIT 1")
    suspend fun getMedicineById(id: Int): MedicineEntity?

}