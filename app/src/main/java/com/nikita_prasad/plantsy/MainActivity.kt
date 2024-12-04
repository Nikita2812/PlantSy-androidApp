package com.nikita_prasad.plantsy

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nikita_prasad.plantsy.navigation.bottombar.NavItem
import com.nikita_prasad.plantsy.navigation.bottombar.Navgraph
import com.nikita_prasad.plantsy.ui.theme.Pink40
import com.nikita_prasad.plantsy.ui.theme.PlantsyTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            PlantsyTheme {
                val items = listOf(
                    NavItem.Home,
                    NavItem.Chatbot,
                    NavItem.Community,
                    NavItem.Scan,
                )
                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val currentItemIndex = items.indexOfFirst { it.route == currentDestination?.route }
                val bottomDestination = items.any { it.route == currentDestination?.route }

                if (currentItemIndex != -1 && currentItemIndex != selectedItemIndex){
                    selectedItemIndex = currentItemIndex
                }


                Scaffold(
                    bottomBar = {
                        if (bottomDestination) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.secondary
                            ){
                                items.forEachIndexed { index, bottomNavItem ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            selectedItemIndex = index
                                            navController.navigate(bottomNavItem.route) {
                                                popUpTo(navController.graph.findStartDestination().id)
                                                launchSingleTop = true
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (selectedItemIndex==index){
                                                    bottomNavItem.selectedIcon
                                                } else bottomNavItem.unselectedIcon,
                                                contentDescription = bottomNavItem.title
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = bottomNavItem.title,
                                                fontSize = 12.sp
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = Color.Green,
                                            selectedIconColor = Color.Blue,
                                            unselectedIconColor = Color.Gray,
                                            selectedTextColor = Color.Blue
                                        ),
                                        alwaysShowLabel = false
                                    )
                                }
                            }
                        }
                    },
                    contentColor = Pink40
                ) { innerPadding ->

                    Navgraph(
                        navController = navController,
                        //modifier = Modifier.padding(innerPadding),
                        paddingValues = innerPadding
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PlantsyTheme {
        Greeting("Android")
    }
}