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

const val DETAIL_ARGUMENT_KEY = "result"

sealed class NavItem(
    val title: String,
    val route: String,
    val unselectedIcon: ImageVector= Icons.Outlined.Home,
    val selectedIcon: ImageVector = Icons.Filled.Home,
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

    object Scan : NavItem(
        title = "Scan",
        route = "scan",
        unselectedIcon = Icons.Outlined.People,
        selectedIcon = Icons.Filled.People,
    )

    object Detail: NavItem(
        title = "Detail",
        route = "detail/{$DETAIL_ARGUMENT_KEY}"){
        fun passResult(result: Int): String{
            return "detail/$result"
        }
    }
}



