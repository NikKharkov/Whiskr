package org.example.whiskr.data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import org.example.whiskr.dto.WalletResponseDto

interface BillingApiService {
    @Headers("Content-Type: application/json")
    @GET("billing/wallet")
    suspend fun getWallet(): WalletResponseDto

    @Headers("Content-Type: application/json")
    @GET("billing/products")
    suspend fun getCatalog(): List<ProductResponseDto>

    @Headers("Content-Type: application/json")
    @POST("billing/purchase")
    suspend fun purchase(@Body request: PurchaseRequestDto): PurchaseResponseDto
}