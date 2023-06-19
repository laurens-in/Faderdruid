package com.example.faderdruid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.faderdruid.ui.model.MidiViewModel
import com.example.faderdruid.ui.theme.FaderdruidTheme
import kotlin.math.roundToInt


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MidiViewModel by viewModels()
        viewModel.connect(viewModel.devices.find { d ->
            d.properties.getString("name", "").equals("Android USB Peripheral Port")
        } ?: viewModel.devices[0])
        setContent {
            FaderdruidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CCSlider(1, viewModel)
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

@Composable
fun CCSlider(ccNum: Int, vm: MidiViewModel) {
    var sliderValue by remember { mutableStateOf(0f) }
    Slider(value = sliderValue, valueRange=0f..127f, onValueChange = { value ->
        sliderValue = value
        vm.sendCC(ccNum, sliderValue.roundToInt())
    })
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FaderdruidTheme {
        Greeting("Android")
    }
}