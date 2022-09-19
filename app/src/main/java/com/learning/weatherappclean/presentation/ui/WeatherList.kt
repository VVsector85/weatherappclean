package com.learning.weatherappclean.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.presentation.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*



@Composable
fun WeatherList (modifier: Modifier, vm:MainViewModel, owner: LifecycleOwner){
   val weatherCardList = remember { mutableStateOf(emptyList<WeatherCard>()) }
   vm.getList().observe(owner){weatherCardList.value =it}
   val loadingState = remember { mutableStateOf(true) }
   vm.getLoadingState().observe(owner){loadingState.value =it}
   SwipeRefresh(
      state = SwipeRefreshState(loadingState.value),
      onRefresh = {vm.refreshCards()}
   ){

      LazyColumn(
         modifier = modifier,
         contentPadding = PaddingValues(bottom = 10.dp),
         reverseLayout = true,

         ) {
         itemsIndexed(weatherCardList.value) { index, item ->

//Text(item.location)
            CardWeather(
               modifier =
               Modifier
                  .fillMaxWidth(0.9f)

                  .height(165.dp)
                  .padding(horizontal = 6.dp, vertical = 12.dp),


            item,vm,index)/*{
               Row(){
                  Box(modifier = Modifier.fillMaxSize(0.7f)){ Text("${item.location}, ${item.temperature}")}
                  Box(){Button(onClick = { vm.deleteCard(index) }) {
                     Text("X")
                  }}





               }
            }*/
         }}




   }


}
