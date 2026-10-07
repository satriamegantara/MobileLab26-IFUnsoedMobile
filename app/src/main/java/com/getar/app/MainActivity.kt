package com.getar.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.getar.app.ui.home.HomeViewModel
import com.getar.app.ui.state.UiState
import com.getar.app.ui.theme.GetarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GetarTheme {
                // TEMPORARY - hapus di M8
                val homeViewModel: HomeViewModel = viewModel()
                LaunchedEffect(homeViewModel) {
                    homeViewModel.uiState.collect { state ->
                        when (state) {
                            is UiState.Loading -> Log.d("GetarTest", "State: Loading")
                            is UiState.Success -> Log.d("GetarTest", "State: Success with ${state.data.size} items")
                            is UiState.Error -> Log.e("GetarTest", "State: Error -> ${state.message}")
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GetarTheme {
        Greeting("Android")
    }
}