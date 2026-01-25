package org.example.whiskr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch
import org.example.whiskr.component.FakeStoreComponent
import org.example.whiskr.component.StoreComponent
import org.example.whiskr.components.ConfettiOverlay
import org.example.whiskr.data.BillingProductType
import org.example.whiskr.data.WalletResponseDto
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.BalanceSection
import org.example.whiskr.ui.components.ProductCard
import org.example.whiskr.ui.components.SubscriptionCard
import org.example.whiskr.ui.components.toDollarPrice
import org.example.whiskr.ui.components.toIsoDate
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import whiskr.features.billing.generated.resources.Res
import whiskr.features.billing.generated.resources.payment_failed
import whiskr.features.billing.generated.resources.store_pack_section
import whiskr.features.billing.generated.resources.store_title
import whiskr.features.billing.generated.resources.store_vip_active_until
import whiskr.features.billing.generated.resources.store_vip_price_format
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun StoreScreen(
    component: StoreComponent
) {
    val model by component.model.subscribeAsState()

    var confettiTrigger by remember { mutableStateOf(0L) }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarText = stringResource(Res.string.payment_failed)
    val scope = rememberCoroutineScope()

    val subscriptionProduct = remember(model.products) {
        model.products.find { it.type == BillingProductType.SUBSCRIPTION }
    }
    val coinPacks = remember(model.products) {
        model.products.filter { it.type == BillingProductType.COIN_PACK }
    }

    val subButtonText =
        if (model.wallet?.isPremium == true && model.wallet?.premiumEndsAt != null) {
            stringResource(
                Res.string.store_vip_active_until,
                model.wallet?.premiumEndsAt.toIsoDate()
            )
        } else {
            val price = subscriptionProduct?.priceInCents?.toDollarPrice() ?: "--"
            stringResource(Res.string.store_vip_price_format, price)
        }

    StripeLauncher(
        clientSecret = model.paymentLaunchSecret,
        onReadyToLaunch = { component.onPaymentSheetShown() },
        onPaymentResult = { success ->
            if (success) {
                component.onPurchaseSuccessful()
                confettiTrigger = Clock.System.now().toEpochMilliseconds()
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = snackbarText,
                        withDismissAction = true
                    )
                }
            }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = WhiskrTheme.colors.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        when {
            model.isLoading && model.products.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = WhiskrTheme.colors.primary)
                }
            }

            model.error != null && model.products.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = model.error ?: "Error", color = WhiskrTheme.colors.error)
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding() + 16.dp,
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = stringResource(Res.string.store_title),
                            color = WhiskrTheme.colors.onBackground,
                            textAlign = TextAlign.Center,
                            style = WhiskrTheme.typography.h3,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            SubscriptionCard(
                                buttonText = subButtonText,
                                isEnabled = model.wallet?.isPremium == false,
                                onSubscribeClick = {
                                    subscriptionProduct?.let { component.onProductClicked(it.key) }
                                },
                                modifier = Modifier.widthIn(max = 600.dp)
                            )
                        }
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            BalanceSection(
                                balance = model.wallet?.balance ?: 0,
                                modifier = Modifier
                                    .widthIn(max = 600.dp)
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = stringResource(Res.string.store_pack_section),
                            style = WhiskrTheme.typography.h3,
                            color = WhiskrTheme.colors.onBackground,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(coinPacks) { product ->
                        ProductCard(
                            product = product,
                            onClick = { component.onProductClicked(product.key) }
                        )
                    }
                }
            }
        }

        ConfettiOverlay(
            trigger = confettiTrigger,
            colors = WhiskrTheme.colors.vipGradient
        )
    }
}

@Preview(name = "Light Mode - Standard", showBackground = true)
@Composable
private fun StoreScreenPreview() {
    WhiskrTheme(isDarkTheme = false) {
        StoreScreen(
            component = FakeStoreComponent()
        )
    }
}

@Preview(name = "Dark Mode - Standard", showBackground = true)
@Composable
private fun StoreScreenDarkPreview() {
    WhiskrTheme(isDarkTheme = true) {
        StoreScreen(
            component = FakeStoreComponent()
        )
    }
}

@Preview(name = "Light Mode - Premium Active", showBackground = true)
@Composable
private fun StoreScreenPremiumPreview() {
    val premiumModel = FakeStoreComponent.defaultModel.copy(
        wallet = WalletResponseDto(
            balance = 5000,
            isPremium = true,
            premiumEndsAt = "2026-05-20T12:00:00"
        )
    )

    WhiskrTheme(isDarkTheme = false) {
        StoreScreen(
            component = FakeStoreComponent(initialModel = premiumModel)
        )
    }
}

@Preview(name = "Tablet", widthDp = 800, heightDp = 600, showBackground = true)
@Composable
private fun StoreScreenTabletPreview() {
    WhiskrTheme(isDarkTheme = true) {
        StoreScreen(
            component = FakeStoreComponent()
        )
    }
}