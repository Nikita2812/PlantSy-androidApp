package com.nikita_prasad.plantsy.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class SensorData(
    val temperature: Double,
    val humidity: Double,
    val moisture: Double,
    val heatIndex: Double,
    val lightlux: Double
)

// 2. Gson serializable data class with annotations
data class SerializableSensorData(
    val temperature: Double,
    val humidity: Double,
    val moisture: Double,
    @SerializedName("heat_index") val heatIndex: Double,
    val lightlux: Double
) {
    // Converter function to domain model
    fun toSensorData(): SensorData = SensorData(
        temperature = temperature,
        humidity = humidity,
        moisture = moisture,
        heatIndex = heatIndex,
        lightlux = lightlux
    )
}

private val sensorJsonData = """{"temperature": 21, "humidity": 54, "moisture": 43, "heat_index": 32, "lightlux": 540}"""

@Composable
fun CommunityScreen(
    modifier: Modifier = Modifier
) {
    // Use Gson for JSON parsing
    val gson = Gson()

    // Parse JSON data using Gson and convert to domain model
    val sensorData = remember {
        gson.fromJson(sensorJsonData, SerializableSensorData::class.java).toSensorData()
    }

    // Define parameters for each meter
    val meterConfigs = listOf(
        MeterConfig(
            value = sensorData.temperature,
            maxValue = 50, // Assuming max temperature is 50°C
            title = "Temperature",
            suffix = "°C",
            color = Color(0xFFFF6B6B) // Red shade for temperature
        ),
        MeterConfig(
            value = sensorData.humidity,
            maxValue = 100, // Humidity is percentage
            title = "Humidity",
            suffix = "%",
            color = Color(0xFF4ECDC4) // Teal shade for humidity
        ),
        MeterConfig(
            value = sensorData.moisture,
            maxValue = 100, // Moisture as percentage
            title = "Moisture",
            suffix = "%",
            color = Color(0xFF1A535C) // Darker teal for moisture
        ),
        MeterConfig(
            value = sensorData.heatIndex,
            maxValue = 60, // Assuming max heat index is 60°C
            title = "Heat Index",
            suffix = "°C",
            color = Color(0xFFFF9F1C) // Orange for heat index
        ),
        MeterConfig(
            value = sensorData.lightlux,
            maxValue = 1000, // Assuming max light is 1000 lux
            title = "Light",
            suffix = "lx",
            color = Color(0xFFFFD166) // Yellow for light
        )
    )

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Plant Sensor Dashboard",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        for (i in meterConfigs.indices step 2) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // First meter in the row
                CircularMeter(
                    modifier = Modifier.weight(1f),
                    indicatorValue = meterConfigs[i].value,
                    maxIndicatorValue = meterConfigs[i].maxValue,
                    canvasSize = 160.dp,
                    backgroundIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                    backgroundIndicatorStrokeWidth = 40f,
                    foregroundIndicatorColor = meterConfigs[i].color,
                    foregroundIndicatorStrokeWidth = 40f,
                    bigTextFontSize = MaterialTheme.typography.h6.fontSize,
                    bigTextColor = MaterialTheme.colors.onSurface,
                    bigTextSuffix = meterConfigs[i].suffix,
                    smallText = meterConfigs[i].title,
                    smallTextFontSize = MaterialTheme.typography.body1.fontSize,
                    smallTextColor = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )

                // Second meter in the row (if available)
                if (i + 1 < meterConfigs.size) {
                    CircularMeter(
                        modifier = Modifier.weight(1f),
                        indicatorValue = meterConfigs[i + 1].value,
                        maxIndicatorValue = meterConfigs[i + 1].maxValue,
                        canvasSize = 160.dp,
                        backgroundIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                        backgroundIndicatorStrokeWidth = 40f,
                        foregroundIndicatorColor = meterConfigs[i + 1].color,
                        foregroundIndicatorStrokeWidth = 40f,
                        bigTextFontSize = MaterialTheme.typography.h6.fontSize,
                        bigTextColor = MaterialTheme.colors.onSurface,
                        bigTextSuffix = meterConfigs[i + 1].suffix,
                        smallText = meterConfigs[i + 1].title,
                        smallTextFontSize = MaterialTheme.typography.body1.fontSize,
                        smallTextColor = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                } else {
                    // Empty space to maintain layout if there's an odd number of meters
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

data class MeterConfig(
    val value: Double,
    val maxValue: Int,
    val title: String,
    val suffix: String,
    val color: Color
)

@Composable
fun CircularMeter(
    modifier: Modifier = Modifier,
    canvasSize: Dp = 300.dp,
    indicatorValue: Double = 0.0,
    maxIndicatorValue: Int = 100,
    backgroundIndicatorColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
    backgroundIndicatorStrokeWidth: Float = 100f,
    foregroundIndicatorColor: Color = MaterialTheme.colors.primary,
    foregroundIndicatorStrokeWidth: Float = 100f,
    bigTextFontSize: TextUnit = MaterialTheme.typography.h3.fontSize,
    bigTextColor: Color = MaterialTheme.colors.onSurface,
    bigTextSuffix: String = "",
    smallText: String = "",
    smallTextFontSize: TextUnit = MaterialTheme.typography.h6.fontSize,
    smallTextColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
) {
    var allowedIndicatorValue by remember {
        mutableStateOf(maxIndicatorValue.toDouble())
    }
    allowedIndicatorValue = if (indicatorValue <= maxIndicatorValue) {
        indicatorValue
    } else {
        maxIndicatorValue.toDouble()
    }

    var animatedIndicatorValue by remember { mutableStateOf(0f) }
    LaunchedEffect(key1 = allowedIndicatorValue) {
        animatedIndicatorValue = allowedIndicatorValue.toFloat()
    }

    val percentage =
        (animatedIndicatorValue / maxIndicatorValue) * 100

    val sweepAngle by animateFloatAsState(
        targetValue = (2.4 * percentage).toFloat(),
        animationSpec = tween(1000)
    )

    val receivedValue by animateFloatAsState(
        targetValue = allowedIndicatorValue.toFloat(),
        animationSpec = tween(1000)
    )

    val animatedBigTextColor by animateColorAsState(
        targetValue = if (allowedIndicatorValue == 0.0)
            MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
        else
            bigTextColor,
        animationSpec = tween(1000)
    )

    Column(
        modifier = modifier
            .size(canvasSize)
            .drawBehind {
                val componentSize = size / 1.25f
                backgroundIndicator(
                    componentSize = componentSize,
                    indicatorColor = backgroundIndicatorColor,
                    indicatorStrokeWidth = backgroundIndicatorStrokeWidth,
                )
                foregroundIndicator(
                    sweepAngle = sweepAngle,
                    componentSize = componentSize,
                    indicatorColor = foregroundIndicatorColor,
                    indicatorStrokeWidth = foregroundIndicatorStrokeWidth,
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmbeddedElements(
            bigText = receivedValue.toDouble(),
            bigTextFontSize = bigTextFontSize,
            bigTextColor = animatedBigTextColor,
            bigTextSuffix = bigTextSuffix,
            smallText = smallText,
            smallTextColor = smallTextColor,
            smallTextFontSize = smallTextFontSize
        )
    }
}

fun DrawScope.backgroundIndicator(
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = 240f,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
}

fun DrawScope.foregroundIndicator(
    sweepAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
}

@Composable
fun EmbeddedElements(
    bigText: Double,
    bigTextFontSize: TextUnit,
    bigTextColor: Color,
    bigTextSuffix: String,
    smallText: String,
    smallTextColor: Color,
    smallTextFontSize: TextUnit
) {
    Text(
        text = smallText,
        color = smallTextColor,
        fontSize = smallTextFontSize,
        textAlign = TextAlign.Center
    )
    Text(
        text = "${bigText.toInt()}$bigTextSuffix",
        color = bigTextColor,
        fontSize = bigTextFontSize,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
}

@Composable
@Preview(showBackground = true)
fun SensorDashboardPreview() {
    CommunityScreen()
}