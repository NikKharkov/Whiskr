package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import org.example.whiskr.data.BillingProductKey
import org.example.whiskr.data.ProductResponseDto
import org.example.whiskr.data.WalletResponseDto

interface StoreComponent {

    val model: Value<Model>

    fun onProductClicked(productKey: BillingProductKey)
    fun onPurchaseSuccessful()
    fun onBackClicked()
    fun onPaymentSheetShown()

    data class Model(
        val wallet: WalletResponseDto? = null,
        val products: List<ProductResponseDto> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val paymentLaunchSecret: String? = null
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onBack: () -> Unit
        ): StoreComponent
    }
}