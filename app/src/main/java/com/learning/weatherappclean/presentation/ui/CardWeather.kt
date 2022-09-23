package com.learning.weatherappclean.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import com.learning.weatherappclean.R
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.weatherappclean.domain.model.CardColorOption
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.presentation.MainViewModel
import com.learning.weatherappclean.presentation.ui.theme.*
import java.util.*

@Composable
fun CardWeather(
    modifier: Modifier,
    content: WeatherCard,
    vm: MainViewModel,
    index: Int
) {

    val colour = when (content.cardColorOption) {
        CardColorOption.BLUE -> MaterialTheme.colors.cold
        CardColorOption.RED -> MaterialTheme.colors.hot
        CardColorOption.YELLOW -> MaterialTheme.colors.warm
        else -> Color.LightGray
    }
    Card(
        modifier = modifier,
        backgroundColor = colour,
    ) {
        Row() {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .align(Alignment.CenterVertically)
            )
            {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_alster),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(20.dp)
                            .size(70.dp, 70.dp),
                        tint = MaterialTheme.colors.onCard
                    )
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {

                    Text(
                        text = "${content.temperature}\u00b0",
                        color = MaterialTheme.colors.onCard,
                        modifier = Modifier.padding(
                            start = 15.dp,
                            top = 0.dp,
                            bottom = 0.dp
                        ).align(Alignment.TopStart),
                        fontSize = 60.sp,
                        fontWeight = FontWeight(600)
                    )

                    Column(modifier = Modifier.align(Alignment.BottomStart)) {
                    Text(
                        text = content.location.uppercase(Locale.ROOT),
                        color = MaterialTheme.colors.onCard,
                        modifier = Modifier.padding(
                            start = 20.dp,
                            top = 0.dp,
                            bottom = 0.dp
                        ),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = content.country,
                        color = MaterialTheme.colors.onCard,
                        modifier = Modifier.padding(
                            start = 20.dp,
                            top = 0.dp,
                            bottom = 10.dp
                        ),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Button(
                    onClick = {
                        vm.deleteCard(index)
                    },
                    /*colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black
                    ),*/
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
    }
}