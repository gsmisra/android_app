package com.dev.gauravmisra.happyfingers


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dev.gauravmisra.happyfingers.screens.HomeScreen
import com.dev.gauravmisra.happyfingers.screens.*

@Composable
fun AppNav() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Routes.HOME) {
        composable(Routes.HOME) { HomeScreen(nav) }
        composable(Routes.CALM) { CalmScreen(nav) }
        composable(Routes.PLAY) { PlayScreen(nav) }
        composable(Routes.TALK) { TalkScreen(nav) }
        composable(Routes.MYDAY) { MyDayScreen(nav) }
        composable(Routes.SETTINGS) { SettingsScreen(nav) }
    }
}
