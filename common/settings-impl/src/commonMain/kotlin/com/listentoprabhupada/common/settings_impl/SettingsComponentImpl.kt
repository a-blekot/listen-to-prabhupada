package com.listentoprabhupada.common.settings_impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.statekeeper.consume
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.listentoprabhupada.common.settings.getLocale
import com.listentoprabhupada.common.settings.isTutorialCompleted
import com.listentoprabhupada.common.settings.setTutorialCompleted
import com.listentoprabhupada.common.settings_api.SettingsComponent
import com.listentoprabhupada.common.settings_api.SettingsOutput
import com.listentoprabhupada.common.settings_api.SettingsState
import com.listentoprabhupada.common.settings_impl.store.SettingsIntent.Locale
import com.listentoprabhupada.common.settings_impl.store.SettingsStoreFactory
import com.listentoprabhupada.common.utils.Consumer
import com.listentoprabhupada.common.utils.analytics.AnalyticsScreen
import com.listentoprabhupada.common.utils.analytics.tutorialComplete
import com.listentoprabhupada.common.utils.analytics.tutorialSettings
import com.listentoprabhupada.common.utils.asValue
import com.listentoprabhupada.common.utils.getStore
import com.listentoprabhupada.common.utils.init

private const val KEY_SETTINGS_STATE = "KEY_SETTINGS_STATE"

class SettingsComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val deps: SettingsDeps,
    private val output: Consumer<SettingsOutput>
) : SettingsComponent, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            SettingsStoreFactory(
                storeFactory = storeFactory,
                initialState = stateKeeper.consume(KEY_SETTINGS_STATE) ?: initialState,
            ).create()
        }

    private val initialState
        get() = SettingsState(
            getLocale(),
            deps.versionInfo
        )

    override val flow: Value<SettingsState> = store.asValue()

    init {
        store.init(instanceKeeper)
        stateKeeper.register(KEY_SETTINGS_STATE) { store.state }
    }

    override fun setLocale(value: String) = store.accept(Locale(value))
    override fun onShowTutorial() = deps.analytics.tutorialSettings()
    override fun onTutorialCompleted() {
        if (!isTutorialCompleted()) {
            deps.analytics.tutorialComplete(AnalyticsScreen.SETTINGS)
            setTutorialCompleted()
        }
    }
    override fun sendEmail() = output(SettingsOutput.Email)
    override fun shareApp() = output(SettingsOutput.ShareApp)
    override fun rateUs() = output(SettingsOutput.RateUs)
    override fun donations() = output(SettingsOutput.Donations)
    override fun back() = output(SettingsOutput.Back)
}
