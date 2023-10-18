package com.pcholt.templatemvihilt.main.viewmodel

import com.pcholt.templatemvihilt.MviViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import javax.inject.Inject

@ViewModelScoped
class MainReducer @Inject constructor() :
    MviViewModel.Reducer<Intent, State, Command>() {

    override fun invoke(state: State, intent: Intent) =
        when (intent) {
            Intent.ClickTheButton -> state.copy(
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

            is Intent.DelayComplete -> state.copy(
                count = state.count + 1,
            )

            Intent.Pulse -> state.copy(
                pulse = state.pulse + 1
            )

            Intent.StartPulse -> state.also {
                launch {
                    while (true) {
                        delay(500)
                        asyncIntent(Intent.Pulse)
                    }
                }
            }
        }

}