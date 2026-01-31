package org.example.whiskr.component.viewer

import com.arkivanov.decompose.ComponentContext
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class DefaultNewsViewerComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted override val url: String,
    @Assisted private val onBack: () -> Unit
) : NewsViewerComponent, ComponentContext by componentContext {

    override fun onBackClick() = onBack()
}