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
import com.getar.app.data.repository.GempaRepository
import com.getar.app.ui.theme.GetarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GetarTheme {
                // TEMPORARY - hapus di M8
                LaunchedEffect(Unit) {
                    try {
                        val repository = GempaRepository()
                        val data = repository.getGempaTerkini()
                        Log.d("GetarTest", "Jumlah data: ${data.size}")
                        if (data.isNotEmpty()) {
                            Log.d("GetarTest", "Data pertama: ${data[0]}")
                        }
                    } catch (e: Exception) {
                        Log.e("GetarTest", "Error fetching gempa: ${e.message}", e)
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