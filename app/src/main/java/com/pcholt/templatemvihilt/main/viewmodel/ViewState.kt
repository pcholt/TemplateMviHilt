package com.pcholt.templatemvihilt.main.viewmodel

sealed interface ViewState {
    data object Unknown : ViewState

    data class Page1(
        val text: String
    ) : ViewState

    data class Page2(
        val text: String
    ) : ViewState
}