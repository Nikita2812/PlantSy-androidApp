package com.nikita_prasad.plantsy.database.appDB.diseaseInfo

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.nikita_prasad.plantsy.database.appDB.appDataDB
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

    suspend fun fetchDiseaseData() {
        try {
            val dataInstance = Firebase.firestore
            for (data in dataInstance.collection("disease").get()
                .await().documents.map { documentSnapshot -> documentSnapshot.data }) {
                Log.d("dbStatus", data.toString())
                val cleanedData = DiseaseDC(
                    symptoms = data?.get("symptoms") as String,
                    domain = data["domain"] as String,
                    introduction = data["introduction"] as String,
                    cure = data["cure"] as String,
                    cure_cycle = data["cure_cycle"] as Long,
                    disease_name = data["disease_name"] as String,
                    diseaseIndex = data["diseaseIndex"] as Long,
                    cover_link = data["cover_link"] as String,
                    plantIndex = data["plantIndex"] as Long
                )
                insertDiseases(cleanedData)
                Log.d("dbStatus", "after clearing: $cleanedData")
            }
        } catch (e: Exception) {
            Log.d(
                "dbStatus",
                e.toString()
            ) // [] me jo h it should exactly match the keys in every documents match ki ho?
        }
    }
}