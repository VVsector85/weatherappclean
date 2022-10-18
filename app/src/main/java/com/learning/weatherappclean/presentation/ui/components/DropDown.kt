package com.learning.weatherappclean.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.presentation.ui.theme.WeatherAppCleanTheme
import com.learning.weatherappclean.presentation.ui.theme.autocomplete
import com.learning.weatherappclean.presentation.ui.theme.onAutocomplete
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun DropDown(
    expanded: State<Boolean>,
    predictionList: State<List<AutocompletePrediction>>,
    isLandscape: Boolean,
    addCard: ((String, AutocompletePrediction?) -> Unit)? = null
) {
    if (expanded.value)
        Column(
            modifier = Modifier
                .requiredSizeIn(maxHeight = 270.dp)
                .fillMaxWidth(if (isLandscape) 0.75f else 0.9f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn() {
                itemsIndexed(predictionList.value) { _, item ->
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colors.autocomplete.copy(alpha = 0.80f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    addCard?.invoke(
                                        "${item.location}, ${item.country}, ${item.region}",
                                        item
                                    )
                                }
                                .padding(5.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(
                                    text = item.location,
                                    modifier = Modifier.padding(top = 2.dp, start = 10.dp),
                                    style = MaterialTheme.typography.h6,
                                    color = MaterialTheme.colors.onAutocomplete
                                )
                                Text(
                                    text = "${item.country}, ${item.region}",
                                    modifier = Modifier.padding(bottom = 2.dp, start = 10.dp),
                                    style = MaterialTheme.typography.subtitle2,
                                    color = MaterialTheme.colors.onAutocomplete
                                )
                            }
                        }
                        Divider(thickness = 1.dp)
                    }
                }
            }
        }
}

@Preview(showBackground = true)
@Composable
fun DropDownPreview() {
    WeatherAppCleanTheme {
        DropDown(
            expanded = MutableStateFlow(true).collectAsState(),
            isLandscape = false,
            predictionList = MutableStateFlow(
                listOf(
                    AutocompletePrediction(
                        location = "London",
                        country = "United Kingdom",
                        region = "Greater London",
                        lat = "",
                        lon = ""
                    ),
                    AutocompletePrediction(
                        location = "Paris",
                        country = "France",
                        region = "Ile-de-France",
                        lat = "",
                        lon = ""
                    ),
                    AutocompletePrediction(
                        location = "New York",
                        country = "United States of America",
                        region = "New York",
                        lat = "",
                        lon = ""
                    )

                )

            ).collectAsState(),
        )
    }
}
