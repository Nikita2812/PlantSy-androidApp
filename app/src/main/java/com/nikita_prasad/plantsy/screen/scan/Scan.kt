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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.nikita_prasad.plantsy.screen.CameraPreview

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    paddingValues: PaddingValues
) {
    var cameraPermissionState: PermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    if (cameraPermissionState.status.isGranted){
        Log.d("permission", "permissionGranted")
    } else {
        Log.d("permission", "permissionNOtGranted")
        ActivityCompat.requestPermissions(
            LocalContext.current as Activity,
            arrayOf(Manifest.permission.CAMERA),
            0
        )
    }
    val scope = rememberCoroutineScope()
    val applContext= LocalContext.current.applicationContext
    val controller = remember{
        LifecycleCameraController(applContext).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                        CameraController.VIDEO_CAPTURE
            )}
    }
    val viewModel = viewModel<MainViewModel>()
    val bitmaps by viewModel.bitmaps.collectAsState()
    val applicationcontext = LocalContext.current.applicationContext
    val modalState= remember {
        mutableStateOf(false)
    }



    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        if (modalState.value){
            ModalBottomSheet(onDismissRequest = { modalState.value= false }) {
                PhotoBottomSheetContent(
                    bitmaps = bitmaps,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }

        CameraPreview(
            controller = controller,
            modifier = Modifier
                .fillMaxSize(.5f)
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
                    modalState.value=true
                }
            ){
                Icon(
                    imageVector = Icons.Default.Collections,
                    contentDescription = "open gallery"

                )
            }
            IconButton(
                onClick = {
                    takePhoto(
                        controller = controller,
                        onPhotoTaken = viewModel::onTakePhoto,
                        applicationcontext = applicationcontext
                    )
                }
            ){
                Icon(
                    imageVector = Icons.Default.Photo,
                    contentDescription = "Take photo"

                )
            }

        }


    }

}

private fun takePhoto(
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
    applicationcontext: Context
){
    controller.takePicture(
        ContextCompat.getMainExecutor(applicationcontext),
        object : ImageCapture.OnImageCapturedCallback(){
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