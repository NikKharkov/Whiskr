package org.example.whiskr.data

import me.tatarka.inject.annotations.Inject
import org.example.whiskr.domain.BillingRepository

@Inject
class BillingRepositoryImpl(
    private val billingApiService: BillingApiService
) : BillingRepository {

    override suspend fun getWallet(): Result<WalletResponseDto> {
        return runCatching { billingApiService.getWallet() }
    }

    override suspend fun getCatalog(): Result<List<ProductResponseDto>> {
        return runCatching { billingApiService.getCatalog() }
    }

    override suspend fun purchase(key: BillingProductKey): Result<PurchaseResponseDto> {
        return runCatching { billingApiService.purchase(PurchaseRequestDto(key)) }
    }
}