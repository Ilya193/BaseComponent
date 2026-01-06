package ru.ikom.basecomponent.component

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

abstract class BaseRootComponent<State : Any, Msg : Any, Label: Any>(initialState: State) : ViewModel() {

    private val components = mutableListOf<DisposableComponent>()

    protected val uiState = MutableStateFlow(initialState)

    val state = uiState.asStateFlow()

    private val _labels = Channel<Label>(capacity = Channel.BUFFERED)
    val labels = _labels.receiveAsFlow()

    protected fun initNestedComponents(vararg components: DisposableComponent) {
        this.components += components
    }

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

    override fun onCleared() {
        super.onCleared()
        components.forEach { it.dispose() }
    }
}