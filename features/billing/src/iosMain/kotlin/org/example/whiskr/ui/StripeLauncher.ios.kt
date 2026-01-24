package org.example.whiskr.ui

import kotlin.experimental.ExperimentalObjCName
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import co.touchlab.kermit.Logger

@OptIn(ExperimentalObjCName::class)
@ObjCName(swiftName = "StripeEntryPoint")
object StripeEntryPoint {
    internal var nativeHandler: ((String, (Boolean) -> Unit) -> Unit)? = null

    fun register(handler: (String, (Boolean) -> Unit) -> Unit) {
        nativeHandler = handler
    }
}

@Composable
actual fun StripeLauncher(
    clientSecret: String?,
    onPaymentResult: (isSuccess: Boolean) -> Unit,
    onReadyToLaunch: () -> Unit
) {
    val currentOnPaymentResult = rememberUpdatedState(onPaymentResult)
    val currentOnReadyToLaunch = rememberUpdatedState(onReadyToLaunch)

    LaunchedEffect(clientSecret) {
        if (clientSecret != null) {
            val handler = StripeEntryPoint.nativeHandler

            if (handler != null) {
                currentOnReadyToLaunch.value()

                handler(clientSecret) { success ->
                    currentOnPaymentResult.value(success)
                }
            } else {
                Logger.e { "Stripe Error: Swift implementation not registered! Call register() in AppDelegate." }
                currentOnPaymentResult.value(false)
            }
        }
    }
}