package org.example.whiskr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.component.StoreComponent
import org.example.whiskr.data.BillingProductType
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.BalanceSection
import org.example.whiskr.ui.components.ProductCard
import org.example.whiskr.ui.components.SubscriptionCard
import org.jetbrains.compose.resources.stringResource
import whiskr.features.billing.generated.resources.Res
import whiskr.features.billing.generated.resources.store_pack_section
import whiskr.features.billing.generated.resources.store_title


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    component: StoreComponent
) {
    val model by component.model.subscribeAsState()

    StripeLauncher(
        clientSecret = model.paymentLaunchSecret,
        onReadyToLaunch = { component.onPaymentSheetShown() },
        onPaymentResult = { success ->
            if (success) {
                component.onPurchaseSuccessful()
            } else {
                // TODO: Snackbar error
            }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = WhiskrTheme.colors.background,
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            )
                        }
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                SubscriptionCard(
                                    onSubscribeClick = { /* TODO: Vip flow */ },
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

                        items(model.products.filter { it.type != BillingProductType.SUBSCRIPTION }) { product ->
                            ProductCard(
                                product = product,
                                onClick = { component.onProductClicked(product.key) }
                            )
                        }
                    }
                }
            }
        }
    }
}