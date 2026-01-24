package org.example.whiskr.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.component.StoreComponent
import org.example.whiskr.data.BillingProductKey

@Composable
fun StoreScreen(
    component: StoreComponent
) {
    val model by component.model.subscribeAsState()

    StripeLauncher(
        clientSecret = model.paymentLaunchSecret,
        onReadyToLaunch = {
            component.onPaymentSheetShown()
        },
        onPaymentResult = { success ->
            if (success) {
                TODO("Confetti")
            } else {
                TODO("Snackbar failer")
            }
        }
    )

    Column {
        Button(onClick = { component.onProductClicked(BillingProductKey.COINS_SMALL) }) {
            Text("Купить")
        }
    }
}