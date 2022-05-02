package com.prabhupadalectures.common.edit.integration

import com.prabhupadalectures.common.edit.TodoEdit.Model
import com.prabhupadalectures.common.edit.store.TodoEditStore.State

internal val stateToModel: (State) -> Model =
    {
        Model(
            text = it.text,
            isDone = it.isDone
        )
    }
