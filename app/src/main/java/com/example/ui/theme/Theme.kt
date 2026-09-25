package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CyanLight,
    onPrimary = SlateDark950,
    primaryContainer = CyanDark,
    onPrimaryContainer = Color.White,
    secondary = VioletLight,
    onSecondary = SlateDark950,
    secondaryContainer = SlateDark800,
    onSecondaryContainer = VioletLight,
    tertiary = AmazonOrange,
    background = SlateDark950,
    onBackground = SlateTextPrimaryDark,
    surface = SlateDark900,
    onSurface = SlateTextPrimaryDark,
    surfaceVariant = SlateDark800,
    onSurfaceVariant = SlateTextSecondaryDark,
    outline = SlateDark600,
    outlineVariant = SlateDark700,
    error = RoseError,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = CyanDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCFFAFE),
    onPrimaryContainer = Color(0xFF164E63),
    secondary = VioletDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEDE9FE),
    onSecondaryContainer = Color(0xFF312E81),
    tertiary = AmazonOrange,
    background = SlateLight50,
    onBackground = SlateTextPrimaryLight,
    surface = Color.White,
    onSurface = SlateTextPrimaryLight,
    surfaceVariant = SlateLight100,
    onSurfaceVariant = SlateTextSecondaryLight,
    outline = SlateLight300,
    outlineVariant = SlateLight200,
    error = RoseError,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent branding colors
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
