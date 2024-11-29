package com.nikita_prasad.plantsy.navigation.bottombar

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nikita_prasad.plantsy.database.appDB.diseaseInfo.diseaseDBvm
import com.nikita_prasad.plantsy.screen.ChatbotScreen
import com.nikita_prasad.plantsy.screen.CommunityScreen
import com.nikita_prasad.plantsy.screen.HomeScreen
import com.nikita_prasad.plantsy.screen.scan.DetailScreen
import com.nikita_prasad.plantsy.screen.scan.ScanScreen
import com.nikita_prasad.plantsy.utils.viewmodel.ScanVM


@Composable
fun Navgraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    // declaring all viewmodels
    val savePhotoViewModel= viewModel<ScanVM>()
    val diseaseDBVM= viewModel<diseaseDBvm>()

    savePhotoViewModel.initialize(diseaseDBVM)
    val context = LocalContext.current
    val versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName

    LaunchedEffect(true) {
        val sharedPreferences =
            context.getSharedPreferences("packageVersionName", Context.MODE_PRIVATE)
        val versionNameSF = sharedPreferences.getString("packageVersionName", "null")

        Log.d("pckge", "versionName: $versionName, versionNameSF: $versionNameSF")

        if (versionName != versionNameSF) {
            Log.d("dbStatus", "triggered")
            diseaseDBVM.fetchDiseaseData(
                onCompletion = {
                    sharedPreferences.edit().putString("packageVersionName", versionName).apply()
                }
            )
            Log.d("dbStatus", "after fetch")
        } else Log.d("dbStatus", "not triggered")

    }

    NavHost(
        navController = navController,
        startDestination = NavItem.Home.route,

    ) {
        composable(route = NavItem.Home.route) {
            HomeScreen()
        }

        composable(route = NavItem.Chatbot.route) {
            ChatbotScreen()
        }

        composable(route = NavItem.Community.route) {
            CommunityScreen()
        }

        composable(route = NavItem.Scan.route) {
            ScanScreen(paddingValues = paddingValues,
                navController = navController,
                scanVM = savePhotoViewModel,
                diseaseDBvm = diseaseDBVM
            )
        }


        composable(route = NavItem.Detail.route,
            arguments = listOf(navArgument(DETAIL_ARGUMENT_KEY) {
                type = NavType.IntType
            }),
        )  {
            Log.d("Args",it.arguments?.getInt(DETAIL_ARGUMENT_KEY).toString())
            DetailScreen(
                plantIndex = it.arguments?.getInt(DETAIL_ARGUMENT_KEY) ?: 0,
                savePhotoviewModel = savePhotoViewModel
            )
        }

    }

}