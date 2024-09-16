package com.nikita_prasad.plantsy.utils.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nikita_prasad.plantsy.DataClass.Classification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScanVM: ViewModel(){

    private val _bitmaps = MutableStateFlow<Bitmap?>(null)
    var bitmaps = _bitmaps.asStateFlow()

    fun onTakePhoto(bitmap: Bitmap){
        _bitmaps.value = bitmap
    }
    fun onClearPhoto(){
        _bitmaps.value = null
    }

    private val _listPrediction = MutableStateFlow<List<Classification>>(emptyList())
    var unsortedClassification = _listPrediction.asStateFlow()

    private val _loadingBoolean = MutableStateFlow(true)
    val isLoadingBoolean = _loadingBoolean.asStateFlow()

    private val _reloadingBoolean = MutableStateFlow(false)
    val isReLoadingBoolean = _reloadingBoolean.asStateFlow()

    private val _normalBoolean = MutableStateFlow(false)
    val isNormalBoolean = _normalBoolean.asStateFlow()

    private val _erroredBoolean = MutableStateFlow(false)
    val isErroredBoolean = _erroredBoolean.asStateFlow()

    private val _classificationData = MutableStateFlow<List<Classification>>(emptyList())
    var classificationData = _classificationData.asStateFlow()





    suspend fun onClassify(context: Context, index: Int){
        _listPrediction.value = emptyList()
        _normalBoolean.value = false
        _reloadingBoolean.value = true
        val rawClassification = try {
            viewModelClassifer.classify(context = context, bitmap = _bitmaps.value!!, result = index)
        } catch (e: Exception){
            Log.d("successIndex", e.printStackTrace().toString())
            emptyList()
        }
        if (rawClassification.isEmpty()){
            _erroredBoolean.value = true
            _loadingBoolean.value = false
            _reloadingBoolean.value = false
        } else {
            _listPrediction.value = rawClassification.filter { it.indexNumber == 1 && it.confidence > 0.75 }
            if (_listPrediction.value.isEmpty()){
                _normalBoolean.value = true)
                _loadingBoolean.value = false
                _reloadingBoolean.value = false
            } else {
                classificationData(group_number = index)
            }

        }
        Log.d("successIndexVMNormal", _normalBoolean.value.toString())
        Log.d("successIndexVMErrored", _erroredBoolean.value.toString())
        Log.d("successIndexVMList", _listPrediction.value.toString())


    }
}