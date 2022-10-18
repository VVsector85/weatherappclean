package com.learning.weatherappclean.presentation.ui.components.weatherlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.presentation.ui.components.ErrorMessage
import com.learning.weatherappclean.presentation.ui.components.NoCards
import com.learning.weatherappclean.util.ErrorMessageProvider
import kotlinx.coroutines.flow.StateFlow

@Composable
fun WeatherList(
    padding: PaddingValues,
    refreshCards: () -> Unit,
    setShowDetails: (Boolean, Int) -> Unit,
    resetErrorMessage: () -> Unit,
    setExpanded: (Boolean) -> Unit,
    setShowSearch: (Boolean) -> Unit,
    weatherCardList: StateFlow<List<WeatherCard>>,
    isLoading: State<Boolean>,
    scrollToFirst: State<Pair<Boolean, Int>>,
    stopScrollToFirst: () -> Unit,
    deleteCard: (Int) -> Unit,
    swapSections: (Int, Int) -> Unit,
    isLandscape: Boolean,
    settings: State<Settings>,
    errorMsg: State<ErrorMessageProvider>,
    noRequests: State<Boolean>
) {
    SwipeRefresh(
        modifier = Modifier.padding(padding),
        state = rememberSwipeRefreshState(isRefreshing = isLoading.value),
        onRefresh = { refreshCards() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ErrorMessage(errorMessageProvider = errorMsg, resetError = resetErrorMessage)
            if (weatherCardList.collectAsState().value.isEmpty() && !isLoading.value) NoCards(
                refreshCards = refreshCards,
                noRequests = noRequests
            )
            if (isLandscape || !settings.value.dragAndDropCards)
                WeatherGrid(
                    weatherCardList = weatherCardList,
                    scrollToFirst = scrollToFirst,
                    settings = settings,
                    setShowDetails = setShowDetails,
                    stopScrollToFirst = stopScrollToFirst,
                    deleteCard = deleteCard,
                    setExpanded = setExpanded,
                    setShowSearch = setShowSearch,
                )
            else
                WeatherColumnWithDrag(
                    weatherCardList = weatherCardList,
                    deleteCard = deleteCard,
                    setExpanded = setExpanded,
                    setShowSearch = setShowSearch,
                    stopScrollToFirst = stopScrollToFirst,
                    swapSections = swapSections,
                    scrollToFirst = scrollToFirst,
                    settings = settings,
                    setShowDetails = setShowDetails,
                )
        }
    }
}
