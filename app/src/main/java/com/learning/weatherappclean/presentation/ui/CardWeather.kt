package com.learning.weatherappclean.presentation.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.learning.weatherappclean.R
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.weatherappclean.domain.model.CardColorOption
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.presentation.MainViewModel
import com.learning.weatherappclean.presentation.getIcon
import com.learning.weatherappclean.presentation.ui.theme.*
import java.util.*

@Composable
fun CardWeather(
    modifier: Modifier,
    content: WeatherCard,
    index: Int,
    delete: (Index: Int) -> Unit,
    setShowDetails:(Boolean,Int)-> Unit,
    settings: State<Settings>
) {

    val colour = when (content.cardColorOption) {
        CardColorOption.BLUE -> MaterialTheme.colors.cold
        CardColorOption.RED -> MaterialTheme.colors.hot
        CardColorOption.YELLOW -> MaterialTheme.colors.warm
        else -> Color.LightGray
    }
    val details = remember{ mutableStateOf(content.showDetails) }

    Card(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        details.value = !details.value
                       content.showDetails = details.value
                       setShowDetails(details.value,index)
                    }
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
                            contentDescription = "",
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
                            .padding(
                                start = 15.dp,
                                top = 0.dp,
                                bottom = 0.dp
                            )
                            .align(Alignment.TopStart),
                        fontSize = 60.sp,
                        fontWeight = FontWeight(600)
                    )

                    Column(modifier = Modifier.align(Alignment.BottomStart)) {
                        Text(
                            text = content.location.uppercase(Locale.ROOT),
                            color = MaterialTheme.colors.onCard,
                            modifier = Modifier
                                .padding(
                                    start = 20.dp,
                                    end = 10.dp,
                                    top = 0.dp,
                                    bottom = 0.dp
                                )
                                .horizontalScroll(state = ScrollState(0)),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = content.country,
                            color = MaterialTheme.colors.onCard,
                            modifier = Modifier
                                .padding(
                                    end = 10.dp,
                                    start = 20.dp,
                                    top = 0.dp,
                                    bottom = 10.dp
                                )
                                .horizontalScroll(state = ScrollState(0)),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = {
                            delete(index)
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
                            contentDescription = "Close",
                            modifier = Modifier.size(12.dp, 12.dp)
                        )
                    }
                }
            }

            if (details.value) Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .padding(start = 26.dp)
                ) {
                    Column() {

                        weatherDetails(
                            value = "${content.humidity}%",
                            iconId = R.drawable.ic_droplet
                        )
                        weatherDetails(
                            value = "${content.pressure}\nmbar",
                            iconId = R.drawable.ic_pressure
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(start = 20.dp)
                ) {
                    Column() {
                        weatherDetails(value = content.uvIndex, iconId = R.drawable.ic_sun)
                        weatherDetails(value = "${content.windSpeed}\nkm/h",iconId = R.drawable.ic_wind
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                ) {
                    Column() {
                        weatherDetails(
                            value = "${content.feelsLike}\u00b0${if (content.units == "f") "F" else "C"}",
                            iconId = R.drawable.ic_sun
                        )
                        weatherDetails(
                            value = "${content.cloudCover}%",
                            iconId = R.drawable.ic_overcast)
                    }
                }
            }
        }
    }
}


@Composable
fun weatherDetails(

    value: String,
    iconId: Int
) {

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {

        Row() {
            Icon(
                painter = painterResource(
                    id = iconId
                ),
                contentDescription = "",
                modifier = Modifier
                    .padding(0.dp)
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


        // Text(modifier = Modifier.align(Alignment.CenterEnd),text = value,color = MaterialTheme.colors.onCard,fontWeight = FontWeight.Bold)
    }


}