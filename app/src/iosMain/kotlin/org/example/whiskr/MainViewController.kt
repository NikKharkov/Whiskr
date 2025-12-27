package org.example.whiskr

import androidx.compose.ui.window.ComposeUIViewController
import org.example.whiskr.root.RootComponent

fun MainViewController(root: RootComponent) =
    ComposeUIViewController {
        RootContent(rootComponent = root)
    }
