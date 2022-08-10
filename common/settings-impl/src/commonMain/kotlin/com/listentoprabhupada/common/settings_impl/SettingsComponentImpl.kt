package com.listentoprabhupada.common.settings_impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.listentoprabhupada.common.settings_api.SettingsComponent
import com.listentoprabhupada.common.settings_api.SettingsOutput
import com.listentoprabhupada.common.settings_api.SettingsState
import com.listentoprabhupada.common.settings_impl.store.SettingsIntent.*
import com.listentoprabhupada.common.settings_impl.store.SettingsStoreFactory
import com.listentoprabhupada.common.utils.Consumer
import com.listentoprabhupada.common.utils.asValue
import com.listentoprabhupada.common.utils.getStore


class SettingsComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    deps: SettingsDeps,
    private val output: Consumer<SettingsOutput>
) : SettingsComponent, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            SettingsStoreFactory(
                storeFactory = storeFactory,
                deps = deps,
            ).create()
        }

    override val flow: Value<SettingsState> = store.asValue()

    override fun onNext() = store.accept(Next)
    override fun onPrev() = store.accept(Prev)
}
