package com.nikita_prasad.plantsy.DataClass

data class Classification(
    val index: Int,
    val confidence: Float,
    var parentindex: Int
)
