package com.shohan.khatiyan.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = SurfaceLight,
    primaryContainer = EmeraldContainer,
    onPrimaryContainer = OnEmeraldContainer,
    secondary = TealSecondary,
    secondaryContainer = TealContainer,
    tertiary = GoldAccent,
    tertiaryContainer = GoldContainer,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariant,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    error = StatusDanger
)

@Composable
fun KhatiyanTheme(
    content: @Composable () -> Unit
) {
    // ALWAYS ENFORCE LIGHT COLOR SCHEME REGARDLESS OF SYSTEM DARK MODE SETTING
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
