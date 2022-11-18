package com.listentoprabhupada.common.settings_impl.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.listentoprabhupada.common.settings_api.SettingsState
import com.listentoprabhupada.common.utils.setAppLocale
import kotlinx.coroutines.launch

internal class SettingsStoreFactory(
    private val storeFactory: StoreFactory,
    private val initialState: SettingsState,
) {

    fun create(): SettingsStore =
        object : SettingsStore,
            Store<SettingsIntent, SettingsState, SettingsLabel> by storeFactory.create(
                name = "SettingsStore",
                autoInit = false,
                initialState = initialState,
                bootstrapper = BootstrapperImpl(),
                executorFactory = { ExecutorImpl() },
                reducer = ReducerImpl
            ) {}

    sealed interface Action {
        object CheckFtue : Action
    }

    sealed interface Msg {
        data class Locale(val value: String) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.CheckFtue)
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<SettingsIntent, Action, SettingsState, Msg, SettingsLabel>() {

        override fun executeAction(action: Action, getState: () -> SettingsState) {
            when (action) {
                Action.CheckFtue -> checkFtue() // TODO ftue
            }
        }

        override fun executeIntent(intent: SettingsIntent, getState: () -> SettingsState) {
            when (intent) {
                is SettingsIntent.Locale -> setLocale(intent.value)
            }
        }

        private fun setLocale(value: String) {
            setAppLocale(value)
            dispatch(Msg.Locale(value))
        }

        private fun checkFtue() {
        }
    }

    private object ReducerImpl : Reducer<SettingsState, Msg> {
        override fun SettingsState.reduce(msg: Msg): SettingsState =
            when (msg) {
                is Msg.Locale -> copy(locale = msg.value)
            }
    }
}
