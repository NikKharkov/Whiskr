package org.example.whiskr.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import co.touchlab.kermit.Logger
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheet.Builder
import com.stripe.android.paymentsheet.PaymentSheetResult

@Composable
actual fun StripeLauncher(
    clientSecret: String?,
    onPaymentResult: (isSuccess: Boolean) -> Unit,
    onReadyToLaunch: () -> Unit
) {
    if (LocalInspectionMode.current) {
        return
    }

    val paymentResultCallback = { result: PaymentSheetResult ->
        when (result) {
            is PaymentSheetResult.Completed -> {
                Logger.d { "Stripe: Payment Completed" }
                onPaymentResult(true)
            }

            is PaymentSheetResult.Canceled -> {
                Logger.d { "Stripe: Payment Canceled" }
                onPaymentResult(false)
            }

            is PaymentSheetResult.Failed -> {
                Logger.e(result.error) { "Stripe: Payment Failed" }
                onPaymentResult(false)
            }
        }
    }

    val paymentSheet = remember(paymentResultCallback) { Builder(paymentResultCallback) }.build()

    LaunchedEffect(clientSecret) {
        if (clientSecret != null) {
            onReadyToLaunch()

            paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret = clientSecret,
                configuration = PaymentSheet.Configuration(
                    merchantDisplayName = "Whiskr"
                )
            )
        }
    }
}