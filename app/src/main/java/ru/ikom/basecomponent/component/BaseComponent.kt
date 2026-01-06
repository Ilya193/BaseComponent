package ru.ikom.basecomponent.component

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

abstract class BaseComponent<State : Any, Msg : Any, Label: Any>(
    initialState: State,
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : DisposableComponent {

    protected val uiState = MutableStateFlow(initialState)

    protected val coroutineScope = CoroutineScope(dispatcher)

    val states = uiState.asStateFlow()

    private val _labels = Channel<Label>(capacity = Channel.BUFFERED)
    val labels = _labels.receiveAsFlow()

    protected fun dispatch(msg: Msg) {
        uiState.update { it.reduce(msg) }
    }

    protected inline fun dispatch(block: State.() -> State) {
        uiState.update { block(it) }
    }

    protected fun publish(label: Label) {
        _labels.trySend(label)
    }

    protected abstract fun State.reduce(msg: Msg): State

    @CallSuper
    override fun dispose() {
        coroutineScope.cancel()
    }
}