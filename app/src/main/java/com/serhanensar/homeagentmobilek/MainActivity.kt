package com.serhanensar.homeagentmobilek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.serhanensar.homeagentmobilek.ui.screens.DashboardScreen
import com.serhanensar.homeagentmobilek.ui.theme.HomeAgentMobileKTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeAgentMobileKTheme {
                com.serhanensar.homeagentmobilek.ui.navigation.AppNavigation()
            }
        }
    }
}
