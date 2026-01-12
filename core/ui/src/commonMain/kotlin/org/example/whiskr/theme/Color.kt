package org.example.whiskr.theme

import androidx.compose.ui.graphics.Color

data class WhiskrColors(
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val secondary: Color,
    val outline: Color,
    val primary: Color,
    val error: Color,
    val like: Color
)

val lightPalette = WhiskrColors(
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF111827),
    surface = Color(0xFFF3F4F6),
    secondary = Color(0xFF6B7280),
    outline = Color(0xFFD1D5DB),
    primary = Color(0xFF1D9BF0),
    error = Color(0xFFEF4444),
    like = Color(0xFFE91E63)
)

val darkPalette = WhiskrColors(
    background = Color(0xFF111827),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF1F2937),
    secondary = Color(0xFF9CA3AF),
    outline = Color(0xFF374151),
    primary = Color(0xFF1D9BF0),
    error = Color(0xFFF87171),
    like = Color(0xFFF91880)
)