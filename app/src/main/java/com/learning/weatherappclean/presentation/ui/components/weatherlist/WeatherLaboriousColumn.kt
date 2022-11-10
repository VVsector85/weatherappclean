package com.learning.weatherappclean.presentation.ui.components.weatherlist

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.presentation.ui.components.CardWeather
import kotlin.math.roundToInt

@Composable
fun WeatherLaboriousColumn(
    weatherCardList: List<WeatherCard>,
    scrollToFirst: State<Pair<Boolean, Int>>,
    settings: State<Settings>,
    setShowDetails: ((Boolean, Int) -> Unit)? = null,
    stopScrollToFirst: (() -> Unit)? = null,
    deleteCard: ((Int) -> Unit)? = null,
    setExpanded: ((Boolean) -> Unit)? = null,
    setShowSearch: ((Boolean) -> Unit)? = null,
) {
    val state = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(state),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LaunchedEffect(scrollToFirst.value.first) {
            if (scrollToFirst.value.first) {
                state.scrollTo(scrollToFirst.value.second)
                stopScrollToFirst?.invoke()
            }
        }

        weatherCardList.forEachIndexed() { index, value ->
            val offsetY = remember { mutableStateOf(0f) }
            CardWeather(
                modifier = Modifier.offset { IntOffset(0, offsetY.value.roundToInt()) }
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->
                            offsetY.value += delta
                        }
                    )
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 20.dp),

                content = value,
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
