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
}