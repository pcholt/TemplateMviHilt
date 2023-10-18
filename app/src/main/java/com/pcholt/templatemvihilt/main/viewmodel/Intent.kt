package com.pcholt.templatemvihilt.main.viewmodel

sealed interface Intent {
    data object DelayComplete : Intent
    data object ClickTheButton : Intent
    data object Pulse : Intent
    data object StartPulse : Intent
}