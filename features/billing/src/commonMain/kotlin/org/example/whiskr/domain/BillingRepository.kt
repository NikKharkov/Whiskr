package org.example.whiskr.domain

import com.arkivanov.decompose.value.Value
import org.example.whiskr.data.BillingProductKey
import org.example.whiskr.data.ProductResponseDto
import org.example.whiskr.data.PurchaseResponseDto
import org.example.whiskr.data.WalletResponseDto

interface BillingRepository {
    val wallet: Value<WalletResponseDto>
    suspend fun getWallet(): Result<WalletResponseDto>
    suspend fun getCatalog(): Result<List<ProductResponseDto>>
    suspend fun purchase(key: BillingProductKey): Result<PurchaseResponseDto>
}