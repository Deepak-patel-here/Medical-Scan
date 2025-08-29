package com.deepakjetpackcompose.medicalscan.di

import android.content.Context
import androidx.room.Room
import com.deepakjetpackcompose.medicalscan.data.local.database.MedicineDAO
import com.deepakjetpackcompose.medicalscan.data.local.database.MedicineDatabase
import com.deepakjetpackcompose.medicalscan.data.remote.repository.MedicalApiRepository
import com.deepakjetpackcompose.medicalscan.data.remote.service.MedicineService
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): MedicineDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = MedicineDatabase::class.java,
            name = "medicine_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(db: MedicineDatabase): MedicineDAO =db.getDao()

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth= FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideMedicineService(): MedicineService{
        val retrofit= Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(MedicineService::class.java)
    }

    @Provides
    @Singleton
    fun provideMedicalRepo(service: MedicineService): MedicalApiRepository= MedicalApiRepository(service = service)
}