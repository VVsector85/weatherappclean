package com.learning.weatherappclean.presentation.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.learning.weatherappclean.R
import com.learning.weatherappclean.domain.model.CardColorOption
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.presentation.ui.theme.WeatherAppCleanTheme
import com.learning.weatherappclean.presentation.ui.theme.cold
import com.learning.weatherappclean.presentation.ui.theme.hot
import com.learning.weatherappclean.presentation.ui.theme.onCard
import com.learning.weatherappclean.presentation.ui.theme.warm
import com.learning.weatherappclean.util.getWeatherIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CardWeather(
    modifier: Modifier,
    content: WeatherCard,
    index: Int,
    settings: State<Settings>,
    deleteCard: ((Index: Int) -> Unit)? = null,
    setShowDetails: ((Boolean, Int) -> Unit)? = null,
    setShowSearch: ((Boolean) -> Unit?)? = null,
    setExpanded: ((Boolean) -> Unit)? = null,

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
    details.value = content.showDetails

    Card(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (settings.value.detailsOnDoubleTap) {
                            details.value = !details.value
                            setShowDetails?.invoke(details.value, index)
                        }
                    },
                    onTap = {
                        setShowSearch?.invoke(false)
                        setExpanded?.invoke(false)
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
                ) {
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        Icon(
                            painter = painterResource(
                                id = getWeatherIcon(
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
                        style = MaterialTheme.typography.h2
                    )

                    Column(modifier = Modifier.align(Alignment.BottomStart)) {
                        Text(
                            text = content.location.uppercase(),
                            color = MaterialTheme.colors.onCard,
                            modifier = Modifier
                                .padding(start = 20.dp, end = 10.dp)
                                .horizontalScroll(state = ScrollState(0)),
                            style = MaterialTheme.typography.h5
                        )
                        Text(
                            text = "${content.country}${if (details.value && settings.value.detailsOnDoubleTap) ", ${content.region}" else ""}",
                            color = MaterialTheme.colors.onCard,
                            modifier = Modifier
                                .padding(end = 10.dp, start = 20.dp, bottom = 10.dp)
                                .horizontalScroll(state = ScrollState(0)),
                            style = MaterialTheme.typography.h6,
                        )
                    }
                    Button(
                        onClick = {
                            deleteCard?.invoke(index)
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

            if (details.value && settings.value.detailsOnDoubleTap) Row(modifier = Modifier.fillMaxWidth()) {
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
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardWeatherPreview(
    @PreviewParameter(WeatherCardPreviewParameterProvider::class) content: WeatherCard
) {
    WeatherAppCleanTheme {
        CardWeather(
            modifier = Modifier,
            content = content,
            index = 0,
            settings = MutableStateFlow(
                Settings(
                    imperialUnits = false,
                    newCardFirst = true,
                    detailsOnDoubleTap = true,
                    dragAndDropCards = true
                )
            ).collectAsState(),
        )
    }
}

class WeatherCardPreviewParameterProvider : PreviewParameterProvider<WeatherCard> {
    override val values = sequenceOf(
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
}
