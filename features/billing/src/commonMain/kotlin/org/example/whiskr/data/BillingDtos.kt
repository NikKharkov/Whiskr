package org.example.whiskr.data

import kotlinx.serialization.Serializable

enum class BillingProductType {
    COIN_PACK,
    SUBSCRIPTION
}

enum class BillingProductKey {
    COINS_SMALL,
    COINS_MEDIUM,
    COINS_LARGE,
    PREMIUM_MONTHLY
}

@Serializable
data class ProductResponseDto(
    val key: BillingProductKey,
    val title: String,
    val type: BillingProductType,
    val priceInCents: Long,
    val rewardAmount: Long
)

@Serializable
data class PurchaseRequestDto(
    val productKey: BillingProductKey
)

@Serializable
data class PurchaseResponseDto(
    val clientSecret: String
)