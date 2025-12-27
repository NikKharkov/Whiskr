package org.example.whiskr.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.auth.generated.resources.Res
import whiskr.features.auth.generated.resources.whisp

@Composable
fun WelcomeHeroContent(
    modifier: Modifier = Modifier,
    isTablet: Boolean,
) {
    Image(
        painter = painterResource(Res.drawable.whisp),
        contentDescription = stringResource(Res.string.whisp),
        contentScale = ContentScale.Fit,
        modifier = modifier.size(if (isTablet) 400.dp else 200.dp),
    )
}
