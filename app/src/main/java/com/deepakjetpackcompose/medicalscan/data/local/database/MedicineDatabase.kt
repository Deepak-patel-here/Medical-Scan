package com.deepakjetpackcompose.medicalscan.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.deepakjetpackcompose.medicalscan.data.local.dto.MedicineEntity

@Database(entities = [MedicineEntity::class], version = 3, exportSchema = true)
abstract class MedicineDatabase: RoomDatabase() {
    abstract fun getDao(): MedicineDAO
}