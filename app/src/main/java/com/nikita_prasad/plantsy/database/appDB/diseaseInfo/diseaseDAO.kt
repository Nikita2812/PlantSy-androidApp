package com.nikita_prasad.plantsy.database.appDB.diseaseInfo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface diseaseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDiseaseData(diseaseDC: DiseaseDC)

    @Query("SELECT * FROM disease_dataset")
    suspend fun readDB(): List<DiseaseDC>

    @Query("SELECT * FROM disease_dataset WHERE diseaseIndex = :diseaseIndex and plantIndex = :plantIndex")
    suspend fun getDiseaseData(diseaseIndex: Long, plantIndex: Long): DiseaseDC

    @Query("SELECT domain FROM disease_dataset where plantIndex = :plantIndex and diseaseIndex = 0")
    suspend fun getDomain(plantIndex: Long): String

}