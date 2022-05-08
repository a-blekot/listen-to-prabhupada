package com.prabhupadalectures.common.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.prabhupadalectures.common.filters.Filters
import com.prabhupadalectures.common.filters.FiltersComponent
import com.prabhupadalectures.common.root.Root.Child.ChildFilters
import com.prabhupadalectures.common.root.Root.Child.ChildResults
import com.prabhupadalectures.common.utils.Consumer
import com.prabhupadalectures.lectures.mvi.lectures.Results
import com.prabhupadalectures.lectures.mvi.lectures.ResultsComponent

class RootComponent internal constructor(
    componentContext: ComponentContext,
    private val results: (ComponentContext, Consumer<Results.Output>) -> Results,
    private val filters: (ComponentContext, Consumer<Filters.Output>) -> Filters
) : Root, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        depsResults: com.prabhupadalectures.lectures.mvi.Dependencies,
        depsFilters: com.prabhupadalectures.common.filters.Dependencies,
        storeFactory: StoreFactory,
    ) : this(
        componentContext = componentContext,
        results = { childContext, output ->
            ResultsComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                deps = depsResults,
                output = output
            )
        },
        filters = { childContext, output ->
            FiltersComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                deps = depsFilters,
                output = output
            )
        }
    )

    private val router =
        router<Configuration, Root.Child>(
            initialConfiguration = Configuration.Results,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override val routerState: Value<RouterState<*, Root.Child>> = router.state

    private fun createChild(configuration: Configuration, componentContext: ComponentContext): Root.Child =
        when (configuration) {
            is Configuration.Results -> ChildResults(results(componentContext, Consumer(::onResultsOutput)))
            is Configuration.Filters -> ChildFilters(filters(componentContext, Consumer(::onFiltersOutput)))
        }

    private fun onResultsOutput(output: Results.Output): Unit =
        when (output) {
            is Results.Output.EditFilters -> router.push(Configuration.Filters)
        }

    private fun onFiltersOutput(output: Filters.Output): Unit =
        when (output) {
            is Filters.Output.ShowResults -> router.pop { isSuccess ->
                if (isSuccess) {
                    (router.activeChild.instance as? ChildResults)?.component?.onUpdateFilters()
                }
            }
        }

    private sealed class Configuration : Parcelable {
        @Parcelize
        object Results : Configuration()

        @Parcelize
        object Filters : Configuration()
    }
}
