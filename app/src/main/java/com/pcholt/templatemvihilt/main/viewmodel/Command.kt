package com.pcholt.templatemvihilt.main.viewmodel

sealed interface Command {
    data class ShowToast(val text: String) : Command
}