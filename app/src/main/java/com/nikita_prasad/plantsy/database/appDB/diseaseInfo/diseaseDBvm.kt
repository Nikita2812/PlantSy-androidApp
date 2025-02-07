package com.nikita_prasad.plantsy.database.appDB.diseaseInfo

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.nikita_prasad.plantsy.database.appDB.appDataDB
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class diseaseDBvm(application: Application): AndroidViewModel(application) {

    private val diseaseRepo: diseaseRepo
    private val diseaseDAO: diseaseDAO


    init{
        diseaseDAO= appDataDB.getAppDBReference(application).diseaseDAO()
        diseaseRepo= diseaseRepo(diseaseDAO)
    }

    private suspend fun insertDiseases(DiseaseDC: DiseaseDC) {
        diseaseRepo.addDisease(DiseaseDC)
    }

    suspend fun readDiseases(): List<DiseaseDC>{
        return diseaseRepo.readDB()
    }
    suspend fun getDiseaseData(diseaseIndex: Long, plantIndex: Long): DiseaseDC {
        return diseaseRepo.getDiseaseData(diseaseIndex = diseaseIndex, plantIndex = plantIndex)
    }

    suspend fun getDomain(plantIndex: Long): String {
        return diseaseRepo.getDomain(plantIndex = plantIndex)
    }

    suspend fun fetchDiseaseData(
        onCompletion: () -> Unit
    ) {
        val dataInstance = Firebase.firestore
        try {
            for (data in dataInstance.collection("disease").get()
                .await().documents.map { documentSnapshot -> documentSnapshot.data }) {
                Log.d("dbStatus", data.toString())
                if (data != null) {
                    val cleanedData = DiseaseDC(
                        symptoms = data["symptoms"] as String,
                        domain = data["domain"] as String,
                        introduction = data["introductions"] as String,
                        cure = data["cure"] as String,
                        cure_cycle = data["cure_cycle"] as Long,
                        disease_name = data["disease_name"] as String,
                        diseaseIndex = data["diseaseIndex"] as Long,
                        cover_link = data["cover_link"] as String,
                        plantIndex = data["plantIndex"] as Long
                    )
                    insertDiseases(cleanedData)
                    Log.d("dbStatus", "after clearing: $cleanedData")
                    onCompletion()
                } else Log.e("dbStatus", "data is null")
            }
        } catch (e: Exception) {
            Log.d(
                "dbStatus",
                e.toString()
            )
        }
    }

    fun updateSelectedPlant(plantIndex: Long, plantName: String) {
        viewModelScope.launch {
            try {
                Log.d("PlantSelection", "Updated plant: $plantName (Index: $plantIndex)")
            }catch (e: Exception) {
                Log.e("PlantSelection", "Error updating plant", e)
            }
        }

    }
}

fun Map<String, Any?>.getStringOrDefault(key: String, default: String = ""): String {
    return this[key]?.toString() ?: default
}

// Extension function to safely get Long from Firestore
fun Map<String, Any?>.getLongOrDefault(key: String, default: Long = 0L): Long {
    return (this[key] as? Number)?.toLong() ?: default
}