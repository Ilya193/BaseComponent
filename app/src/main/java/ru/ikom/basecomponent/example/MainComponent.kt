package ru.ikom.basecomponent.example

import ru.ikom.basecomponent.component.BaseRootComponent

class MainComponent : BaseRootComponent<MainComponent.State,
        MainComponent.Msg, MainComponent.Label>(initialState()) {

    val nestedComponent = NestedComponent()

    init {
        internalInitNestedComponents()

        dispatch(Msg.UpdateLoading(true))
    }

    private fun internalInitNestedComponents() {
        initNestedComponents(nestedComponent)
    }

    override fun State.reduce(msg: Msg): State =
        when (msg) {
            is Msg.UpdateLoading -> copy(isLoading = msg.isLoading)
        }

    data class State(
        val isLoading: Boolean
    )

    sealed interface Msg {
        class UpdateLoading(val isLoading: Boolean) : Msg
    }

    sealed interface Label {
        class ShowSnackbar(val message: String) : Label
    }
}

private fun initialState() =
    MainComponent.State(
        isLoading = false
    )