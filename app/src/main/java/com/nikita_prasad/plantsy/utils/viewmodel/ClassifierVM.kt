package com.nikita_prasad.plantsy.utils.viewmodel

import android.app.ApplicationExitInfo
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import com.nikita_prasad.plantsy.DataClass.Classification
import com.nikita_prasad.plantsy.ml.AppleBlackRot
import com.nikita_prasad.plantsy.ml.AppleScab
import com.nikita_prasad.plantsy.ml.CedarAppleRust
import com.nikita_prasad.plantsy.ml.Cherry
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class ClassifierVM: ViewModel() {

    fun classify(
        context: Context,
        bitmap: Bitmap,
        result: Int,
        index: Int=0
    ): List<Classification> {
        var predictedClasses: List<Classification>

        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)
        var imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(256, 256, ResizeOp.ResizeMethod.BILINEAR))
            .build()
        tensorImage = imageProcessor.process(tensorImage)
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)
        Log.d("inputString", "result: $result, index: $index")

        predictedClasses = when (result) {
            0 -> { predictionApple(inputFeature0 = inputFeature0, context = context) }
            //1 -> { predictionBellPepper(inputFeature0 = inputFeature0, context = context)}
            else -> { predictionApple(inputFeature0 = inputFeature0, context = context) }
        }

        return predictedClasses
    }

    private fun predictionApple(
        context: Context,
        inputFeature0: TensorBuffer
    ): List<Classification> {
        val predictedClass= mutableListOf<Classification>()
        val outputScab= AppleScab
            .newInstance(context)
            .process(inputFeature0)
            .outputFeature0AsTensorBuffer
        val maxIndexScab= getMaxIndex(outputScab.floatArray)
        predictedClass.add(
            Classification(maxIndexScab, outputScab.floatArray[maxIndexScab]*100, 0)
        )
        val outputPre= AppleBlackRot
            .newInstance(context)
            .process(inputFeature0)
            .outputFeature0AsTensorBuffer
        val maxIndexPre= getMaxIndex(outputPre.floatArray)
        predictedClass.add(
            Classification(maxIndexPre, outputPre.floatArray[maxIndexPre]*100, 1)
        )
        val outputPreRust= CedarAppleRust
            .newInstance(context)
            .process(inputFeature0)
            .outputFeature0AsTensorBuffer
        val maxIndexPreRust= getMaxIndex(outputPreRust.floatArray)
        predictedClass.add(
            Classification(maxIndexPreRust, outputPreRust.floatArray[maxIndexPreRust]*100, 2)
        )
        return predictedClass
    }

    private fun getMaxIndex(floatArray: FloatArray): Int {
        return floatArray.withIndex().maxByOrNull { it.value }?.index ?: -1
    }

    /*private fun predictionBellPepper(
        context: Context,
        inputFeature0: TensorBuffer
    ): List<Classification> {
        val predictedClass= mutableListOf<Classification>()
        val outputPre= BellPepper
            .newInstance(context)
            .process(inputFeature0)
            .outputFeature0AsTensorBuffer
        val maxIndexPre= getMaxIndex(outputPre.floatArray)
        predictedClass.add(
            Classification(maxIndexPre, outputPre.floatArray[maxIndexPre]*100, 0)
        )
        val outputPre2= bellpep2
            .newInstance(context)
            .process(inputFeature0)
            .outputFeature0AsTensorBuffer
        val maxIndexPre2= getMaxIndex(outputPre2.floatArray)
        predictedClass.add(
            Classification(maxIndexPre2, outputPre2.floatArray[maxIndexPre2]*100, 0)
        ) 
        return predictedClass

   }*/

    private fun predictionCherry(
        context: Context,
        inputFeature0: TensorBuffer
    ): List<Classification> {
        val predictedClass= mutableListOf<Classification>()
        val outputcherry= Cherry
            .newInstance(context)
            .process(inputFeature0)
            .outputFeature0AsTensorBuffer
        val maxIndexcherry= getMaxIndex(outputcherry.floatArray)
        predictedClass.add(
            Classification(maxIndexcherry, outputcherry.floatArray[maxIndexcherry]*100, 0)
        )
        return predictedClass
    }

    private fun predictionCitrusBlackSpot(
        context: Context,
        inputFeature0: TensorBuffer
    ): List<Classification> {
        val predictedClass = mutableListOf<Classification>()
        val outputcitrus = Cherry
            .newInstance(context)
            .process(inputFeature0)
            .outputFeature0AsTensorBuffer
        val maxIndexcitrus = getMaxIndex(outputcitrus.floatArray)
        predictedClass.add(
            Classification(maxIndexcitrus, outputcitrus.floatArray[maxIndexcitrus] * 100, 0)
        )
        return predictedClass
    }
}