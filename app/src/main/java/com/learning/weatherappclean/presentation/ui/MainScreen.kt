package com.learning.weatherappclean.presentation.ui

import android.content.res.Configuration
import android.graphics.Color.alpha
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.weatherappclean.R
import com.learning.weatherappclean.presentation.MainViewModel
import com.learning.weatherappclean.presentation.ui.theme.autocomplete
import com.learning.weatherappclean.presentation.ui.theme.onTextField

@Composable
fun MainScreen(vm: MainViewModel) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val showTopBar = remember {
        mutableStateOf(false)
    }
    val textLocation = remember { mutableStateOf("") }//to be deleted
    val searchText = vm.getSearchText.collectAsState()
    val expanded = vm.getExpanded.collectAsState()
    val errorMsg = vm.getError.collectAsState()
    val predictionsList = vm.getPredictions//.collectAsState()
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

            if (showTopBar.value) {
                Box(modifier = Modifier.padding(start=5.dp,top=15.dp))
                {
                    Icon(
                        painter = painterResource(id = com.google.android.material.R.drawable.material_ic_menu_arrow_up_black_24dp),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .size(50.dp)

                            .clickable { showTopBar.value = !showTopBar.value
                                vm.getExpanded.value = false}
                            .background(MaterialTheme.colors.autocomplete.copy(alpha = 0f)),
                        tint = MaterialTheme.colors.onTextField
                    )
                }
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
                        fontSize = 25.sp,
                    )
                    TextLocation(
                        modifier = Modifier.fillMaxWidth(if(isLandscape)0.75f else 0.9f),
                        vm = vm,

                        textSearch = searchText
                    )
                }
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

            if (!showTopBar.value)  Box(modifier = Modifier.padding(start=5.dp,top=15.dp))
        {
            Icon(
                painter = painterResource(id = androidx.appcompat.R.drawable.abc_ic_search_api_material),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(50.dp)

                    .clickable { showTopBar.value = !showTopBar.value
                    }
                    .background(MaterialTheme.colors.autocomplete.copy(alpha = 0.0f)),
                tint = MaterialTheme.colors.autocomplete.copy(alpha = 0.8f)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {






            DropDown(
                expanded = expanded,
                //textSearch = textLocation,
                vm = vm,
                predictionsList = predictionsList.collectAsState(initial = emptyList()
            )
            )
        }
    }
}
