package com.nikita_prasad.plantsy.database.appDB.diseaseInfo

class diseaseRepo(private val diseaseDAO: diseaseDAO) {

    suspend fun addDisease(diseaseDC: DiseaseDC){
        diseaseDAO.addDiseaseData(diseaseDC = diseaseDC)
    }

    suspend fun readDB(): List<DiseaseDC>{
        return diseaseDAO.readDB()
    }
}