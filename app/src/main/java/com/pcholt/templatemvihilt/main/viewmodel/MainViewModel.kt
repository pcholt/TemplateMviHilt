package com.pcholt.templatemvihilt.main.viewmodel

import com.pcholt.templatemvihilt.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(reducer: MainReducer, mapper: MainMapper) :
    MviViewModel<Intent, State, ViewState>(reducer, mapper, State.initial)

@ViewModelScoped
class MainReducer @Inject constructor() :
    MviViewModel.Reducer<Intent, State>() {

    override fun invoke(state: State, intent: Intent) =
        when (intent) {
            Intent.ClickTheButton -> {
                State(
                    count = state.count + 1,
                    pageNumber = when (state.pageNumber) {
                        1 -> {
                            scope.launch {
                                delay(1000)
                                asyncIntent(Intent.DelayComplete)
                            }
                            2
                        }

                        2 -> 1
                        else -> 1
                    }
                )
            }

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
            1 -> ViewState.Page1.DisplayText("STATE A ${state.count}")
            2 -> ViewState.Page2.DisplayText("STATE B ${state.count}")
            else -> ViewState.Unknown
        }
}

sealed interface Intent {
    data object DelayComplete : Intent
    data object ClickTheButton : Intent
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

    sealed interface Page1 : ViewState {
        data class DisplayText(
            val text: String
        ) : Page1
    }

    sealed interface Page2 : ViewState {
        data class DisplayText(
            val text: String
        ) : Page2
    }
}