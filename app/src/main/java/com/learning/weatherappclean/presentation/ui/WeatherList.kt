package com.learning.weatherappclean.presentation.ui

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*


import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.presentation.MainViewModel
import com.learning.weatherappclean.presentation.ui.dragdrop.DragDropColumn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



@Composable


fun WeatherList(
    padding: PaddingValues,
    vm: MainViewModel,
    weatherCardList: State<List<WeatherCard>>,
    loadingState: State<Boolean>,
    scrollToFirst: State<Boolean>,
    isLandscape: Boolean
) {


    SwipeRefresh(
        modifier = Modifier.padding(top = 0.dp),
        state = rememberSwipeRefreshState(isRefreshing = loadingState.value),
        onRefresh = { vm.refreshCards() }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding).padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isLandscape)
                WeatherGrid(
                    vm = vm,
                    weatherCardList = weatherCardList,
                    scrollToFirst = scrollToFirst
                )
            else
                WeatherColumnWithDrag(
                    weatherCardList = weatherCardList,
                    vm = vm,
                    scrollToFirst = scrollToFirst
                )
        }
    }
}

@Composable
fun WeatherGrid(
    vm: MainViewModel,
    weatherCardList: State<List<WeatherCard>>,
    scrollToFirst: State<Boolean>

) {

    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(290.dp),

        modifier = Modifier.fillMaxWidth(0.9f),
        contentPadding = PaddingValues(bottom = 40.dp, top = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 20.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        userScrollEnabled = true,
        state = gridState
    ) {
        if (scrollToFirst.value) CoroutineScope(Dispatchers.Main).launch {
            gridState.scrollToItem(0)
        }

        items(weatherCardList.value.size) { index ->


            CardWeather(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(horizontal = 0.dp, vertical = 0.dp)
                    .height(130.dp),
                content = weatherCardList.value.reversed()[index],
                index = weatherCardList.value.size - index - 1,
                delete = vm::deleteCard
            )
        }
    }
}


@Composable
fun WeatherColumnWithDrag(
    weatherCardList: State<List<WeatherCard>>,
    vm: MainViewModel,
    scrollToFirst: State<Boolean>
) {
    DragDropColumn(
        items = weatherCardList.value.reversed(),
        onSwap = vm::swapSections,
        scrollToFirst = scrollToFirst
    ) { index, item ->
        CardWeather(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(horizontal = 0.dp, vertical = 0.dp)
                .height(130.dp),
            content = item,
            index =  weatherCardList.value.size - index - 1,
            delete = vm::deleteCard
        )
    }


}
