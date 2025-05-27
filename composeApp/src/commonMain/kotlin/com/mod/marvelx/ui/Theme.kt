package com.mod.marvelx.ui

import androidx.compose.material3.darkColorScheme

val MarvelColorScheme = darkColorScheme(
    primary = MarvelColors.MarvelRed,
    onPrimary = MarvelColors.TextPrimary,
    primaryContainer = MarvelColors.MarvelRedDark,
    onPrimaryContainer = MarvelColors.TextPrimary,

    secondary = MarvelColors.MarvelGold,
    onSecondary = MarvelColors.MarvelBlack,
    secondaryContainer = MarvelColors.MarvelGold.copy(alpha = 0.3f),
    onSecondaryContainer = MarvelColors.TextPrimary,

    tertiary = MarvelColors.MarvelBlue,
    onTertiary = MarvelColors.TextPrimary,

    background = MarvelColors.MarvelBlack,
    onBackground = MarvelColors.TextPrimary,

    surface = MarvelColors.SurfacePrimary,
    onSurface = MarvelColors.TextPrimary,
    surfaceVariant = MarvelColors.SurfaceSecondary,
    onSurfaceVariant = MarvelColors.TextSecondary,

    surfaceContainer = MarvelColors.SurfaceCard,
    surfaceContainerHigh = MarvelColors.MarvelGray,
    surfaceContainerHighest = MarvelColors.MarvelLightGray,

    outline = MarvelColors.MarvelGray,
    outlineVariant = MarvelColors.MarvelLightGray,

    error = MarvelColors.Error,
    onError = MarvelColors.TextPrimary,
    errorContainer = MarvelColors.Error.copy(alpha = 0.3f),
    onErrorContainer = MarvelColors.TextPrimary,
)