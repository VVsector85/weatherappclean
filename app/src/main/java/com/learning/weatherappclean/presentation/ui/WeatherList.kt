package com.learning.weatherappclean.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.learning.weatherappclean.util.ErrorMessage
import com.learning.weatherappclean.domain.model.Settings
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
    isLoading: State<Boolean>,
    scrollToFirst: State<Pair<Boolean, Int>>,
    isLandscape: Boolean,
    settings: State<Settings>,
    errorMsg: State<ErrorMessage>,
    noRequests: State<Boolean>
) {
    SwipeRefresh(
        modifier = Modifier.padding(padding),
        state = rememberSwipeRefreshState(isRefreshing = isLoading.value),
        onRefresh = { vm.refreshCards() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ErrorMessage(errorMsg = errorMsg, resetError = vm::resetErrorMessage)
            if (weatherCardList.value.isEmpty() && !isLoading.value) NoCards(
                vm = vm,
                noRequests = noRequests
            )
            if (isLandscape || !settings.value.dragAndDropCards)
                WeatherGrid(
                    vm = vm,
                    weatherCardList = weatherCardList,
                    scrollToFirst = scrollToFirst,
                    settings = settings,
                    setShowDetails = vm::setShowDetails
                )
            else
                WeatherColumnWithDrag(
                    weatherCardList = weatherCardList,
                    vm = vm,
                    scrollToFirst = scrollToFirst,
                    settings = settings,
                    setShowDetails = vm::setShowDetails
                )
        }
    }
}

@Composable
fun WeatherGrid(
    vm: MainViewModel,
    weatherCardList: State<List<WeatherCard>>,
    scrollToFirst: State<Pair<Boolean, Int>>,
    settings: State<Settings>,
    setShowDetails: (Boolean, Int) -> Unit

) {
    val gridState = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(290.dp),
        modifier = Modifier.fillMaxWidth(0.9f),
        contentPadding = PaddingValues(bottom = 40.dp, top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 20.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        userScrollEnabled = true,
        state = gridState
    ) {
        if (scrollToFirst.value.first) CoroutineScope(Dispatchers.Main).launch {
            gridState.scrollToItem(scrollToFirst.value.second)
            vm.stopScrollToFirst()
        }
        items(weatherCardList.value.size) { index ->
            CardWeather(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                content = weatherCardList.value[index],
                index = index,
                delete = vm::deleteCard,
                settings = settings,
                setShowDetails = setShowDetails,
                vm = vm
            )
        }
    }
}


@Composable
fun WeatherColumnWithDrag(
    weatherCardList: State<List<WeatherCard>>,
    vm: MainViewModel,
    scrollToFirst: State<Pair<Boolean, Int>>,
    settings: State<Settings>,
    setShowDetails: (Boolean, Int) -> Unit
) {
    DragDropColumn(
        items = weatherCardList.value,
        onSwap = vm::swapSections,
        scrollToFirst = scrollToFirst,
        stopScrollToFirst = vm::stopScrollToFirst,
        contentPadding = PaddingValues(bottom = 60.dp, top = 10.dp),
    ) { index, item ->
        CardWeather(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            content = item,
            index = index,
            delete = vm::deleteCard,
            settings = settings,
            setShowDetails = setShowDetails,
            vm = vm
        )
    }
}
