package com.prabhupadalectures.common.edit.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.prabhupadalectures.common.edit.TodoItem
import com.prabhupadalectures.common.edit.store.TodoEditStore.Intent
import com.prabhupadalectures.common.edit.store.TodoEditStore.Label
import com.prabhupadalectures.common.edit.store.TodoEditStore.State
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class TodoEditStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database,
    private val id: Long,
    private val ioContext: CoroutineContext
) {

    fun provide(): TodoEditStore =
        object : TodoEditStore, Store<Intent, State, Label> by storeFactory.create(
            name = "EditStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class Loaded(val item: TodoItem) : Msg()
        data class TextChanged(val text: String) : Msg()
        data class DoneChanged(val isDone: Boolean) : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeAction(action: Unit, getState: () -> State) {
            scope.launch {
                val item: TodoItem? = withContext(ioContext) { database.load(id = id) }
                item?.let { dispatch(Msg.Loaded(it)) }
            }
        }

        override fun executeIntent(intent: Intent, getState: () -> State) =
            when (intent) {
                is Intent.SetText -> setText(text = intent.text, state = getState())
                is Intent.SetDone -> setDone(isDone = intent.isDone, state = getState())
            }

        private fun setText(text: String, state: State) {
            scope.launch {
                withContext(ioContext) { database.setText(id = id, text = text) }
                dispatch(Msg.TextChanged(text = text))
                publish(Label.Changed(TodoItem(text = text, isDone = state.isDone)))
            }
        }

        private fun setDone(isDone: Boolean, state: State) {
            scope.launch {
                withContext(ioContext) { database.setDone(id = id, isDone = isDone) }
                dispatch(Msg.DoneChanged(isDone = isDone))
                publish(Label.Changed(TodoItem(text = state.text, isDone = isDone)))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.Loaded -> copy(text = msg.item.text, isDone = msg.item.isDone)
                is Msg.TextChanged -> copy(text = msg.text)
                is Msg.DoneChanged -> copy(isDone = msg.isDone)
            }
    }

    interface Database {
        suspend fun load(id: Long): TodoItem?
        suspend fun setText(id: Long, text: String)
        suspend fun setDone(id: Long, isDone: Boolean)
    }
}
