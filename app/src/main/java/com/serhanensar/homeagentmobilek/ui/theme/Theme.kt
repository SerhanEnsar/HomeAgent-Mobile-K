package com.serhanensar.homeagentmobilek.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AccentCyan,
    secondary = AccentBlue,
    background = BackgroundDark,
    surface = CardBackground,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    outline = BorderColor
)

@Composable
fun HomeAgentMobileKTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
