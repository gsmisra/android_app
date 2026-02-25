package com.dev.gauravmisra.happyfingers.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.dev.gauravmisra.happyfingers.Routes

@Composable
fun FloatingHomeButton(
    nav: NavController,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = {
            nav.navigate(Routes.HOME) {
                popUpTo(0)          // full reset for predictability
                launchSingleTop = true
            }
        },
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(
            imageVector = Icons.Filled.Home,
            contentDescription = "Main Menu"
        )
    }
}
