package com.prabhupadalectures.common.main.integration

import com.prabhupadalectures.common.main.TodoItem
import com.prabhupadalectures.common.main.store.TodoMainStoreProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

internal class TodoMainStoreDatabase(
//    private val database: TodoSharedDatabase
) : TodoMainStoreProvider.Database {

    override val updates: Flow<List<TodoItem>> =
        flow {
            var counter = 1L
            GlobalScope.launch {
                delay(1000L)
                emit(listOf(TodoItem(id = counter++, text = "Text $counter")))
            }
        }
//        database
//            .observeAll()
//            .map { it.map { dbEntity -> dbEntity.toItem() } }


    override suspend fun setDone(id: Long, isDone: Boolean) {}
//        database.setDone(id = id, isDone = isDone)

    override suspend fun delete(id: Long) {}
//        database.delete(id = id)

    override suspend fun add(text: String) {}
//        database.add(text = text)
}
