package com.learning.weatherappclean

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.learning.weatherappclean.presentation.MainViewModel
import com.learning.weatherappclean.presentation.ui.components.MainScreen
import com.learning.weatherappclean.presentation.ui.theme.WeatherAppCleanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        vm.refreshCardsOnResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppCleanTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(
                    color = MaterialTheme.colors.background
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(vm = vm)
                }
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppCleanTheme {
       MainScreen()
    }
}
*/
