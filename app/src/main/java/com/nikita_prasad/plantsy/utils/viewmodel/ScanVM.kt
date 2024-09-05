package com.nikita_prasad.plantsy.utils.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
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
}