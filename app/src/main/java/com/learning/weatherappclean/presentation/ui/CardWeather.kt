package com.learning.weatherappclean.presentation.ui

import androidx.compose.foundation.background
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
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.presentation.MainViewModel
import java.util.*

@Composable
fun CardWeather(content:WeatherCard, vm:MainViewModel, index:Int){

        Card(modifier = Modifier
            .padding(horizontal = 0.dp, vertical = 10.dp)
            .fillMaxWidth(0.9f)
            .height(140.dp),
            backgroundColor = Color.Gray,


            ) {
            Row() {
                Box(

                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .align(Alignment.CenterVertically)
                    //.background(Color.Green),


                )
                {
                    /*Text(
                        text = testValue.value.description,
                        fontSize = 10.sp,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(12.dp),
                    )*/


                    Column(modifier = Modifier.align(Alignment.Center)) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_alster),
                            contentDescription = "",
                            modifier = Modifier

                                .padding(20.dp)
                                .size(70.dp, 70.dp),
                            tint = Color.White

                        )

                    }

                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column() {
                        Text(
                            text = "${content.temperature}\u00b0",
                            color = Color.White,
                            modifier = Modifier.padding(
                                start = 15.dp,
                                top = 5.dp,
                                bottom = 0.dp
                            ),
                            fontSize = 60.sp,
                            fontWeight = FontWeight(600)
                        )
                        Text(
                            text = content.location.uppercase(Locale.ROOT),
                            color = Color.White,
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
                            color = Color.White,
                            modifier = Modifier.padding(
                                start = 20.dp,
                                top = 0.dp
                            ),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = {
                            vm.deleteCard(index)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                            contentColor = Color.Black
                        ),
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
                            //tint = Color.Cyan
                        )
                    }
                }

        }
    }

}