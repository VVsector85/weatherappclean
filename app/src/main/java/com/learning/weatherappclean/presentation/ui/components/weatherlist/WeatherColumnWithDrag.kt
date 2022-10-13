package com.learning.weatherappclean.presentation.ui.components.weatherlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.presentation.MainViewModel
import com.learning.weatherappclean.presentation.ui.CardWeather
import com.learning.weatherappclean.presentation.ui.components.dragdrop.DragDropColumn


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
