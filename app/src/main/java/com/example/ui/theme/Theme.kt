package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ChristianGreenLight,
    onPrimary = ChristianDarkBg,
    primaryContainer = ChristianDarkGreenContainer,
    onPrimaryContainer = Color.White,
    secondary = ChristianGoldLight,
    onSecondary = ChristianDarkBg,
    secondaryContainer = ChristianDarkGold,
    onSecondaryContainer = ChristianGoldLight,
    background = ChristianDarkBg,
    onBackground = ChristianDarkText,
    surface = ChristianDarkSurface,
    onSurface = ChristianDarkText,
    surfaceVariant = ChristianDarkSurfaceVariant,
    onSurfaceVariant = ChristianDarkTextMuted,
    outline = ChristianDarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = ChristianDarkGreen,
    onPrimary = Color.White,
    primaryContainer = ChristianDarkGreenContainer,
    onPrimaryContainer = Color.White,
    secondary = ChristianGold,
    onSecondary = Color.White,
    secondaryContainer = ChristianGoldContainer,
    onSecondaryContainer = ChristianDarkGold,
    background = ChristianCreamBg,
    onBackground = ChristianTextDark,
    surface = ChristianPaperSurface,
    onSurface = ChristianTextDark,
    surfaceVariant = ChristianSurfaceVariant,
    onSurfaceVariant = ChristianTextMuted,
    outline = ChristianOutline
)

@Composable
fun NepaliBibleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
