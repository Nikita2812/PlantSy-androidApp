package com.nikita_prasad.plantsy.screen.scan

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nikita_prasad.plantsy.utils.viewmodel.ScanVM

@Composable
fun DetailScreen(
    plantIndex: Int,
    savePhotoviewModel: ScanVM,
) {
    val context= LocalContext.current
    val bitmap = savePhotoviewModel.bitmaps.collectAsState()
    LaunchedEffect(true) {
        savePhotoviewModel.onClassify(context, plantIndex = plantIndex)
    }
    val nrmlBool = savePhotoviewModel.isNormalBoolean.collectAsState().value
    val errBool = savePhotoviewModel.isErroredBoolean.collectAsState().value


    if (nrmlBool) {
        Text("Plant is healthyth")
    } else if (errBool) {
        Text("Error")
    } else {

        val data = savePhotoviewModel.data.collectAsState().value

        Log.d("res", data.toString())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Result: $plantIndex",
                fontSize = 24.sp
            )
            Image(
                bitmap = bitmap.value!!.asImageBitmap(),
                ""
            )
            Text(text = "Disease: ${data.disease_name}")

        }
    }

}