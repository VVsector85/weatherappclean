package com.learning.weatherappclean.presentation.ui.components.weatherlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.presentation.ui.components.CardWeather
import com.learning.weatherappclean.presentation.ui.components.dragdrop.DragDropColumn
import kotlinx.coroutines.flow.StateFlow

@Composable
fun WeatherColumnWithDrag(
    weatherCardList: StateFlow<List<WeatherCard>>,
    settings: State<Settings>,
    scrollToFirst: State<Pair<Boolean, Int>>,
    swapSections: ((Int, Int) -> Unit)? = null,
    stopScrollToFirst: (() -> Unit)? = null,
    deleteCard: ((Int) -> Unit)? = null,
    setExpanded: ((Boolean) -> Unit)? = null,
    setShowSearch: ((Boolean) -> Unit)? = null,
    setShowDetails: ((Boolean, Int) -> Unit)? = null,
) {
    DragDropColumn(
        items = weatherCardList.collectAsState().value,
        onSwap = swapSections,
        scrollToFirst = scrollToFirst,
        stopScrollToFirst = stopScrollToFirst,
        contentPadding = PaddingValues(bottom = 60.dp, top = 10.dp),
    ) { index, item ->
        CardWeather(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            content = item,
            index = index,
            deleteCard = deleteCard,
            settings = settings,
            setShowDetails = setShowDetails,
            setShowSearch = setShowSearch,
            setExpanded = setExpanded,
            weatherCardList = weatherCardList
        )
    }
}
