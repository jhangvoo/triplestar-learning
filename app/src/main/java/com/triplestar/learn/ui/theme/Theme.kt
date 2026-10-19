package com.triplestar.learn.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = AstroBluePrimary,
    onPrimary = SurfaceLight,
    primaryContainer = AstroBlueLight,
    onPrimaryContainer = AstroBlueDark,
    secondary = StarGoldPrimary,
    onSecondary = TextPrimaryLight,
    secondaryContainer = StarGoldLight,
    onSecondaryContainer = StarGoldDark,
    tertiary = EmeraldSuccess,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight
)

private val DarkColorScheme = darkColorScheme(
    primary = AstroBlueLight,
    onPrimary = BackgroundDark,
    primaryContainer = AstroBlueDark,
    onPrimaryContainer = SurfaceLight,
    secondary = StarGoldPrimary,
    onSecondary = BackgroundDark,
    secondaryContainer = StarGoldDark,
    onSecondaryContainer = StarGoldLight,
    tertiary = EmeraldSuccess,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark
)

@Composable
fun TripleStarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep branded cheerful colors for kids
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
