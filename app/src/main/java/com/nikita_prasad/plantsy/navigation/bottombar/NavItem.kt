package com.nikita_prasad.plantsy.navigation.bottombar

import android.health.connect.datatypes.ExerciseRoute
import android.icu.text.CaseMap.Title
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.Assistant
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(
    val title: String,
    val route: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
) {
    object Home : NavItem(
        title = "Home",
        route = "home",
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
    )

    object Chatbot : NavItem(
        title = "Chatbot",
        route = "chatbot",
        unselectedIcon = Icons.Outlined.Assistant,
        selectedIcon = Icons.Filled.Assistant,
    )

    object Community : NavItem(
        title = "Community",
        route = "community",
        unselectedIcon = Icons.Outlined.People,
        selectedIcon = Icons.Filled.People,
    )

}