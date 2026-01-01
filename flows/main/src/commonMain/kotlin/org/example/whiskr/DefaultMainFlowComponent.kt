package org.example.whiskr

import com.arkivanov.decompose.ComponentContext
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class DefaultMainFlowComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onSignOut: () -> Unit,
) : MainFlowComponent, ComponentContext by componentContext {


}