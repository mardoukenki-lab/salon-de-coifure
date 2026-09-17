package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme =
  lightColorScheme(
    primary = SalonTerracotta,
    onPrimary = SalonPaper,
    primaryContainer = SalonTerracottaSoft,
    onPrimaryContainer = SalonNavy,
    secondary = SalonNavy,
    onSecondary = SalonPaper,
    secondaryContainer = SalonRose.copy(alpha = 0.35f),
    onSecondaryContainer = SalonNavy,
    tertiary = SalonNavySoft,
    onTertiary = SalonPaper,
    background = SalonCream,
    onBackground = SalonInk,
    surface = SalonPaper,
    onSurface = SalonInk,
    surfaceVariant = SalonCream,
    onSurfaceVariant = SalonInkSoft,
    outline = SalonLine,
    error = SalonDangerFg,
    errorContainer = SalonDangerBg
  )

private val DarkColorScheme =
  darkColorScheme(
    primary = DarkSalonTerracotta,
    onPrimary = DarkSalonBackground,
    primaryContainer = SalonTerracotta.copy(alpha = 0.4f),
    onPrimaryContainer = DarkSalonNavy,
    secondary = DarkSalonNavy,
    onSecondary = DarkSalonBackground,
    secondaryContainer = SalonNavySoft,
    onSecondaryContainer = DarkSalonNavy,
    background = DarkSalonBackground,
    onBackground = DarkSalonNavy,
    surface = DarkSalonPaper,
    onSurface = DarkSalonNavy,
    surfaceVariant = DarkSalonPaper,
    onSurfaceVariant = DarkSalonNavy.copy(alpha = 0.7f),
    outline = DarkSalonLine,
    error = SalonDangerFg,
    errorContainer = SalonDangerBg
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
