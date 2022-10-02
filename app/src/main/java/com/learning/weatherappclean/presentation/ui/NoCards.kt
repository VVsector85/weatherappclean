package com.learning.weatherappclean.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.weatherappclean.R
import com.learning.weatherappclean.presentation.MainViewModel
import com.learning.weatherappclean.presentation.ui.theme.onTextField

@Composable
fun NoCards(noRequests: State<Boolean>, vm: MainViewModel) {

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center)
    {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = if (!noRequests.value)
                stringResource(R.string.noCardsLoaded)
            else stringResource(R.string.noItemsToShow),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onTextField
        )
        if (!noRequests.value) Button(
            onClick = { vm.refreshCards() }, modifier = Modifier
                .padding(30.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.tryAgain), modifier = Modifier.padding(20.dp))
        }
    }
}