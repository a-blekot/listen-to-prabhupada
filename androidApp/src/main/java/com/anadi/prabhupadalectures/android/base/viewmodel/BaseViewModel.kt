package com.anadi.prabhupadalectures.android.base.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anadi.prabhupadalectures.android.util.observeConnectivityAsFlow
import com.anadi.prabhupadalectures.utils.ConnectionState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface UiState

interface UiEvent

interface UiEffect

abstract class BaseViewModel<EVENT : UiEvent, STATE : UiState, EFFECT : UiEffect>(
    context: Context,
    private val mainDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
    private val eventsDebounceMs: Long = 0L
) : ViewModel() {
    private val initialState: STATE by lazy { setInitialState() }
    abstract fun setInitialState(): STATE

    private val _state: MutableStateFlow<STATE> = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state.asStateFlow()

    private val _event: MutableSharedFlow<EVENT> = MutableSharedFlow()

    private val _effect: Channel<EFFECT> = Channel(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        subscribeToEvents()
        observeConnection(context)
    }

    @Suppress("UNCHECKED_CAST")
    fun setEvent(event: UiEvent) {
        viewModelScope.launch(ioDispatcher) { _event.emit(event as EVENT) }
    }

    protected fun setState(reducer: STATE.() -> STATE) {
        viewModelScope.launch(mainDispatcher) {
            val newState = state.value.reducer()
            _state.value = newState
        }
    }

    private fun subscribeToEvents() {
        viewModelScope.launch(ioDispatcher) {
            _event
                .debounce(eventsDebounceMs)
                .collect {
                handleEvents(it)
            }
        }
    }

    private fun observeConnection(context: Context) =
        viewModelScope.launch {
            delay(1000L)
            context.observeConnectivityAsFlow()
                .collect {
                    onConnectivityStateChanged(it)
                }
        }

    protected open fun onConnectivityStateChanged(connectionState: ConnectionState) {}
    abstract fun handleEvents(event: EVENT)

    protected fun setEffect(builder: () -> EFFECT) {
        viewModelScope.launch(ioDispatcher) {
            val effectValue = builder()
            _effect.send(effectValue)
        }
    }
}
