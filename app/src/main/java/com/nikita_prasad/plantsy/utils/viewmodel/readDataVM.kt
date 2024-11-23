package com.nikita_prasad.plantsy.utils.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class readDataVM : ViewModel() {

    suspend fun fetchDiseaseData() {
        // val list = mutableListOf< >()
        try {
            val dataInstance = Firebase.firestore
            for (data in dataInstance.collection("disease").get()
                .await().documents.map { documentSnapshot -> documentSnapshot.data }) {
                Log.d("dbStatus", data.toString())
            }
        } catch (e: Exception) {
            Log.d("dbStatus", e.toString())
        }
    }

}