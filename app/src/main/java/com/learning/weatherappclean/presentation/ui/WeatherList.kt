package com.learning.weatherappclean.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.presentation.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun WeatherList(padding:PaddingValues, vm: MainViewModel, owner: LifecycleOwner) {
    val weatherCardList = remember { mutableStateOf(emptyList<WeatherCard>()) }
    vm.getList().observe(owner) { weatherCardList.value = it }
    val loadingState = rememberSwipeRefreshState(isRefreshing = false)
    vm.getLoadingState().observe(owner) { loadingState.isRefreshing = it }
    val listState = rememberLazyListState()



    SwipeRefresh(
        state = loadingState,
        onRefresh = { vm.refreshCards() }

    ) {
        vm.getScrollToFirst().observe(owner) { CoroutineScope(Dispatchers.Main).launch {listState.scrollToItem(weatherCardList.value.size)} }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 50.dp),
            reverseLayout = true,
            horizontalAlignment = Alignment.CenterHorizontally,
            userScrollEnabled = true,
            state = listState

            ) {

            itemsIndexed(weatherCardList.value) { index, item ->

                CardWeather(content = item,vm = vm,index = index)

            }
        }


    }


}
