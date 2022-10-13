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
import com.learning.weatherappclean.presentation.ui.components.dragdrop.DragDropColumn
import com.learning.weatherappclean.presentation.ui.components.weatherlist.WeatherColumnWithDrag
import com.learning.weatherappclean.presentation.ui.components.weatherlist.WeatherGrid
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
                refreshCards = vm::refreshCards,
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


