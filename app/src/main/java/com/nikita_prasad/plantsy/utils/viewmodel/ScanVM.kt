package com.nikita_prasad.plantsy.utils.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.nikita_prasad.plantsy.database.appDB.diseaseInfo.DiseaseDC
import com.nikita_prasad.plantsy.database.appDB.diseaseInfo.diseaseDBvm
import com.nikita_prasad.plantsy.utils.dataclass.Classification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScanVM(application: Application): AndroidViewModel(application){

    private val _bitmaps = MutableStateFlow<Bitmap?>(null)
    var bitmaps = _bitmaps.asStateFlow()

    private val classifierVM = ClassifierVM()

    fun onTakePhoto(bitmap: Bitmap){
        _bitmaps.value = bitmap
    }

    val viewModelClassifier: ClassifierVM= ViewModelProvider.AndroidViewModelFactory
        .getInstance(application.applicationContext as Application)
        .create(
            ClassifierVM::class.java
        )

    /*val viewModelDiseaseDB= ViewModelProvider.AndroidViewModelFactory
        .getInstance(application.applicationContext as Application)
        .create(
            diseaseDBviewModel::class.java
        )*/

    private val _classificationData = MutableStateFlow<List<Classification>>(emptyList())
    var classificationData = _classificationData.asStateFlow()

    private val _erroredBoolean = MutableStateFlow(false)
    val isErroredBoolean = _erroredBoolean.asStateFlow()

    private val _listPrediction = MutableStateFlow<List<Classification>>(emptyList())
    var unsortedClassification = _listPrediction.asStateFlow()

    private val _loadingBoolean = MutableStateFlow(true)
    val isLoadingBoolean = _loadingBoolean.asStateFlow()

    private val _maxIndex=MutableStateFlow(
        Classification(index = 404, confidence = 0f, diseaseIndex = 404)
    )

    val maxIndex= _maxIndex.asStateFlow()

    private val _notMaxElements= MutableStateFlow<List<Classification>>(emptyList())
    val minIndex= _notMaxElements.asStateFlow()

    private val _normalBoolean = MutableStateFlow(false)
    val isNormalBoolean = _normalBoolean.asStateFlow()

    private val _reloadingBoolean = MutableStateFlow(false)
    val isReLoadingBoolean = _reloadingBoolean.asStateFlow()

    private val _dataLoaded = MutableStateFlow(
        DiseaseDC(
            "",
            Long.MAX_VALUE,
            "",
            "",
            Long.MAX_VALUE,
            "",
            "",
            "",
            Long.MAX_VALUE
        )
    )
    val data = _dataLoaded.asStateFlow()

    private lateinit var _diseaseDBvm: diseaseDBvm

  //  private val _updatedClassificationData= MutableStateFlow(disease_data_dataClass())
  fun initialize(diseaseDBvm: diseaseDBvm) {
      _diseaseDBvm = diseaseDBvm

  }

    suspend fun onClassify(context: Context, plantIndex: Int) {
        _listPrediction.value = emptyList()
        _normalBoolean.value = false
        _reloadingBoolean.value = true
        val rawClassification = try {
            viewModelClassifier.classify(
                context = context,
                bitmap = _bitmaps.value!!,
                result = plantIndex
            )
        } catch (e: Exception){
            Log.e("successIndex", e.printStackTrace().toString())
            emptyList()
        }
        if (rawClassification.isEmpty()){
            _erroredBoolean.value = true
            _loadingBoolean.value = false
            _reloadingBoolean.value = false
        } else {
            _listPrediction.value = rawClassification.filter { it.index == 1 && it.confidence > 0.75 }
            if (_listPrediction.value.isEmpty()){
                _normalBoolean.value = true
                _loadingBoolean.value = false
                _reloadingBoolean.value = false
            } else {
                findMaxConfidence()
                Log.d(
                    "dbSearchInput",
                    "maxIndex: ${maxIndex.value.diseaseIndex.toLong()} \n plantIndex: $plantIndex"
                )
                _dataLoaded.value = _diseaseDBvm.getDiseaseData(
                    _maxIndex.value.diseaseIndex.toLong(),
                    plantIndex.toLong()
                )
            }

        }
        Log.d("MNormal", _normalBoolean.value.toString())
        Log.d("successIndexVMErrored", _erroredBoolean.value.toString())
        Log.d("successIndexVMList", _listPrediction.value.toString())

    }

    private fun findMaxConfidence() {
        Log.d("filteredData", "before: ${_listPrediction.value}")
        var highestConfidenceDisease = _listPrediction.value[0]
        for (data in _listPrediction.value){
            if (data.confidence > highestConfidenceDisease.confidence) {
                highestConfidenceDisease = data
            }
        }
        _maxIndex.value = highestConfidenceDisease
        Log.d("filteredData", "after: $highestConfidenceDisease")
    }

    fun classify(context: Context, bitmap: Bitmap, result: Int): Int {
        return classifierVM.classify(context, bitmap, result)[0].index
    }

}