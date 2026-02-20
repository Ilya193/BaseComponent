package ru.ikom.basecomponent.component

fun interface Reducer<State: Any, Message: Any> {
    fun State.reduce(msg: Message): State
}