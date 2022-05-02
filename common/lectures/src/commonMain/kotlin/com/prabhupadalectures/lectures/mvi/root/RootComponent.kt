package com.prabhupadalectures.lectures.mvi.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.prabhupadalectures.lectures.mvi.Dependencies
import com.prabhupadalectures.lectures.mvi.lectures.Results
import com.prabhupadalectures.lectures.mvi.lectures.ResultsComponent
import com.prabhupadalectures.lectures.mvi.root.Root.Child
import kotlin.coroutines.CoroutineContext

class RootComponent internal constructor(
    componentContext: ComponentContext,
    private val results: (ComponentContext) -> Results,
) : Root, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        deps: Dependencies
    ) : this(
        componentContext = componentContext,
        results = { childContext ->
            ResultsComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                deps = deps,
            )
        },
    )

    private val router =
        router<Configuration, Child>(
            initialConfiguration = Configuration.Results,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override val routerState: Value<RouterState<*, Child>> = router.state

    private fun createChild(configuration: Configuration, componentContext: ComponentContext): Child =
        when (configuration) {
            is Configuration.Results -> Child.ResultsChild(results(componentContext))
        }

    private sealed class Configuration : Parcelable {
        @Parcelize
        object Results : Configuration()
    }
}