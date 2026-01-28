package org.example.whiskr.data

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.domain.BillingRepository
import org.example.whiskr.dto.WalletResponseDto

@Inject
class BillingRepositoryImpl(
    private val billingApiService: BillingApiService
) : BillingRepository {

    private val _wallet = MutableValue(WalletResponseDto(0, false, null))
    override val wallet: Value<WalletResponseDto> = _wallet

    override suspend fun getWallet(): Result<WalletResponseDto> {
        return runCatching { billingApiService.getWallet() }
            .onSuccess { newWallet ->
                _wallet.value = newWallet
            }
    }

    override suspend fun getCatalog(): Result<List<ProductResponseDto>> {
        return runCatching { billingApiService.getCatalog() }
    }

    override suspend fun purchase(key: BillingProductKey): Result<PurchaseResponseDto> {
        return runCatching { billingApiService.purchase(PurchaseRequestDto(key)) }
    }
}