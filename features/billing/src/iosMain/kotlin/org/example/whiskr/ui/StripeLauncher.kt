package org.example.whiskr.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import cocoapods.StripePaymentSheet.STPPaymentSheet
import cocoapods.StripePaymentSheet.STPPaymentSheetConfiguration
import cocoapods.StripePaymentSheet.STPPaymentSheetResult
import cocoapods.StripePaymentSheet.STPPaymentSheetResultCompleted
import cocoapods.StripePaymentSheet.STPPaymentSheetResultFailed
import cocoapods.StripePaymentSheet.STPPaymentSheetResultCanceled

@Composable
actual fun StripeLauncher(
    clientSecret: String?,
    onPaymentResult: (isSuccess: Boolean) -> Unit,
    onReadyToLaunch: () -> Unit
) {
    LaunchedEffect(clientSecret) {
        if (clientSecret != null) {
            onReadyToLaunch()

            val rootController = UIApplication.sharedApplication.keyWindow?.rootViewController

            if (rootController != null) {
                presentPaymentSheet(rootController, clientSecret, onPaymentResult)
            } else {
                println("Stripe Error: Root View Controller not found")
                onPaymentResult(false)
            }
        }
    }
}

private fun presentPaymentSheet(
    viewController: UIViewController,
    secret: String,
    onResult: (Boolean) -> Unit
) {
    // 2. Настраиваем конфиг (Название в чеке, Apple Pay и т.д.)
    val config = STPPaymentSheetConfiguration().apply {
        merchantDisplayName = "Whiskr App"
        // allowsDelayedPaymentMethods = true // Если нужны всякие Klarna и т.д.
    }

    // 3. Создаем шторку
    val paymentSheet = STPPaymentSheet(
        paymentIntentClientSecret = secret,
        configuration = config
    )

    // 4. Показываем шторку
    // completion - это лямбда, которая сработает после закрытия шторки
    paymentSheet.presentFromViewController(viewController) { result: STPPaymentSheetResult? ->
        when (result) {
            is STPPaymentSheetResultCompleted -> {
                println("Stripe iOS: Completed")
                onResult(true)
            }
            is STPPaymentSheetResultCanceled -> {
                println("Stripe iOS: Canceled")
                onResult(false)
            }
            is STPPaymentSheetResultFailed -> {
                // error прокидывается внутри объекта результата
                println("Stripe iOS: Failed - ${result.error?.localizedDescription}")
                onResult(false)
            }
            else -> {
                onResult(false)
            }
        }
    }
}