package com.learning.weatherappclean.presentation.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.weatherappclean.domain.model.Autocomplete
import com.learning.weatherappclean.presentation.MainViewModel
import com.learning.weatherappclean.presentation.ui.theme.autocomplete
import com.learning.weatherappclean.presentation.ui.theme.onAutocomplete

@Composable
fun DropDown(
    expanded: State<Boolean>,
    vm: MainViewModel,
    predictionList: State<List<Autocomplete.Prediction>>,
    isLandscape:Boolean

) {
    if (expanded.value)
        Column(
            modifier = Modifier
                .requiredSizeIn(maxHeight = 270.dp)
                .fillMaxWidth(if (isLandscape) 0.75f else 0.9f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
            ) {
                itemsIndexed(predictionList.value) { index, item ->
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colors.autocomplete.copy(alpha = 0.85f))
                    ) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable { vm.addCard(/*"${item.lat}, ${item.lon}"*/location ="${item.location}, ${item.country}, ${item.region}", prediction = item) }
                            .padding(5.dp)
                        )
                        {
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(
                                    text = item.location,
                                    modifier = Modifier
                                        .padding(top = 2.dp)
                                        .padding(start = 10.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colors.onAutocomplete
                                )
                                Text(
                                    text = "${item.country}, ${item.region}",
                                    modifier = Modifier
                                        .padding(bottom = 2.dp)
                                        .padding(start = 10.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colors.onAutocomplete
                                )
                            }
                        }
                        Divider(startIndent = 8.dp, thickness = 1.dp)
                    }
                }
            }
        }
}