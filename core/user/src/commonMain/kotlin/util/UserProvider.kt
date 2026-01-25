package util

import androidx.compose.runtime.staticCompositionLocalOf
import domain.UserState

val LocalUser = staticCompositionLocalOf<UserState?> { error("User not provided") }