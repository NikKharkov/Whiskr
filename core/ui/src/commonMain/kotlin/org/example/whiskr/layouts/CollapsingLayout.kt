package org.example.whiskr.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.example.whiskr.theme.WhiskrTheme
import kotlin.math.roundToInt

@Composable
fun CollapsingTopBarScaffold(
    topBarContentHeight: Dp,
    useStatusBarPadding: Boolean = true,
    topBar: @Composable (Modifier) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val density = LocalDensity.current
    val focusManager = LocalFocusManager.current

    val statusBarHeight = if (useStatusBarPadding) {
        WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    } else 0.dp

    val totalToolbarHeight = topBarContentHeight + statusBarHeight
    val totalToolbarHeightPx = with(density) { totalToolbarHeight.toPx() }

    val collapsingState = rememberCollapsingState(
        maxHeight = totalToolbarHeightPx,
        onUserScroll = { focusManager.clearFocus() }
    )

    Scaffold(
        containerColor = WhiskrTheme.colors.background,
        contentWindowInsets = WindowInsets.ime,
        modifier = Modifier.nestedScroll(collapsingState.nestedScrollConnection)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            content(PaddingValues(top = totalToolbarHeight))

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(totalToolbarHeight)
                    .offset { collapsingState.toIntOffset() }
                    .background(WhiskrTheme.colors.background)
                    .then(if (useStatusBarPadding) Modifier.statusBarsPadding() else Modifier)
            ) {
                topBar(Modifier.fillMaxSize())
            }
        }
    }
}
@Composable
fun rememberCollapsingState(
    maxHeight: Float,
    onUserScroll: () -> Unit = {}
): CollapsingState {
    val density = LocalDensity.current
    return remember(maxHeight, density) {
        CollapsingState(
            maxHeight = maxHeight,
            onUserScroll = onUserScroll
        )
    }
}

class CollapsingState(
    val maxHeight: Float,
    private val onUserScroll: () -> Unit
) {
    var offset by mutableStateOf(0f)
        private set

    val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            if (source == NestedScrollSource.UserInput) {
                onUserScroll()
            }

            val delta = available.y
            val newOffset = offset + delta
            offset = newOffset.coerceIn(-maxHeight, 0f)

            return Offset.Zero
        }
    }

    fun toIntOffset() = IntOffset(x = 0, y = offset.roundToInt())
}