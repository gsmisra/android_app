package com.dev.gauravmisra.happyfingers


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dev.gauravmisra.happyfingers.ui.theme.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NeuroPlayTheme {
                AppNav()
            }
        }
    }
}
