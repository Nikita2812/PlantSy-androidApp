package com.nikita_prasad.plantsy.navigation.bottombar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nikita_prasad.plantsy.screen.ChatbotScreen
import com.nikita_prasad.plantsy.screen.CommunityScreen
import com.nikita_prasad.plantsy.screen.HomeScreen

@Composable
fun Navgraph(
    navController: NavHostController,
) {
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
    }
}