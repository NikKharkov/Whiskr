package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.data.BillingProductKey
import org.example.whiskr.domain.BillingRepository

@Inject
class DefaultStoreComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onBack: () -> Unit,
    private val billingRepository: BillingRepository
) : StoreComponent, ComponentContext by componentContext {

    private val _model = MutableValue(StoreComponent.Model(isLoading = true))
    override val model: Value<StoreComponent.Model> = _model

    private val scope = componentScope()

    init {
        loadData()
    }

    private fun loadData() {
        scope.launch {
            _model.value = _model.value.copy(isLoading = true, error = null)

            val walletResult = billingRepository.getWallet()
            val productsResult = billingRepository.getCatalog()

            if (walletResult.isSuccess && productsResult.isSuccess) {
                _model.value = _model.value.copy(
                    wallet = walletResult.getOrNull(),
                    products = productsResult.getOrNull() ?: emptyList(),
                    isLoading = false
                )
            } else {
                _model.value = _model.value.copy(
                    isLoading = false,
                    error = "Failed to load shop data"
                )
            }
        }
    }

    override fun onProductClicked(productKey: BillingProductKey) {
        scope.launch {
            _model.value = _model.value.copy(isLoading = true)

            billingRepository.purchase(productKey)
                .onSuccess { response ->
                    _model.value = _model.value.copy(
                        isLoading = false,
                        paymentLaunchSecret = response.clientSecret
                    )
                }
                .onFailure {
                    _model.value = _model.value.copy(
                        isLoading = false,
                        error = "Failed to initiate purchase: ${it.message}"
                    )
                }
        }
    }

    override fun onPurchaseSuccessful() {
        onPaymentSheetShown()

        scope.launch {
            billingRepository.getWallet().onSuccess { newWallet ->
                _model.value = _model.value.copy(wallet = newWallet)
            }
        }
    }

    override fun onPaymentSheetShown() {
        _model.value = _model.value.copy(paymentLaunchSecret = null)
    }

    override fun onBackClicked() = onBack()
}