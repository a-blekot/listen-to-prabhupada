package com.prabhupadalectures.common.main.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.prabhupadalectures.common.main.TodoMain
import com.prabhupadalectures.common.main.TodoMain.Model
import com.prabhupadalectures.common.main.TodoMain.Output
import com.prabhupadalectures.common.main.store.TodoMainStore.Intent
import com.prabhupadalectures.common.main.store.TodoMainStoreProvider
import com.prabhupadalectures.common.utils.Consumer
import com.prabhupadalectures.common.utils.asValue
import com.prabhupadalectures.common.utils.getStore
import kotlin.coroutines.CoroutineContext

class TodoMainComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    ioContext: CoroutineContext,
    private val output: Consumer<Output>
) : TodoMain, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            TodoMainStoreProvider(
                storeFactory = storeFactory,
                database = TodoMainStoreDatabase(),
                ioContext = ioContext
            ).provide()
        }

    override val models: Value<Model> = store.asValue().map(stateToModel)

    override fun onItemClicked(id: Long) {
        output(Output.Selected(id = id))
    }

    override fun onItemDoneChanged(id: Long, isDone: Boolean) {
        store.accept(Intent.SetItemDone(id = id, isDone = isDone))
    }

    override fun onItemDeleteClicked(id: Long) {
        store.accept(Intent.DeleteItem(id = id))
    }

    override fun onInputTextChanged(text: String) {
        store.accept(Intent.SetText(text = text))
    }

    override fun onAddItemClicked() {
        store.accept(Intent.AddItem)
    }
}
