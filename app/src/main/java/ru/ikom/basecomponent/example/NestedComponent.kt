package ru.ikom.basecomponent.example

import ru.ikom.basecomponent.component.BaseComponent
import ru.ikom.basecomponent.component.Reducer

class NestedComponentReducer : Reducer<NestedComponent.State, NestedComponent.Msg> {
    override fun NestedComponent.State.reduce(msg: NestedComponent.Msg): NestedComponent.State =
        when (msg) {
            is NestedComponent.Msg.UpdateTime -> copy(time = msg.time)
        }
}

class NestedComponent(
    reducer: Reducer<State, Msg> = NestedComponentReducer()
) : BaseComponent<NestedComponent.State, NestedComponent.Msg,
        NestedComponent.Label>(initialState(), reducer) {

    init {
        dispatch(Msg.UpdateTime("10:00"))
    }

    data class State(
        val time: String
    )

    sealed interface Msg {
        class UpdateTime(val time: String) : Msg
    }

    sealed interface Label
}

private fun initialState() =
    NestedComponent.State(
        time = ""
    )