package org.example.whiskr.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch
import org.example.whiskr.component.FakeStoreComponent
import org.example.whiskr.component.StoreComponent
import org.example.whiskr.components.ConfettiOverlay
import org.example.whiskr.data.BillingProductType
import org.example.whiskr.dto.WalletResponseDto
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.StoreContent
import org.example.whiskr.ui.components.toDollarPrice
import org.example.whiskr.ui.components.toIsoDate
import org.jetbrains.compose.resources.stringResource
import whiskr.features.billing.generated.resources.Res
import whiskr.features.billing.generated.resources.payment_failed
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

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var confettiTrigger by remember { mutableStateOf(0L) }

    val subscriptionProduct = remember(model.products) {
        model.products.find { it.type == BillingProductType.SUBSCRIPTION }
    }
    val coinPacks = remember(model.products) {
        model.products.filter { it.type == BillingProductType.COIN_PACK }
    }

    val vipButtonText =
        if (model.wallet?.isPremium == true && model.wallet?.premiumEndsAt != null) {
            stringResource(
                Res.string.store_vip_active_until,
                model.wallet?.premiumEndsAt.toIsoDate()
            )
        } else {
            stringResource(
                Res.string.store_vip_price_format,
                subscriptionProduct?.priceInCents?.toDollarPrice() ?: "--"
            )
        }

    val snackbarText = stringResource(Res.string.payment_failed)

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
        Box(modifier = Modifier.fillMaxSize()) {
            StoreContent(
                model = model,
                contentPadding = innerPadding,
                vipButtonText = vipButtonText,
                subscriptionProduct = subscriptionProduct,
                coinPacks = coinPacks,
                onProductClick = component::onProductClicked
            )

            ConfettiOverlay(
                trigger = confettiTrigger,
                colors = WhiskrTheme.colors.vipGradient
            )
        }
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