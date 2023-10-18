package com.pcholt.templatemvihilt.main.viewmodel

import com.pcholt.templatemvihilt.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(reducer: MainReducer, mapper: MainMapper) :
    MviViewModel<Intent, State, ViewState, Command>(reducer, mapper, State.initial)

@ViewModelScoped
class MainReducer @Inject constructor() :
    MviViewModel.Reducer<Intent, State, Command>() {

    override fun invoke(state: State, intent: Intent) =
        when (intent) {
            Intent.ClickTheButton -> State(
                count = state.count + 1,
                pageNumber = when (state.pageNumber) {
                    1 -> {
                        launch {
                            command(Command.ShowToast("A toast"))
                            delay(3000)
                            asyncIntent(Intent.DelayComplete)
                        }
                        2
                    }

                    2 -> 1
                    else -> 1
                }
            )

            is Intent.DelayComplete -> State(
                count = state.count + 1,
                pageNumber = 1
            )
        }

}

@ViewModelScoped
class MainMapper @Inject constructor() : MviViewModel.Mapper<State, ViewState> {
    override fun invoke(state: State) =
        when (state.pageNumber) {
            1 -> ViewState.Page1("STATE A ${state.count}")
            2 -> ViewState.Page2("STATE B ${state.count}")
            else -> ViewState.Unknown
        }
}

sealed interface Intent {
    data object DelayComplete : Intent
    data object ClickTheButton : Intent
}

sealed interface Command {
    data class ShowToast(val text: String) : Command
}

data class State(
    val count: Int,
    val pageNumber: Int,
) {
    companion object {
        val initial = State(1, 1)
    }
}

sealed interface ViewState {
    data object Unknown : ViewState

    data class Page1(
        val text: String
    ) : ViewState

    data class Page2(
        val text: String
    ) : ViewState
}