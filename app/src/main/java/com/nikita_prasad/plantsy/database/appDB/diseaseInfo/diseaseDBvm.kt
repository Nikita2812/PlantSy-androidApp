package com.nikita_prasad.plantsy.database.appDB.diseaseInfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nikita_prasad.plantsy.database.appDB.appDataDB

class diseaseDBvm(application: Application): AndroidViewModel(application) {

    private val diseaseRepo: diseaseRepo
    private val diseaseDAO: diseaseDAO

    init{
        diseaseDAO= appDataDB.getAppDBReference(application).diseaseDAO()
        diseaseRepo= diseaseRepo(diseaseDAO)
    }

    suspend fun insertDiseases(DiseaseDC: DiseaseDC){
        diseaseRepo.addDisease(DiseaseDC)
    }

    suspend fun readDiseases(): List<DiseaseDC>{
        return diseaseRepo.readDB()
    }
    
}