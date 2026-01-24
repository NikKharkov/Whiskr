package org.example.whiskr.domain

import androidx.compose.runtime.staticCompositionLocalOf

interface PaymentHandler {
    fun launchStripe(clientSecret: String, onResult: (Boolean) -> Unit)
}

val LocalPaymentHandler = staticCompositionLocalOf<PaymentHandler> {
    error("PaymentHandler not provided!")
}