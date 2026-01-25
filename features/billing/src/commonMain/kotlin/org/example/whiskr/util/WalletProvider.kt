package org.example.whiskr.util

import androidx.compose.runtime.staticCompositionLocalOf
import org.example.whiskr.data.WalletResponseDto

val LocalWalletProvider = staticCompositionLocalOf { WalletResponseDto(balance = 0, isPremium = false, premiumEndsAt = null) }