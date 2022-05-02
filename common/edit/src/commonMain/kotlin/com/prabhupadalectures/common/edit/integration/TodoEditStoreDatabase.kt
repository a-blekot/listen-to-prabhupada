package com.prabhupadalectures.common.edit.integration

import com.prabhupadalectures.common.edit.TodoItem
import com.prabhupadalectures.common.edit.store.TodoEditStoreProvider.Database

internal class TodoEditStoreDatabase(
//    private val database: TodoSharedDatabase
) : Database {

    override suspend fun load(id: Long): TodoItem? =
        TodoItem("", false)
//        database.select(id = id)?.toItem()

    override suspend fun setText(id: Long, text: String) {}
//        database.setText(id = id, text = text)

    override suspend fun setDone(id: Long, isDone: Boolean) {}
//        database.setDone(id = id, isDone = isDone)
}
