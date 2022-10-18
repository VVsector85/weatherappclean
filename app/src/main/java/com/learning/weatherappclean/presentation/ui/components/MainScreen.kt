package com.learning.weatherappclean.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.weatherappclean.R
import com.learning.weatherappclean.presentation.MainViewModel
import com.learning.weatherappclean.presentation.ui.components.weatherlist.WeatherList
import com.learning.weatherappclean.presentation.ui.theme.onTextField

@Composable
fun MainScreen(vm: MainViewModel) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val showSearch = vm.getShowSearch.collectAsState()
    val searchText = vm.getSearchText.collectAsState()
    val expanded = vm.getExpanded.collectAsState()
    val errorMsg = vm.getError.collectAsState()
    val predictionsList = vm.getPredictions.collectAsState(initial = emptyList())
    val weatherCardList = vm.getCardList
    val isLoading = vm.getLoadingState.collectAsState()
    val scrollToFirst = vm.getScrollToFirst.collectAsState()
    val settings = vm.getSettings.collectAsState()
    val noRequests = vm.getNoRequests.collectAsState()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        scaffoldState = scaffoldState,
        drawerContent = {
            SettingsMenu(settings = settings, saveSettings = vm::saveSettings, refreshCards = vm::refreshCards)
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
                    contentDescription = stringResource(id = R.string.alsterLogo),
                    modifier = if (isLandscape) {
                        Modifier
                            .size(30.dp)
                            .padding(4.dp)
                    } else {
                        Modifier
                            .size(55.dp)
                            .padding(8.dp)
                    },
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    ) { padding ->
        WeatherList(
            padding = if (showSearch.value) PaddingValues(top = 80.dp) else if (!showSearch.value && !isLandscape) PaddingValues(top = 60.dp) else padding,
            weatherCardList = weatherCardList,
            isLoading = isLoading,
            scrollToFirst = scrollToFirst,
            isLandscape = isLandscape,
            settings = settings,
            errorMessage = errorMsg,
            noRequests = noRequests,
            deleteCard = vm::deleteCard,
            stopScrollToFirst = vm::stopScrollToFirst,
            resetErrorMessage = vm::resetErrorMessage,
            setShowDetails = vm::setShowDetails,
            refreshCards = vm::refreshCards,
            setExpanded = vm::setExpanded,
            setShowSearch = vm::setShowSearch,
            swapSections = vm::swapSections

        )
        Box(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            if (!showSearch.value) Button(
                onClick = {
                    vm.setShowSearch(true)
                    vm.setExpanded(false)
                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .padding(5.dp)
                    .align(if (isLandscape)Alignment.TopStart else Alignment.TopEnd)
                    .size(50.dp),
                shape = CircleShape,
            ) {
                Icon(
                    painter = painterResource(id = com.google.android.material.R.drawable.abc_ic_search_api_material),
                    contentDescription = stringResource(id = R.string.searchLocation),
                    modifier = Modifier
                        .size(35.dp),
                    tint = MaterialTheme.colors.onTextField
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (showSearch.value)
                TextLocation(
                    modifier = Modifier
                        .fillMaxWidth(if (isLandscape) 0.75f else 0.9f)
                        .height(60.dp),
                    addCard = vm::addCard,
                    setExpanded = vm::setExpanded,
                    setSearchText = vm::setSearchText,
                    textSearch = searchText
                ) else if (!isLandscape)
                Text(
                    text = stringResource(id = R.string.howIsTheWeather),
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                )
            DropDown(
                expanded = expanded,
                addCard = vm::addCard,
                predictionList = predictionsList,
                isLandscape = isLandscape
            )
        }
    }
}
