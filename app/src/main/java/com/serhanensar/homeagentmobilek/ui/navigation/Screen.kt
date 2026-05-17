// Copyright (c) 2026 Serhan Ensar. All rights reserved.
package com.serhanensar.homeagentmobilek.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Filled.Home)
    object Assistant : Screen("assistant", "Asistan", Icons.Filled.Person)
    object Settings : Screen("settings", "Ayarlar", Icons.Filled.Settings)
}
