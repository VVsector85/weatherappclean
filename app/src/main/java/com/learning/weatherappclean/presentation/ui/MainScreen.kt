package com.learning.weatherappclean.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color


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
    val showSearch =vm.getShowSearch.collectAsState()
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
            Box(modifier = Modifier.fillMaxWidth()) {
              if (!showSearch.value)  Button(
                    onClick = {
                        vm.setShowSearch(true)
                        vm.setExpanded(false)
                    },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.TopEnd)
                        .size(50.dp),
                    shape = CircleShape,
                    //colors = ButtonDefaults.buttonColors(backgroundColor = androidx.compose.ui.graphics.Color.Cyan.copy(alpha = 0.3f))
                )
                {
                    Icon(
                        painter = painterResource(id = com.google.android.material.R.drawable.abc_ic_search_api_material),
                        contentDescription = "",
                        modifier = Modifier

                            .size(35.dp),


                            //.background(MaterialTheme.colors.autocomplete.copy(alpha = 0f)),
                        tint = MaterialTheme.colors.onTextField
                    )
                }
            }


            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (showSearch.value)
                    TextLocation(
                        modifier = Modifier.fillMaxWidth(if (isLandscape) 0.75f else 0.9f),
                        vm = vm,

                        textSearch = searchText
                    )else if (!isLandscape) Row() {


                    Text(
                        text = "How is the weather in...",
                        modifier = Modifier

                            .padding(top = 16.dp, bottom = 18.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp,
                    )


                }
            }


        },
        drawerContent = {
            SettingsMenu(vm = vm,settings = settings)
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

        if (!showSearch.value) Box(modifier = Modifier.padding(start = 5.dp, top = 15.dp))
        {

            /* Button( modifier = Modifier

                 .size(50.dp).padding(0.dp),

                 onClick = {showTopBar.value = !showTopBar.value  },
                 shape = RoundedCornerShape(50),
                 colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.autocomplete.copy(alpha = 0.3f))

             )
             {
                 Icon(
                     painter = painterResource(id = androidx.appcompat.R.drawable.abc_ic_search_api_material),
                     contentDescription = "",

                   //  tint = MaterialTheme.colors.autocomplete.copy(alpha = 0.8f)
                 )
             }
 */

        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            DropDown(
                expanded = expanded,
                //textSearch = textLocation,
                vm = vm,
                predictionsList = predictionsList.collectAsState(
                    initial = emptyList()
                )
            )
        }
    }
}
