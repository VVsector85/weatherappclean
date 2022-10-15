package com.learning.weatherappclean.presentation.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import com.learning.weatherappclean.R
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.weatherappclean.domain.model.CardColorOption
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.util.getIcon
import com.learning.weatherappclean.presentation.ui.theme.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import java.util.*

@Composable
fun CardWeather(
    modifier: Modifier,
    content: WeatherCard,
    index: Int,
    settings: State<Settings>,
    deleteCard: (Index: Int) -> Unit,
    setShowDetails: (Boolean, Int) -> Unit,
    setShowSearch:(Boolean)->Unit,
    setExpanded:(Boolean)->Unit,
    weatherCardList: StateFlow<List<WeatherCard>>
) {
    val colour = when (content.cardColorOption) {
        CardColorOption.BLUE -> MaterialTheme.colors.cold
        CardColorOption.RED -> MaterialTheme.colors.hot
        CardColorOption.YELLOW -> MaterialTheme.colors.warm
        else -> Color.LightGray
    }

    val details = remember {
        mutableStateOf(content.showDetails)
    }
    /**I don't like at all that I have to pass weatherCardList to CardWeather composable
     * but I could not find the other way to update simple/detailed view of a weather card.
     * Without 'collectLatest' the lazyList remembers the position of expanded cards and
     * when I swap cards they change their order but 'showDetails' parameter remains applied to the
     * same card by its index. My explanation is a bit confusing, so its easier just to comment
     * LaunchedEffect block to see the behaviour I tried to describe*/
    LaunchedEffect(Unit) {
      weatherCardList.collectLatest {if (it.size > index) details.value = it[index].showDetails  }
    }
    Card(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (settings.value.detailsOnDoubleTap) {
                            details.value = !details.value
                            setShowDetails(details.value, index)
                        }
                    },
                    onTap = {
                        setShowSearch(false)
                        setExpanded(false)
                    },
                )
            },
        backgroundColor = colour,
    ) {
        Column() {
            Row() {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .align(Alignment.CenterVertically)
                )
                {
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        Icon(
                            painter = painterResource(
                                id = getIcon(
                                    content.weatherCode.toInt(),
                                    content.isNightIcon
                                )
                            ),
                            contentDescription = content.weatherDescription,
                            modifier = Modifier
                                .padding(20.dp)
                                .size(70.dp, 70.dp),
                            tint = MaterialTheme.colors.onCard
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                ) {
                    Text(
                        text = "${content.temperature}\u00b0${if (content.units == "f") "F" else ""}",
                        color = MaterialTheme.colors.onCard,
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .align(Alignment.TopStart),
                        fontSize = 60.sp,
                        fontWeight = FontWeight(600)
                    )

                    Column(modifier = Modifier.align(Alignment.BottomStart)) {
                        Text(
                            text = content.location.uppercase(Locale.ROOT),
                            color = MaterialTheme.colors.onCard,
                            modifier = Modifier
                                .padding(start = 20.dp, end = 10.dp)
                                .horizontalScroll(state = ScrollState(0)),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${content.country}${ if (details.value && settings.value.detailsOnDoubleTap) ", ${content.region}" else "" }",
                            color = MaterialTheme.colors.onCard,
                            modifier = Modifier
                                .padding(end = 10.dp,start = 20.dp,bottom = 10.dp)
                                .horizontalScroll(state = ScrollState(0)),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                    }
                    Button(
                        onClick = {
                            deleteCard(index)
                        },
                        shape = CircleShape,
                        contentPadding = PaddingValues(10.dp),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .size(35.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_closebutton_cross),
                            contentDescription = stringResource(id = R.string.deleteWeatherCard),
                            modifier = Modifier.size(12.dp, 12.dp)
                        )
                    }
                }
            }

            if (details.value && settings.value.detailsOnDoubleTap) Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.33f)
                        .padding(start = 20.dp)
                ) {
                    Column() {
                        WeatherDetails(
                            value = "${content.humidity}%",
                            iconId = R.drawable.ic_droplet,
                            description = R.string.humidity
                        )
                        WeatherDetails(
                            value = "${content.pressure}\nmbar",
                            iconId = R.drawable.ic_pressure,
                            description = R.string.pressure
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(start = 24.dp)
                ) {
                    Column() {
                        WeatherDetails(
                            value = content.uvIndex,
                            iconId = R.drawable.ic_sun_uv,
                            description = R.string.uvIndex
                        )
                        WeatherDetails(
                            value = "${content.windSpeed}\n${if (content.units == "f") "mil" else "km"}/h",
                            iconId = R.drawable.ic_wind,
                            description = R.string.windSpeed
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                ) {
                    Column() {
                        WeatherDetails(
                            value = "${content.feelsLike}\u00b0${if (content.units == "f") "F" else "C"}",
                            iconId = R.drawable.ic_temp_feels_like, description = R.string.feelsLike
                        )
                        WeatherDetails(
                            value = "${content.cloudCover}%",
                            iconId = R.drawable.ic_overcast, description = R.string.cloudCover
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherDetails(
    value: String,
    iconId: Int,
    description: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row() {
            Icon(
                painter = painterResource(
                    id = iconId
                ),
                contentDescription = stringResource(id = description),
                modifier = Modifier
                    .size(30.dp, 30.dp)
                    .align(Alignment.CenterVertically),
                tint = MaterialTheme.colors.onCard
            )
            Text(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.CenterVertically),
                text = value,
                color = MaterialTheme.colors.onCard,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}