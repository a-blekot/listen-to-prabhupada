package com.listentoprabhupada.common.utils

import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.rx.Disposable
import io.github.aakira.napier.Napier

fun <T : Any> Store<*, T, *>.asValue(): Value<T> =
    object : Value<T>() {
        override val value: T get() = state
        private var disposables = emptyMap<(T) -> Unit, Disposable>()

        override fun subscribe(observer: (T) -> Unit) {
            val disposable = states(com.arkivanov.mvikotlin.rx.observer(onNext = observer))
            this.disposables += observer to disposable
        }

        override fun unsubscribe(observer: (T) -> Unit) {
            val disposable = disposables[observer] ?: return
            this.disposables -= observer
            disposable.dispose()
        }
    }

fun Store<*, *, *>.init(instanceKeeper: InstanceKeeper) {
    Napier.d("init(instanceKeeper)", tag = "PlayerStore")
    if (instanceKeeper.get(key = this) == null) {
        instanceKeeper.put(key = this, InitInstance)
        init()
    }
}

private object InitInstance : InstanceKeeper.Instance {
    override fun onDestroy() {
        // no-op
    }
}
