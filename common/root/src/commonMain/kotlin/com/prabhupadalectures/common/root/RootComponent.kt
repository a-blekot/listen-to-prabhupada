package com.prabhupadalectures.common.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.prabhupadalectures.common.feature_results_api.ResultsComponent
import com.prabhupadalectures.common.feature_results_api.ResultsOutput
import com.prabhupadalectures.common.feature_results_impl.ResultsComponentImpl
import com.prabhupadalectures.common.feature_results_impl.ResultsDeps
import com.prabhupadalectures.common.filters.Dependencies
import com.prabhupadalectures.common.filters.Filters
import com.prabhupadalectures.common.filters.FiltersComponent
import com.prabhupadalectures.common.root.Root.Child.ChildFilters
import com.prabhupadalectures.common.root.Root.Child.ChildResults
import com.prabhupadalectures.common.utils.Consumer

class RootComponent internal constructor(
    componentContext: ComponentContext,
    private val results: (ComponentContext, Consumer<ResultsOutput>) -> ResultsComponent,
    private val filters: (ComponentContext, Consumer<Filters.Output>) -> Filters
) : Root, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        deps: RootDeps,
    ) : this(
        componentContext = componentContext,
        results = { childContext, output ->
            ResultsComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                deps = deps.run { ResultsDeps(db, api, playerBus, ioContext, mainContext) },
                output = output
            )
        },
        filters = { childContext, output ->
            FiltersComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                deps = deps.run { Dependencies(db, api, ioContext, mainContext) },
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

    private fun onResultsOutput(output: ResultsOutput): Unit =
        when (output) {
            is ResultsOutput.EditFilters -> router.push(Configuration.Filters)
            else -> { /** TODO **/}
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
