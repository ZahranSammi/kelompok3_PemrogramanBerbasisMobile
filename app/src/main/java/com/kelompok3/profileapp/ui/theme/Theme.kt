package com.kelompok3.profileapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Locked to LightColorScheme per DESIGN.md section 2 to maintain high-contrast projector legibility in class (R-21).
private val PlayfulDaylightColorScheme = lightColorScheme(
    primary = ElectricSkyBlue,
    onPrimary = PureWhiteSurface,
    primaryContainer = SkyPastelBadge,
    onPrimaryContainer = ElectricSkyBlueDark,
    secondary = SunnyMangoInk,
    onSecondary = PureWhiteSurface,
    secondaryContainer = ButterYellowSurface,
    onSecondaryContainer = SunnyMangoInk,
    tertiary = BubblegumCoralAccent,
    onTertiary = PureWhiteSurface,
    tertiaryContainer = BubblegumCoralTint,
    onTertiaryContainer = BubblegumCoralInk,
    background = WarmCreamBg,
    onBackground = DeepInkNavy,
    surface = PureWhiteSurface,
    onSurface = DeepInkNavy,
    surfaceVariant = SoftCloudSurface,
    onSurfaceVariant = MutedSlate,
    outline = ChartLineSlate,
    error = Color(0xFFB91C1C),
    onError = PureWhiteSurface
)

@Composable
fun Kelompok3Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PlayfulDaylightColorScheme,
        typography = Kelompok3Typography,
        content = content
    )
}
