package com.listentoprabhupada.common.settings_impl.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.listentoprabhupada.common.settings_impl.SettingsDeps
import com.listentoprabhupada.common.settings_api.SettingsState
import io.github.aakira.napier.Napier

internal class SettingsStoreFactory(
    private val storeFactory: StoreFactory,
    private val deps: SettingsDeps
) {

    fun create(): SettingsStore =
        object : SettingsStore,
            Store<SettingsIntent, SettingsState, SettingsLabel> by storeFactory.create(
                name = "SettingsStore",
                initialState = SettingsState(),
                executorFactory = { ExecutorImpl() },
                reducer = ReducerImpl
            ) {}

    private sealed interface Msg {
        object StartLoading : Msg
        data class LoadingComplete(val state: SettingsState = SettingsState()) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<SettingsIntent, Unit, SettingsState, Msg, SettingsLabel>() {
        override fun executeAction(action: Unit, getState: () -> SettingsState) {}

        override fun executeIntent(intent: SettingsIntent, getState: () -> SettingsState) {
            if (getState().isLoading) {
                Napier.d("executeIntent canceled, isLoading = true!", tag = "SettingsStoreExecutor")
                return
            }

            when (intent) {
                SettingsIntent.Next -> {}
                SettingsIntent.Prev -> {}
                else -> {
                    /** do nothing **/
                }
            }
        }
    }

    private object ReducerImpl : Reducer<SettingsState, Msg> {
        override fun SettingsState.reduce(msg: Msg): SettingsState =
            when (msg) {
                Msg.StartLoading -> copy(isLoading = true)
                is Msg.LoadingComplete -> msg.state
            }
    }
}
