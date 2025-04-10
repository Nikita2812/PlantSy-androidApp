package com.nikita_prasad.plantsy.screen.scan

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

    val lightGreen = Color(0xFFE8F5E9)
    val mediumGreen = Color(0xFF81C784)
    val darkGreen = Color(0xFF2E7D32)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = lightGreen
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    when {
                        nrmlBool -> {
                            Text(
                                text = "Good News!",
                                color = darkGreen,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Your plant is healthy",
                                color = darkGreen,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        errBool -> {
                            Text(
                                text = "Error",
                                color = Color.Red,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "There was a problem analyzing your plant.",
                                color = Color.DarkGray,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        else -> {
                            val data = savePhotoviewModel.data.collectAsState().value
                            Log.d("res", data.toString())

                            Text(
                                text = "Diagnosis Result",
                                color = darkGreen,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            bitmap.value?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Plant image",
                                    modifier = Modifier
                                        .size(250.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(2.dp, mediumGreen, RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(mediumGreen, RoundedCornerShape(8.dp))
                                    .padding(16.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Disease Detected:",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Text(
                                        text = "${data.disease_name}",
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }

                            if (data.disease_name?.contains("early blight", ignoreCase = true) == true) {
                                Spacer(modifier = Modifier.height(16.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFF1F8E9), RoundedCornerShape(8.dp))
                                        .padding(16.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "Treatment Tips:",
                                            color = darkGreen,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("• ", color = darkGreen, fontWeight = FontWeight.Bold)
                                            Text("Remove infected leaves", color = Color.DarkGray)
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("• ", color = darkGreen, fontWeight = FontWeight.Bold)
                                            Text("Apply fungicide", color = Color.DarkGray)
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("• ", color = darkGreen, fontWeight = FontWeight.Bold)
                                            Text("Water at plant base only", color = Color.DarkGray)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}