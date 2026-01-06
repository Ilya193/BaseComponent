package ru.ikom.basecomponent.example

import ru.ikom.basecomponent.component.BaseComponent

class NestedComponent : BaseComponent<NestedComponent.State,
        NestedComponent.Msg, NestedComponent.Label>(initialState()) {

    init {
        dispatch(Msg.UpdateTime("10:00"))
    }

    override fun State.reduce(msg: Msg): State =
        when (msg) {
            is Msg.UpdateTime -> copy(time = msg.time)
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