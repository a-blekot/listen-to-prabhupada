package com.prabhupadalectures.common.main.integration

import com.prabhupadalectures.common.main.TodoMain.Model
import com.prabhupadalectures.common.main.store.TodoMainStore.State

internal val stateToModel: (State) -> Model =
    {
        Model(
            items = it.items,
            text = it.text
        )
    }