package com.pcholt.templatemvihilt.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.lifecycle.lifecycleScope
import com.pcholt.templatemvihilt.main.viewmodel.Command
import com.pcholt.templatemvihilt.main.viewmodel.Intent
import com.pcholt.templatemvihilt.main.viewmodel.MainViewModel
import com.pcholt.templatemvihilt.main.viewmodel.ViewState
import com.pcholt.templatemvihilt.ui.theme.ApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm by viewModels<MainViewModel>()
        vm.intend(Intent.StartPulse)

        lifecycleScope.launch {
            vm.commandFlow.onEach {
                when(it) {
                    is Command.ShowToast -> Toast.makeText(this@MainActivity, it.text, Toast.LENGTH_SHORT).show()
                }
            }.collect()
        }

        setContent {

            val state: State<ViewState> = vm.stateFlow.collectAsState()

            ApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (val value = state.value) {
                        is ViewState.Page1 ->
                            Page1(value.text) {
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
fun Page1(name: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
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
fun Preview1() {
    ApplicationTheme {
        Page1("Android") {

        }
    }
}