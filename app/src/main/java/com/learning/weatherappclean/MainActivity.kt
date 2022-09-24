package com.learning.weatherappclean

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.learning.weatherappclean.presentation.MainViewModel
import com.learning.weatherappclean.presentation.ui.DropDown
import com.learning.weatherappclean.presentation.ui.TextLocation
import com.learning.weatherappclean.presentation.ui.WeatherList
import com.learning.weatherappclean.presentation.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppCleanTheme {

                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(
                    color = MaterialTheme.colors.background
                )
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    MainScreen(vm)
                }
            }
        }
    }
}

@Composable

fun MainScreen(vm: MainViewModel) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val textLocation = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }
    val errorMsg = vm.getError.collectAsState()
    val predictionsList = vm.getPredictions.collectAsState()
    val weatherCardList = vm.getList.collectAsState()
    val loadingState = vm.getLoadingState.collectAsState()
    val scrollToFirst = vm.getScrollToFirst.collectAsState()
    val iconModifier =
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Modifier
                .size(30.dp)
                .padding(4.dp)
        } else {
            Modifier
                .size(55.dp)
                .padding(8.dp)
        }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {


                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "How is the weather in...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp, bottom = 5.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                    )

                    TextLocation(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        vm = vm,
                        expanded = expanded,
                        textLocation = textLocation
                    )

                }


            } else {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "How is the weather in...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 18.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp,
                    )
                    TextLocation(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        vm = vm,
                        expanded = expanded,
                        textLocation = textLocation
                    )

                }


            }

        },
        drawerContent = { Text(text = "drawerContent") },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background),

                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_alster),
                    contentDescription = "Alster logo",
                    modifier = iconModifier,
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    ) { padding ->

        WeatherList(
            padding = padding,
            vm = vm,
            weatherCardList = weatherCardList,
            loadingState = loadingState,
            scrollToFirst = scrollToFirst
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (errorMsg.value != "") Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
                    .padding(0.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = errorMsg.value,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            DropDown(
                expanded = expanded,
                textLocation = textLocation,
                predictionsList = predictionsList
            )
        }
    }
}


/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppCleanTheme {
        Greeting()
    }
}*/
