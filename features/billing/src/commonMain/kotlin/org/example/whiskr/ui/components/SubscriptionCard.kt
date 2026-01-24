package org.example.whiskr.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.data.ProductResponseDto
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.billing.generated.resources.Res
import whiskr.features.billing.generated.resources.ic_coin
import whiskr.features.billing.generated.resources.mascot_subscription
import whiskr.features.billing.generated.resources.store_balance_label
import whiskr.features.billing.generated.resources.store_coins_format
import whiskr.features.billing.generated.resources.store_price_format
import whiskr.features.billing.generated.resources.store_vip_desc
import whiskr.features.billing.generated.resources.store_vip_price
import whiskr.features.billing.generated.resources.store_vip_title

@Composable
fun SubscriptionCard(
    onSubscribeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = WhiskrTheme.colors.vipGradient,
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.mascot_subscription),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .weight(0.3f),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(0.7f)) {
                Text(
                    text = stringResource(Res.string.store_vip_title),
                    style = WhiskrTheme.typography.h3.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.25f),
                            blurRadius = 4f
                        )
                    )
                )
                Text(
                    text = stringResource(Res.string.store_vip_desc),
                    style = WhiskrTheme.typography.body,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                WhiskrButton(
                    text = stringResource(Res.string.store_vip_price),
                    onClick = onSubscribeClick,
                    containerColor = Color.White,
                    contentColor = WhiskrTheme.colors.primary,
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun BalanceSection(
    balance: Long,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(WhiskrTheme.colors.surface)
            .border(1.dp, WhiskrTheme.colors.outline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(Res.string.store_balance_label),
            style = WhiskrTheme.typography.h3.copy(fontWeight = FontWeight.Normal),
            color = WhiskrTheme.colors.onBackground
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Image(
                painter = painterResource(Res.drawable.ic_coin),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Text(
                text = balance.toString(),
                style = WhiskrTheme.typography.h3,
                color = WhiskrTheme.colors.onBackground
            )
        }
    }
}

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
                text = stringResource(Res.string.store_price_format, product.priceInCents.toDollarPrice()),
                onClick = onClick,
                contentColor = Color.White,
                containerColor = WhiskrTheme.colors.primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
