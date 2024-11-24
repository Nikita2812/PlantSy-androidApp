package com.nikita_prasad.plantsy.database.appDB.diseaseInfo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "disease_dataset")
data class DiseaseDC(
    val cure: String = "",
    val cure_cycle: Long = 0L,
    @PrimaryKey(autoGenerate = false)
    val disease_name : String,
    val domain : String,
    val diseaseIndex: Long,
    val introduction : String,
    val symptoms : String,
    val cover_link : String,
    val plantIndex: Long
)