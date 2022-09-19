package com.learning.weatherappclean.presentation.ui

import com.learning.weatherappclean.R
import android.util.Log
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
fun CardWeather(modifier: Modifier,testValue:WeatherCard, vm:MainViewModel, index:Int){
    Card(
        modifier = modifier,
        //elevation = 4.dp,
        backgroundColor = Color.Gray


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
                    /*if (testValue.value.isUpdated) {*/
                    Icon(
                        painter = painterResource(id = R.drawable.ic_alster),
                        contentDescription = "",
                        modifier = Modifier

                            .padding(20.dp)
                            .size(70.dp, 70.dp),
                        tint = Color.White

                    )
                    /*} else {
                        CircularProgressIndicator(color = Color.White)
                    }*/
                }

            }
            Box(modifier = Modifier.fillMaxWidth()) {


                Column() {

                    Text(
                        text = testValue.temperature,
                        color = Color.White,
                        modifier = Modifier.padding(
                            start = 25.dp,
                            top = 12.dp,
                            bottom = 0.dp
                        ),
                        fontSize = 58.sp,
                        fontWeight = FontWeight(500)
                    )
                    Text(
                        text = testValue.location.uppercase(Locale.ROOT),
                        color = Color.White,
                        modifier = Modifier.padding(
                            start = 25.dp,
                            top = 0.dp
                        ),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold

                    )
                    Text(
                        text = testValue.country,
                        color = Color.White,
                        modifier = Modifier.padding(
                            start = 25.dp,
                            top = 0.dp
                        ),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold

                    )

                }



                Button(
                    onClick = { /*TODO*/
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

                        painter = painterResource(id = R.drawable.ic_alster),
                        contentDescription = "Close",
                        modifier = Modifier.size(12.dp, 12.dp)
                        //tint = Color.Cyan
                    )
                }
            }
        }
    }
}