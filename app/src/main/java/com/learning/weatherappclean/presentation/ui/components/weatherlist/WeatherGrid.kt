package com.learning.weatherappclean.presentation.ui.components.weatherlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.presentation.ui.components.CardWeather
import kotlinx.coroutines.launch

@Composable
fun WeatherGrid(
    weatherCardList: List<WeatherCard>,
    scrollToFirst: State<Pair<Boolean, Int>>,
    settings: State<Settings>,
    setShowDetails: ((Boolean, Int) -> Unit)? = null,
    stopScrollToFirst: (() -> Unit)? = null,
    deleteCard: ((Int) -> Unit)? = null,
    setExpanded: ((Boolean) -> Unit)? = null,
    setShowSearch: ((Boolean) -> Unit)? = null,
) {
    val scope = rememberCoroutineScope()
    val gridState = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(290.dp),
        modifier = Modifier.fillMaxWidth(0.9f),
        contentPadding = PaddingValues(bottom = 60.dp, top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 20.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        userScrollEnabled = true,
        state = gridState
    ) {
        if (scrollToFirst.value.first) {
            scope.launch {
                gridState.scrollToItem(scrollToFirst.value.second)
                stopScrollToFirst?.invoke()
            }
        }
        items(weatherCardList.size) { index ->
            CardWeather(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                content = weatherCardList[index],
                index = index,
                deleteCard = deleteCard,
                settings = settings,
                setShowDetails = setShowDetails,
                setExpanded = setExpanded,
                setShowSearch = setShowSearch

            )
        }
    }
}
