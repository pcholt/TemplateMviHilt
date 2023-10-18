package com.pcholt.templatemvihilt.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pcholt.templatemvihilt.main.viewmodel.Intent
import com.pcholt.templatemvihilt.main.viewmodel.MainViewModel
import com.pcholt.templatemvihilt.main.viewmodel.ViewState
import com.pcholt.templatemvihilt.ui.theme.ApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: MainViewModel = viewModel()
            val state: State<ViewState> = vm.stateFlow.collectAsState()
//            val command: State<Command> = vm.commandFlow.collectAsState()

            ApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (val value = state.value) {
                        is ViewState.Page1 ->
                            Greeting(value.text) {
                                vm.intend(Intent.ClickTheButton)
                            }

                        is ViewState.Page2 ->
                            Page2(value.text) {
                                vm.intend(Intent.ClickTheButton)
                            }

                        ViewState.Unknown ->
                            Page2("UNKNOWN") {}

                    }
                }
            }
        }
    }


}

@Composable
fun Page2(name: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(modifier = modifier) {
        Button(onClick = onClick) {
            Text(
                text = "Hello $name!"
            )
        }
        Text(
            text = "Hello $name!"
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(modifier = modifier) {
        Button(onClick = onClick) {
            Text(
                text = "Hello $name!"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ApplicationTheme {
        Greeting("Android") {

        }
    }
}