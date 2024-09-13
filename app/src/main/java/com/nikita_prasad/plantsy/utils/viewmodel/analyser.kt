package com.nikita_prasad.plantsy.utils.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.nikita_prasad.plantsy.ml.MainClassifier
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class analyzer(
    private val context: Context,
    private val a: (String) -> Unit
): ImageAnalysis.Analyzer {

    private var frameSkipCounter= 0

    override fun analyze(image: ImageProxy) {
        if (frameSkipCounter%60==0){
            val matrix = Matrix().apply{
                postRotate(image.imageInfo.rotationDegrees.toFloat())
            }
            val rotatedBitmap = Bitmap.createBitmap(
                image.toBitmap(),
                0,
                0,
                image.width,
                image.height,
                matrix,
                true
            )
            var tensorImage= TensorImage(DataType.FLOAT32)
            tensorImage.load(rotatedBitmap)
            val imageProcessor= ImageProcessor.Builder()
                .add(ResizeOp(256,256, ResizeOp.ResizeMethod.BILINEAR))
                .build()
            tensorImage= imageProcessor.process(tensorImage)
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(tensorImage.buffer)
            val outputFeature0 = MainClassifier.newInstance(context).process(inputFeature0).outputFeature0AsTensorBuffer
            Log.d("analyzerResults", getMaxIndex(outputFeature0.floatArray).toString())
            a(getMaxIndex(outputFeature0.floatArray).toString())
        }
        frameSkipCounter++
        image.close()
    }

    private  fun getMaxIndex(arr: FloatArray?): Int{
        return  arr?.withIndex()?.maxByOrNull { it.value }?.index ?: -1
    }

}

