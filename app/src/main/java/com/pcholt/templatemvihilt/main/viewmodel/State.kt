package com.pcholt.templatemvihilt.main.viewmodel

data class State(
    val count: Int,
    val pageNumber: Int,
    val pulse: Int = 0,
) {
    companion object {
        val initial = State(1, 1, pulse = 0)
    }
}