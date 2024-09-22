package com.nikita_prasad.plantsy.utils.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.nikita_prasad.plantsy.utils.dataclass.Classification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScanVM(application: Application): AndroidViewModel(application){

    private val _bitmaps = MutableStateFlow<Bitmap?>(null)
    var bitmaps = _bitmaps.asStateFlow()

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
        Classification(index = 404, confidence = 0f, parentindex = 404,)
    )

    val maxIndex= _maxIndex.asStateFlow()

    private val _notMaxElements= MutableStateFlow<List<Classification>>(emptyList())
    val minIndex= _notMaxElements.asStateFlow()

    private val _normalBoolean = MutableStateFlow(false)
    val isNormalBoolean = _normalBoolean.asStateFlow()

    private val _reloadingBoolean = MutableStateFlow(false)
    val isReLoadingBoolean = _reloadingBoolean.asStateFlow()

  //  private val _updatedClassificationData= MutableStateFlow(disease_data_dataClass())


    suspend fun onClassify(context: Context, index: Int){
        _listPrediction.value = emptyList()
        _normalBoolean.value = false
        _reloadingBoolean.value = true
        val rawClassification = try {
            viewModelClassifier.classify(context = context, bitmap = _bitmaps.value!!, result = index)
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
               // classificationData(group_number = index)
            }

        }
        sortClassifiedList()
        Log.d("MNormal", _normalBoolean.value.toString())
        Log.d("successIndexVMErrored", _erroredBoolean.value.toString())
        Log.d("successIndexVMList", _listPrediction.value.toString())


    }
    private fun sortClassifiedList(){
        val maxValue= _listPrediction.value.maxByOrNull { it.confidence }?.confidence ?:0
        val notMaxElements= mutableListOf<Classification>()

        Log.d("classificationData", _listPrediction.value.toString())
        for (data in _listPrediction.value){
            if (data.confidence==maxValue) _maxIndex.value= (data) else notMaxElements.add(data)
        }
        _notMaxElements.value= notMaxElements
        Log.d("classificationDataHighest", _maxIndex.value.toString())
        Log.d("classificationDataSorted", _notMaxElements.value.toString())
    }

}