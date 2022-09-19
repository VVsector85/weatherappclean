package com.learning.weatherappclean

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleOwner

import com.learning.weatherappclean.presentation.ui.theme.WeatherAppCleanTheme
import com.learning.weatherappclean.presentation.MainViewModel
import com.learning.weatherappclean.presentation.ui.WeatherList
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm:MainViewModel by viewModels()
    override fun onDestroy() {
        super.onDestroy()
        Log.d("my_tag", "activity destroyed")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("my_tag", "activity created")

        setContent {

            WeatherAppCleanTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen (vm,this)
                }
            }
        }
    }
}

@Composable
fun MainScreen(vm:MainViewModel, owner: LifecycleOwner) {

    val textLocation = remember { mutableStateOf("") }
    val textError = remember { mutableStateOf("") }

    vm.getError().observe(owner){textError.value = it

    }
R.drawable.ic_alster
   Column{
       TextField(value = textLocation.value ,  onValueChange = { value ->
           textLocation.value = value
           vm.getPredictions(textLocation.value)
       } ,
           Modifier
               .fillMaxWidth(0.8f)
               .align(Alignment.CenterHorizontally))
       Text(text = textError.value)
       Button(
           onClick = { vm.addCard(textLocation.value) }, Modifier
               .fillMaxWidth(0.8f)
               .align(Alignment.CenterHorizontally)
       ) {
           Text("Add card")
       }
WeatherList(modifier = Modifier.align(Alignment.CenterHorizontally), vm = vm, owner =owner )
     }
}






/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppCleanTheme {
        Greeting()
    }
}*/
