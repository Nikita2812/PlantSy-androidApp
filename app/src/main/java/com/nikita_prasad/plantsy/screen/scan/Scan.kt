package com.nikita_prasad.plantsy.screen.scan

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.nikita_prasad.plantsy.database.appDB.diseaseInfo.diseaseDBvm
import com.nikita_prasad.plantsy.navigation.bottombar.NavItem
import com.nikita_prasad.plantsy.screen.CameraPreview
import com.nikita_prasad.plantsy.utils.dataclass.PlantNameIndexDC
import com.nikita_prasad.plantsy.utils.viewmodel.ScanVM
import com.nikita_prasad.plantsy.utils.viewmodel.analyzer

@SuppressLint("RememberReturnType", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    paddingValues: PaddingValues,
    navController: NavHostController,
    scanVM: ScanVM,
    diseaseDBvm: diseaseDBvm,
) {
    var cameraPermissionState: PermissionState =
        rememberPermissionState(permission = Manifest.permission.CAMERA)

    if (cameraPermissionState.status.isGranted) {
        Log.d("permission", "permissionGranted")
    } else {
        Log.d("permission", "permissionNOtGranted")
        ActivityCompat.requestPermissions(
            LocalContext.current as Activity,
            arrayOf(Manifest.permission.CAMERA),
            0
        )
    }

    val context = LocalContext.current
    val applContext = context.applicationContext
    var plantIndex by remember {
        mutableStateOf(Int.MAX_VALUE)
    }

    var detectedPlant by remember { mutableStateOf("") }

    val analyzer = remember {
        analyzer(
            context = context,
            a = {
                plantIndex = it.toInt()
            }
        )
    }

    val controller = remember {
        LifecycleCameraController(applContext).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                        CameraController.IMAGE_ANALYSIS
            )

            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(applContext),
                analyzer
            )
        }
    }
    LaunchedEffect(plantIndex) {
        if (plantIndex != Int.MAX_VALUE) {
            detectedPlant = diseaseDBvm.getDomain(plantIndex.toLong())
        }
    }

    val bitmaps by scanVM.bitmaps.collectAsState()
    val applicationcontext = LocalContext.current.applicationContext
    val modalState = remember {
        mutableStateOf(false)
    }
    val flashLightMode = remember {
        mutableStateOf(FlashlightMode.Off)
    }
    when (flashLightMode.value) {
        FlashlightMode.Off -> controller.cameraControl?.enableTorch(false)
        FlashlightMode.On -> controller.cameraControl?.enableTorch(true)
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Scan",
                        fontWeight = FontWeight(600),
                        fontSize = 26.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                ),
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.SettingsSuggest,
                            contentDescription = "settings"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "back"
                        )
                    }
                }
            )

        },
        modifier = Modifier
            .fillMaxSize()
    )

    {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (modalState.value) {
                ModalBottomSheet(onDismissRequest = {
                    modalState.value = false
                }) {
                    Column {
                        Text(text = detectedPlant)
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.clickable {
                                    if (plantIndex.isMaxValue()) {
                                        navController.navigate(
                                            route = NavItem.Detail.passResult(
                                                plantIndex
                                            )
                                        )
                                    }
                                },
                                text = "go to details"
                            )
                            PlantSwitchDropdown(
                                onPlantIndexUpdate = { newIndex ->
                                    plantIndex = newIndex
                                }
                            )
                        }
                    }
                }
            }

            CameraPreview(
                controller = controller,
                modifier = Modifier
                    .fillMaxHeight(.8f)
                    .fillMaxWidth()
            )
            IconButton(
                onClick = {
                    controller.cameraSelector =
                        if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else {
                            CameraSelector.DEFAULT_BACK_CAMERA
                        }
                },
                modifier = Modifier.offset(16.dp, 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "switch camera"
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    //.align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(
                    onClick = {
                        modalState.value = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Collections,
                        contentDescription = "open gallery"

                    )
                }
                IconButton(
                    onClick = {
                        takePhoto(
                            controller = controller,
                            onPhotoTaken = {
                                scanVM.onTakePhoto(it)
                                modalState.value = true
                            },
                            applicationcontext = applicationcontext
                        )
                    }
                )
                {
                    Icon(
                        imageVector = Icons.Default.Photo,
                        contentDescription = "Take photo"

                    )
                }
                IconButton(
                    onClick = {
                        if (flashLightMode.value == FlashlightMode.Off) {
                            flashLightMode.value = FlashlightMode.On
                        } else {
                            flashLightMode.value = FlashlightMode.Off
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (flashLightMode.value == FlashlightMode.Off) Icons.Outlined.FlashOff else Icons.Outlined.FlashOn,
                        contentDescription = "flashlight"
                    )
                }
                Text(text = detectedPlant)

            }
        }
    }
}


enum class FlashlightMode {
    Off,
    On
}


private fun takePhoto(
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
    applicationcontext: Context
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(applicationcontext),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                onPhotoTaken(image.toBitmap())
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("camera", "Couldn't take photo: ", exception)

            }
        }
    )
}

@Composable
fun PlantSwitchDropdown(
    onPlantIndexUpdate: (Int) -> Unit
) {
    val plantDataList: List<PlantNameIndexDC> = listOf(
        PlantNameIndexDC(1, "Apple"),
        PlantNameIndexDC(2, "Bell pepper"),
        PlantNameIndexDC(3, "Cherry"),
        PlantNameIndexDC(4, "Citrus"),
        PlantNameIndexDC(5, "Corn"),
        PlantNameIndexDC(6, "Grape"),
        PlantNameIndexDC(7, "Peach"),
        PlantNameIndexDC(8, "Potato"),
        PlantNameIndexDC(9, "Strawberry"),
        PlantNameIndexDC(10, "Tomato")
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Select an option") }

    Box {
        Text(
            text = selectedOption,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            plantDataList.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.plantName) },
                    onClick = {
                        selectedOption = option.plantName
                        onPlantIndexUpdate(option.plantIndex)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun Int.isMaxValue(): Boolean {
    if (Int.MAX_VALUE == this) return false
    return true
}

