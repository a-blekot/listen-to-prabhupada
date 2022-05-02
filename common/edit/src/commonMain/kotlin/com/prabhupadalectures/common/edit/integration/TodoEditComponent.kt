package com.prabhupadalectures.common.edit.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.prabhupadalectures.common.edit.TodoEdit
import com.prabhupadalectures.common.edit.TodoEdit.Model
import com.prabhupadalectures.common.edit.TodoEdit.Output
import com.prabhupadalectures.common.edit.store.TodoEditStore.Intent
import com.prabhupadalectures.common.edit.store.TodoEditStoreProvider
import com.prabhupadalectures.common.utils.Consumer
import com.prabhupadalectures.common.utils.asValue
import com.prabhupadalectures.common.utils.getStore
import kotlin.coroutines.CoroutineContext

class TodoEditComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    itemId: Long,
    ioContext: CoroutineContext,
    private val output: Consumer<Output>
) : TodoEdit, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            TodoEditStoreProvider(
                storeFactory = storeFactory,
                database = TodoEditStoreDatabase(),
                id = itemId,
                ioContext = ioContext
            ).provide()
        }

    override val models: Value<Model> = store.asValue().map { stateToModel(it) }

    override fun onTextChanged(text: String) {
        store.accept(Intent.SetText(text = text))
    }

    override fun onDoneChanged(isDone: Boolean) {
        store.accept(Intent.SetDone(isDone = isDone))
    }

    override fun onCloseClicked() {
        output(Output.Finished)
    }
}
