package org.example.whiskr.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalWhiskrColors = staticCompositionLocalOf<WhiskrColors> {
    error("No colors provided")
}

val LocalWhiskrTypography = staticCompositionLocalOf { typography }

@Composable
fun WhiskrTheme(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (isDarkTheme) darkPalette else lightPalette

    CompositionLocalProvider(
        LocalWhiskrColors provides colors,
        LocalWhiskrTypography provides typography,
        content = content
    )
}

object WhiskrTheme {
    val colors: WhiskrColors
        @Composable
        @ReadOnlyComposable
        get() = LocalWhiskrColors.current

    val typography: WhiskrTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalWhiskrTypography.current
}