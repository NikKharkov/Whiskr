package org.example.whiskr.ui.components

import org.example.whiskr.data.BillingProductKey
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import whiskr.features.billing.generated.resources.Res
import whiskr.features.billing.generated.resources.ic_coin
import whiskr.features.billing.generated.resources.mascot_large
import whiskr.features.billing.generated.resources.mascot_medium
import whiskr.features.billing.generated.resources.mascot_small
import whiskr.features.billing.generated.resources.pack_large
import whiskr.features.billing.generated.resources.pack_medium
import whiskr.features.billing.generated.resources.pack_small
import whiskr.features.billing.generated.resources.pack_unknown

fun BillingProductKey.toTitleRes(): StringResource = when (this) {
    BillingProductKey.COINS_SMALL -> Res.string.pack_small
    BillingProductKey.COINS_MEDIUM -> Res.string.pack_medium
    BillingProductKey.COINS_LARGE -> Res.string.pack_large
    else -> Res.string.pack_unknown
}

fun BillingProductKey.toImageRes(): DrawableResource = when (this) {
    BillingProductKey.COINS_SMALL -> Res.drawable.mascot_small
    BillingProductKey.COINS_MEDIUM -> Res.drawable.mascot_medium
    BillingProductKey.COINS_LARGE -> Res.drawable.mascot_large
    else -> Res.drawable.ic_coin
}

fun Long.toDollarPrice(): String {
    val dollars = this / 100
    val cents = this % 100
    return "$dollars.${cents.toString().padStart(2, '0')}"
}