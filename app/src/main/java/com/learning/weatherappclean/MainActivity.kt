package com.learning.weatherappclean


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.learning.weatherappclean.presentation.MainViewModel
import com.learning.weatherappclean.presentation.ui.*
import com.learning.weatherappclean.presentation.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()


    override fun onResume() {
          super.onResume()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherAppCleanTheme {

                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(
                    color = MaterialTheme.colors.background
                )
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    MainScreen(vm=vm)
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
