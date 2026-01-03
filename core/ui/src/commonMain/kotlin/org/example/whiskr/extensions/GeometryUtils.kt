package org.example.whiskr.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import kotlin.math.hypot

fun Modifier.circularClip(radius: Float, center: Offset) = drawWithContent {
    val path = Path().apply {
        addOval(Rect(center = center, radius = radius))
    }
    clipPath(path) {
        this@drawWithContent.drawContent()
    }
}

fun Size.getDistanceToCorner(from: Offset): Float {
    val corners = listOf(
        Offset(0f, 0f),
        Offset(width, 0f),
        Offset(0f, height),
        Offset(width, height)
    )
    return corners.maxOf { corner -> hypot((from.x - corner.x), (from.y - corner.y)) }
}