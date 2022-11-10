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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.learning.weatherappclean.domain.model.CardColorOption
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.presentation.ui.components.ErrorMessage
import com.learning.weatherappclean.presentation.ui.components.NoCards
import com.learning.weatherappclean.presentation.ui.theme.WeatherAppCleanTheme
import com.learning.weatherappclean.util.ErrorMessageProvider
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun WeatherList(
    isLandscape: Boolean,
    settings: State<Settings>,
    errorMessage: State<ErrorMessageProvider>,
    noRequests: State<Boolean>,
    padding: PaddingValues,
    isLoading: State<Boolean>,
    weatherCardList: List<WeatherCard>,
    scrollToFirst: State<Pair<Boolean, Int>>,
    refreshCards: (() -> Unit)? = null,
    setShowDetails: ((Boolean, Int) -> Unit)? = null,
    resetErrorMessage: (() -> Unit)? = null,
    setExpanded: ((Boolean) -> Unit)? = null,
    setShowSearch: ((Boolean) -> Unit)? = null,
    deleteCard: ((Int) -> Unit)? = null,
    swapSections: ((Int, Int) -> Unit)? = null,
    stopScrollToFirst: (() -> Unit)? = null,

) {

    SwipeRefresh(
        modifier = Modifier.padding(padding),
        state = rememberSwipeRefreshState(isRefreshing = isLoading.value),
        onRefresh = { refreshCards?.invoke() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ErrorMessage(errorMessageProvider = errorMessage, resetError = resetErrorMessage)
            if (weatherCardList.isEmpty() && !isLoading.value) NoCards(
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

           /* WeatherLaboriousColumn(
                weatherCardList = weatherCardList,
                scrollToFirst = scrollToFirst,
                settings = settings,
                setShowDetails = setShowDetails,
                stopScrollToFirst = stopScrollToFirst,
                deleteCard = deleteCard,
                setExpanded = setExpanded,
                setShowSearch = setShowSearch,
            )*/

            else {
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
}

@Preview(showBackground = true)
@Composable
fun WeatherListPreview() {
    WeatherAppCleanTheme {
        WeatherList(
            isLandscape = false,
            settings = MutableStateFlow(
                Settings(
                    imperialUnits = false,
                    newCardFirst = true,
                    detailsOnDoubleTap = true,
                    dragAndDropCards = true,
                    showVideo = false,
                    swipeToDismiss = false
                )
            ).collectAsState(),
            errorMessage = MutableStateFlow(ErrorMessageProvider()).collectAsState(),
            noRequests = MutableStateFlow(false).collectAsState(),
            padding = PaddingValues(),
            isLoading = MutableStateFlow(false).collectAsState(),
            scrollToFirst = MutableStateFlow(Pair(false, 0)).collectAsState(),
            weatherCardList =
            listOf(
                WeatherCard(
                    location = "Stockholm",
                    temperature = "0",
                    country = "Sweden",
                    region = "Stockholms Lan",
                    units = "m",
                    cloudCover = "50",
                    feelsLike = "-3",
                    humidity = "76",
                    pressure = "1014",
                    uvIndex = "2",
                    windSpeed = "11",
                    weatherCode = "113",
                    lat = "",
                    lon = "",
                    weatherDescription = "",
                    isNightIcon = true,
                    showDetails = false,
                    cardColorOption = CardColorOption.BLUE
                ),
                WeatherCard(
                    location = "Kharkiv",
                    temperature = "18",
                    country = "Ukraine",
                    region = "Kharkivska oblast'",
                    units = "m",
                    cloudCover = "40",
                    feelsLike = "15",
                    humidity = "70",
                    pressure = "1015",
                    uvIndex = "4",
                    windSpeed = "12",
                    weatherCode = "113",
                    lat = "",
                    lon = "",
                    weatherDescription = "",
                    isNightIcon = false,
                    showDetails = false,
                    cardColorOption = CardColorOption.YELLOW
                ),
                WeatherCard(
                    location = "Cairo",
                    temperature = "26",
                    country = "Egypt",
                    region = "Al Qahirah",
                    units = "m",
                    cloudCover = "75",
                    feelsLike = "25",
                    humidity = "42",
                    pressure = "1016",
                    uvIndex = "7",
                    windSpeed = "15",
                    weatherCode = "116",
                    lat = "",
                    lon = "",
                    weatherDescription = "",
                    isNightIcon = false,
                    showDetails = true,
                    cardColorOption = CardColorOption.RED
                ),
            )
        )
    }
}
