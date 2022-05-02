package com.prabhupadalectures.common.main.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.prabhupadalectures.common.main.TodoItem
import com.prabhupadalectures.common.main.store.TodoMainStore.Intent
import com.prabhupadalectures.common.main.store.TodoMainStore.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class TodoMainStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database,
    private val ioContext: CoroutineContext
) {

    fun provide(): TodoMainStore =
        object : TodoMainStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = "TodoListStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class ItemsLoaded(val items: List<TodoItem>) : Msg()
        data class ItemDoneChanged(val id: Long, val isDone: Boolean) : Msg()
        data class ItemDeleted(val id: Long) : Msg()
        data class TextChanged(val text: String) : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Nothing>() {
        override fun executeAction(action: Unit, getState: () -> State) {
            scope.launch(ioContext) {
                database.updates
                    .map { Msg.ItemsLoaded(it) }
                    .onEach(::dispatch)
            }
        }

        override fun executeIntent(intent: Intent, getState: () -> State): Unit =
            when (intent) {
                is Intent.SetItemDone -> setItemDone(id = intent.id, isDone = intent.isDone)
                is Intent.DeleteItem -> deleteItem(id = intent.id)
                is Intent.SetText -> dispatch(Msg.TextChanged(text = intent.text))
                is Intent.AddItem -> addItem(state = getState())
            }

        private fun setItemDone(id: Long, isDone: Boolean) {
            scope.launch(ioContext) {
                database.setDone(id = id, isDone = isDone)
                dispatch(Msg.ItemDoneChanged(id = id, isDone = isDone))
            }
        }

        private fun deleteItem(id: Long) {
            scope.launch(ioContext) {
                database.delete(id = id)
                dispatch(Msg.ItemDeleted(id = id))
            }
        }

        private fun addItem(state: State) {
            if (state.text.isNotEmpty()) {
                scope.launch(ioContext) {
                    database.add(text = state.text)
                    dispatch(Msg.TextChanged(text = ""))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ItemsLoaded -> copy(items = msg.items.sorted())
                is Msg.ItemDoneChanged -> update(id = msg.id) { copy(isDone = msg.isDone) }
                is Msg.ItemDeleted -> copy(items = items.filterNot { it.id == msg.id })
                is Msg.TextChanged -> copy(text = msg.text)
            }

        private inline fun State.update(id: Long, func: TodoItem.() -> TodoItem): State {
            val item = items.find { it.id == id } ?: return this

            return put(item.func())
        }

        private fun State.put(item: TodoItem): State {
            val oldItems = items.associateByTo(mutableMapOf(), TodoItem::id)
            val oldItem: TodoItem? = oldItems.put(item.id, item)

            return copy(items = if (oldItem?.order == item.order) oldItems.values.toList() else oldItems.values.sorted())
        }

        private fun Iterable<TodoItem>.sorted(): List<TodoItem> = sortedByDescending(TodoItem::order)
    }

    interface Database {
        val updates: Flow<List<TodoItem>>

        suspend fun setDone(id: Long, isDone: Boolean)

        suspend fun delete(id: Long)

        suspend fun add(text: String)
    }
}
