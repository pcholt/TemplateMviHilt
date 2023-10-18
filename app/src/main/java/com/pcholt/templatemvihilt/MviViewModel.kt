package com.pcholt.templatemvihilt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class MviViewModel<I, S, VS>(
    private val reducer: Reducer<I, S>,
    private val mapper: Mapper<S, VS>,
    initialState: S,
) : ViewModel() {

    private val intentChannel = Channel<I>(1).also {
        reducer.asyncIntentChannel = it
        reducer.scope = viewModelScope
    }

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
            CoroutineScope(Dispatchers.Default),
            SharingStarted.Eagerly,
            mapper.invoke(initialState)
        )

    interface Mapper<S, VS> {
        operator fun invoke(state: S): VS
    }

    abstract class Reducer<I, S> {
        internal lateinit var asyncIntentChannel: Channel<I>
        lateinit var scope : CoroutineScope
        abstract fun invoke(state: S, intent: I): S
        fun asyncIntent(intent: I) {
            scope.launch {
                asyncIntentChannel.send(intent)
            }
        }
    }
}