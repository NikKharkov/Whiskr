package org.example.whiskr.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.data.ProductResponseDto
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.billing.generated.resources.Res
import whiskr.features.billing.generated.resources.store_coins_format
import whiskr.features.billing.generated.resources.store_price_format

@Composable
fun ProductCard(
    product: ProductResponseDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = WhiskrTheme.colors.surface),
        border = BorderStroke(1.dp, WhiskrTheme.colors.outline.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(product.key.toImageRes()),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = stringResource(product.key.toTitleRes()),
                style = WhiskrTheme.typography.h3,
                textAlign = TextAlign.Center,
                color = WhiskrTheme.colors.onBackground
            )

            Text(
                text = stringResource(Res.string.store_coins_format, product.rewardAmount),
                style = WhiskrTheme.typography.body,
                color = WhiskrTheme.colors.secondary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            WhiskrButton(
                text = stringResource(
                    Res.string.store_price_format,
                    product.priceInCents.toDollarPrice()
                ),
                onClick = onClick,
                contentColor = Color.White,
                containerColor = WhiskrTheme.colors.primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}