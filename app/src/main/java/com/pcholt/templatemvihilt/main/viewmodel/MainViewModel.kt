package com.pcholt.templatemvihilt.main.viewmodel

import com.pcholt.templatemvihilt.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(reducer: MainReducer, mapper: MainMapper) :
    MviViewModel<Intent, State, ViewState, Command>(reducer, mapper, State.initial)

