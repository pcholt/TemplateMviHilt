package com.pcholt.templatemvihilt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class MviViewModel<I, S, VS, C>(
    private val reducer: Reducer<I, S, C>,
    private val mapper: Mapper<S, VS>,
    initialState: S,
) : ViewModel() {

    private val intentChannel = Channel<I>(1).also {
        reducer.asyncIntentChannel = it
        reducer.scope = viewModelScope
    }
    private val _commandFlow = MutableSharedFlow<C>().also {
        reducer.commandChannel = it
    }
    val commandFlow : SharedFlow<C> = _commandFlow

    fun intend(intent: I) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    val stateFlow: StateFlow<VS> = intentChannel
        .consumeAsFlow()
        .scan(initialState, reducer::invoke)
        .map(mapper::invoke)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            mapper.invoke(initialState)
        )


    interface Mapper<S, VS> {
        operator fun invoke(state: S): VS
    }

    abstract class Reducer<I, S, C> {
        internal lateinit var commandChannel: MutableSharedFlow<C>
        lateinit var asyncIntentChannel: Channel<I>
        lateinit var scope : CoroutineScope
        abstract fun invoke(state: S, intent: I): S
        suspend fun asyncIntent(intent: I) { asyncIntentChannel.send(intent) }
        suspend fun command(command: C) { commandChannel.emit(command) }

        fun launch(block: suspend CoroutineScope.() -> Unit) {
            scope.launch {
                block()
            }
        }
    }
}