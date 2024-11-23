package com.nikita_prasad.plantsy.utils.dataclass

data class Classification(
    val index: Int,
    val confidence: Float,
    var diseaseIndex: Int
)
