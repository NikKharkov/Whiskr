package org.example.whiskr.data

import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import whiskr.features.ai_studio.generated.resources.Res
import whiskr.features.ai_studio.generated.resources._3d_render
import whiskr.features.ai_studio.generated.resources.cyberpunk
import whiskr.features.ai_studio.generated.resources.oil_painting
import whiskr.features.ai_studio.generated.resources.style_3d
import whiskr.features.ai_studio.generated.resources.style_cyberpunk
import whiskr.features.ai_studio.generated.resources.style_oil
import whiskr.features.ai_studio.generated.resources.style_watercolor
import whiskr.features.ai_studio.generated.resources.watercolor

enum class AiArtStyle(val displayName: StringResource, val artImage: DrawableResource) {
    CYBERPUNK(Res.string.cyberpunk, Res.drawable.style_cyberpunk),
    OIL_PAINTING(Res.string.oil_painting, Res.drawable.style_oil),
    RENDER_3D(Res.string._3d_render, Res.drawable.style_3d),
    WATERCOLOR(Res.string.watercolor, Res.drawable.style_watercolor);
}

@Serializable
data class AiGenerationRequestDto(
    val prompt: String,
    val style: AiArtStyle?
)

@Serializable
data class AiGenerationResponseDto(
    val id: Long,
    val resultImageUrl: String,
    val updatedBalance: Long
)

@Serializable
data class AiGalleryItemDto(
    val id: Long,
    val imageUrl: String,
    val prompt: String,
    val style: AiArtStyle?
)