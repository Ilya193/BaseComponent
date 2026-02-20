package ru.ikom.basecomponent.example

import ru.ikom.basecomponent.component.BaseRootComponent
import ru.ikom.basecomponent.component.Reducer

class MainComponentReducer : Reducer<MainComponent.State, MainComponent.Msg> {
    override fun MainComponent.State.reduce(msg: MainComponent.Msg): MainComponent.State =
        when (msg) {
            is MainComponent.Msg.UpdateLoading -> copy(isLoading = msg.isLoading)
        }
}

class MainComponent(
    reducer: Reducer<State, Msg> = MainComponentReducer(),
) : BaseRootComponent<MainComponent.State, MainComponent.Msg,
        MainComponent.Label>(initialState(), reducer) {

    val nestedComponent = NestedComponent() // DI

    init {
        internalInitNestedComponents()

        dispatch(Msg.UpdateLoading(true))
    }

    private fun internalInitNestedComponents() {
        initNestedComponents(nestedComponent)
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