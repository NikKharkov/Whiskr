package org.example.whiskr.component

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.example.whiskr.data.BillingProductKey
import org.example.whiskr.data.BillingProductType
import org.example.whiskr.data.ProductResponseDto
import org.example.whiskr.dto.WalletResponseDto

class FakeStoreComponent(initialModel: StoreComponent.Model = defaultModel) : StoreComponent {

    override val model: Value<StoreComponent.Model> = MutableValue(initialModel)

    override fun onProductClicked(productKey: BillingProductKey) {}
    override fun onPurchaseSuccessful() {}
    override fun onBackClicked() {}
    override fun onPaymentSheetShown() {}

    companion object {
        val defaultModel = StoreComponent.Model(
            wallet = WalletResponseDto(
                balance = 1250,
                isPremium = false,
                premiumEndsAt = null
            ),
            products = listOf(
                ProductResponseDto(
                    key = BillingProductKey.PREMIUM_MONTHLY,
                    title = "Whiskr Premium",
                    type = BillingProductType.SUBSCRIPTION,
                    priceInCents = 499,
                    rewardAmount = 30
                ),
                ProductResponseDto(
                    key = BillingProductKey.COINS_SMALL,
                    title = "Handful of Coins",
                    type = BillingProductType.COIN_PACK,
                    priceInCents = 199,
                    rewardAmount = 100
                ),
                ProductResponseDto(
                    key = BillingProductKey.COINS_MEDIUM,
                    title = "Bag of Coins",
                    type = BillingProductType.COIN_PACK,
                    priceInCents = 499,
                    rewardAmount = 300
                ),
                ProductResponseDto(
                    key = BillingProductKey.COINS_LARGE,
                    title = "Chest of Coins",
                    type = BillingProductType.COIN_PACK,
                    priceInCents = 999,
                    rewardAmount = 700
                )
            ),
            isLoading = false
        )
    }
}