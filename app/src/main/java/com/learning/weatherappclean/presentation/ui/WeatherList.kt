package com.learning.weatherappclean.presentation.ui

import android.content.ClipData
import android.content.res.Configuration
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
fun WeatherList(
    padding: PaddingValues,
    vm: MainViewModel,
    weatherCardList: State<List<WeatherCard>>,
    loadingState: State<Boolean>,
    scrollToFirst: State<Boolean>
) {
    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()
    SwipeRefresh(
        modifier = Modifier.padding(top = 10.dp),
        state = rememberSwipeRefreshState(isRefreshing = loadingState.value),
        onRefresh = { vm.refreshCards() }
    ) {
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(260.dp),

                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 50.dp),

                //horizontalAlignment =  Alignment.CenterHorizontally,
                // verticalAlignment =  Alignment.CenterVertically,
                userScrollEnabled = true,
                state = gridState
            ) {
                if (scrollToFirst.value) CoroutineScope(Dispatchers.Main).launch {
                    gridState.scrollToItem(0)
                }

                items(weatherCardList.value.size) { index ->

                    Row(modifier = Modifier.fillMaxWidth()) {


                        CardWeather(
                            modifier = Modifier

                                .fillMaxWidth(1f)
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                                .height(130.dp),

                            content = weatherCardList.value.asReversed()[index],
                            vm = vm,
                            index = weatherCardList.value.size - index - 1
                        )


                    }
                }
            }


        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 50.dp),
                reverseLayout = true,
                horizontalAlignment = Alignment.CenterHorizontally,
                userScrollEnabled = true,
                state = listState

            ) {
                if (scrollToFirst.value) CoroutineScope(Dispatchers.Main).launch {
                    listState.scrollToItem(weatherCardList.value.size)
                }
                itemsIndexed(weatherCardList.value) { index, item ->
                    CardWeather(
                        modifier = Modifier
                            .padding(horizontal = 0.dp, vertical = 10.dp)
                            .fillMaxWidth(0.9f)
                            .height(130.dp),
                        content = item, vm = vm, index = index
                    )
                }
            }
        }
    }
}
