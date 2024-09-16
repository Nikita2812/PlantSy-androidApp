package com.nikita_prasad.plantsy.DataClass

import android.icu.text.AlphabeticIndex.ImmutableIndex

data class Classification(
    val index: Int,
    val confidence: Float,
    var parentindex: Int,
)
