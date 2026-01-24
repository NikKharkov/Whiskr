package org.example.whiskr.ui

import androidx.compose.runtime.Composable

@Composable
expect fun StripeLauncher(
    clientSecret: String?,
    onPaymentResult: (isSuccess: Boolean) -> Unit,
    onReadyToLaunch: () -> Unit
)