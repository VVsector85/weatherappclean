package com.learning.weatherappclean

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner

import com.learning.weatherappclean.presentation.ui.theme.WeatherAppCleanTheme
import com.learning.weatherappclean.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm:MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("tag", "activity created")

        setContent {

            WeatherAppCleanTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting (vm,this)
                }
            }
        }
    }
}

@Composable
fun Greeting(vm:MainViewModel,owner: LifecycleOwner) {


    val textGet = remember { mutableStateOf("No data") }
    val textPut = remember { mutableStateOf("Put data here") }

    vm.getResult().observe(owner){textGet.value =it  }

   Column{

       Text(textGet.value,
           Modifier
               .fillMaxWidth(0.8f)
               .padding(10.dp))

       Button(onClick = { /*TODO*/

vm.load()
                        },
           Modifier
               .fillMaxWidth(0.8f)
               .align(Alignment.CenterHorizontally)) {
Text("Get data")
       }
       TextField(value = textPut.value ,  onValueChange = { value ->
           textPut.value = value} ,
           Modifier
               .fillMaxWidth(0.8f)
               .align(Alignment.CenterHorizontally))
       Button(onClick = { /*TODO*/

           vm.save(textPut.value)
                        },
           Modifier
               .fillMaxWidth(0.8f)
               .align(Alignment.CenterHorizontally)){
           Text("Put data")
       }
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
