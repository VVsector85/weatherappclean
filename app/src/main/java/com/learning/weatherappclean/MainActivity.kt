package com.learning.weatherappclean

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.learning.weatherappclean.domain.model.WeatherCard

import com.learning.weatherappclean.presentation.ui.theme.WeatherAppCleanTheme
import com.learning.weatherappclean.presentation.MainViewModel
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
                    Greeting (vm,this)
                }
            }
        }
    }
}

@Composable
fun Greeting(vm:MainViewModel,owner: LifecycleOwner) {
    val loadingState = remember { mutableStateOf(true) }
    val textLocation = remember { mutableStateOf("") }
    val weatherCardList = remember { mutableStateOf(emptyList<WeatherCard>()) }
    vm.getList().observe(owner){weatherCardList.value =it}
    vm.getLoadingState().observe(owner){loadingState.value =it}

   Column{
       TextField(value = textLocation.value ,  onValueChange = { value ->
           textLocation.value = value
           vm.getPredictions(textLocation.value)
       } ,
           Modifier
               .fillMaxWidth(0.8f)
               .align(Alignment.CenterHorizontally))

       Button(
           onClick = { vm.addCard(textLocation.value) }, Modifier
               .fillMaxWidth(0.8f)
               .align(Alignment.CenterHorizontally)
       ) {
           Text("Add card")
       }

       SwipeRefresh(
           state = SwipeRefreshState(loadingState.value),
           onRefresh = {vm.refreshCards()}
       ){

       LazyColumn(
           modifier = Modifier.align(Alignment.CenterHorizontally),
           contentPadding = PaddingValues(bottom = 10.dp),
           reverseLayout = true,

           ) {
           itemsIndexed(weatherCardList.value) { index, item ->


               Card(
                   modifier = Modifier
                       .fillMaxWidth(0.9f)
                       .height(80.dp)
                       .padding(horizontal = 6.dp, vertical = 12.dp),
                   //elevation = 4.dp,
                   backgroundColor = Color.LightGray

               ){
                   Row(){
                       Box(modifier = Modifier.fillMaxSize(0.7f)){ Text("${item.location}, ${item.temperature}")}
                       Box(){Button(onClick = { vm.deleteCard(index) }) {
                           Text("X")
                       }}





                   }
               }
           }}




   }}
}

/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppCleanTheme {
        Greeting()
    }
}*/
