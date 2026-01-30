package org.example.whiskr.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.whiskr.component.StoreComponent
import org.example.whiskr.data.BillingProductKey
import org.example.whiskr.data.ProductResponseDto
import org.example.whiskr.data.WalletResponseDto
import org.example.whiskr.theme.LocalIsTablet
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.stringResource
import whiskr.features.billing.generated.resources.Res
import whiskr.features.billing.generated.resources.store_pack_section
import whiskr.features.billing.generated.resources.store_title

@Composable
fun StoreContent(
    model: StoreComponent.Model,
    contentPadding: PaddingValues,
    vipButtonText: String,
    subscriptionProduct: ProductResponseDto?,
    coinPacks: List<ProductResponseDto>,
    onProductClick: (BillingProductKey) -> Unit
) {
    val isTablet = LocalIsTablet.current

    if (model.isLoading && model.products.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = WhiskrTheme.colors.primary)
        }
    }

    if (model.error != null && model.products.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = model.error, color = WhiskrTheme.colors.error)
        }
    }

    if (isTablet) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding())
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            Column(
                modifier = Modifier
                    .width(360.dp)
                    .fillMaxHeight()
                    .padding(top = 24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.store_title),
                    style = WhiskrTheme.typography.h3,
                    color = WhiskrTheme.colors.onBackground,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                VipStatusSection(
                    wallet = model.wallet,
                    vipButtonText = vipButtonText,
                    subscriptionProduct = subscriptionProduct,
                    onProductClick = onProductClick
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.store_pack_section),
                    style = WhiskrTheme.typography.h3,
                    color = WhiskrTheme.colors.onBackground,
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 180.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(coinPacks) { product ->
                        ProductCard(
                            product = product,
                            onClick = { onProductClick(product.key) })
                    }
                }
            }
        }

    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = stringResource(Res.string.store_title),
                    style = WhiskrTheme.typography.h3,
                    textAlign = TextAlign.Center,
                    color = WhiskrTheme.colors.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    VipStatusSection(
                        wallet = model.wallet,
                        vipButtonText = vipButtonText,
                        subscriptionProduct = subscriptionProduct,
                        onProductClick = onProductClick,
                        modifier = Modifier.widthIn(max = 500.dp)
                    )
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = stringResource(Res.string.store_pack_section),
                    style = WhiskrTheme.typography.h3,
                    color = WhiskrTheme.colors.onBackground,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }

            items(coinPacks) { product ->
                ProductCard(
                    product = product,
                    onClick = { onProductClick(product.key) }
                )
            }
        }
    }
}

@Composable
private fun VipStatusSection(
    wallet: WalletResponseDto?,
    vipButtonText: String,
    subscriptionProduct: ProductResponseDto?,
    onProductClick: (BillingProductKey) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SubscriptionCard(
            buttonText = vipButtonText,
            isEnabled = wallet?.isPremium == false,
            onSubscribeClick = {
                subscriptionProduct?.let { onProductClick(it.key) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        BalanceSection(
            balance = wallet?.balance ?: 0,
            modifier = Modifier.fillMaxWidth()
        )
    }
}