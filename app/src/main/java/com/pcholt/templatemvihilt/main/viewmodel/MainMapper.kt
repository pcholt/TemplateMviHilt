package com.pcholt.templatemvihilt.main.viewmodel

import com.pcholt.templatemvihilt.MviViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class MainMapper @Inject constructor() : MviViewModel.Mapper<State, ViewState> {
    override fun invoke(state: State) =
        when (state.pageNumber) {
            1 -> ViewState.Page1("STATE A ${state.count} ${state.pulse}")
            2 -> ViewState.Page2("STATE B ${state.count} ${state.pulse}")
            else -> ViewState.Unknown
        }
}