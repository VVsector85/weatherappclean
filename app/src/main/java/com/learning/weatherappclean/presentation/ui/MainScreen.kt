package com.learning.weatherappclean.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.weatherappclean.R
import com.learning.weatherappclean.presentation.MainViewModel

@Composable
fun MainScreen(vm: MainViewModel) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val textLocation = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }
    val errorMsg = vm.getError.collectAsState()
    val predictionsList = vm.getPredictions.collectAsState()
    val weatherCardList = vm.getList.collectAsState()
    val isLoading = vm.getLoadingState.collectAsState()
    val scrollToFirst = vm.getScrollToFirst.collectAsState()
    val settings = vm.getSettings.collectAsState()
    val noRequests = vm.getNoRequests.collectAsState()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE


    val iconModifier =
        if (isLandscape) {
            Modifier
                .size(30.dp)
                .padding(4.dp)
        } else {
            Modifier
                .size(55.dp)
                .padding(8.dp)
        }


    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        scaffoldState = scaffoldState,
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isLandscape) Text(
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
        },
        drawerContent = {
            SettingsMenu(vm)
        },
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
            isLoading = isLoading,
            scrollToFirst = scrollToFirst,
            isLandscape = isLandscape,
            settings = settings,
            errorMsg = errorMsg,
            noRequests = noRequests
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DropDown(
                expanded = expanded,
                textLocation = textLocation,
                predictionsList = predictionsList
            )
        }
    }
}
