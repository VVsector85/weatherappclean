package com.learning.weatherappclean.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import com.learning.weatherappclean.R
import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.presentation.MainViewModel

@Composable
@ExperimentalMaterialApi
fun TextWithDropMenu(modifier: Modifier, vm: MainViewModel, owner: LifecycleOwner) {
    val predictions = remember { mutableStateOf(emptyList<AutocompletePrediction.Predictions>()) }
    val expanded = remember { mutableStateOf(false) }
    val textLocation = remember { mutableStateOf("") }
    vm.getPredictions().observe(owner) {
        predictions.value = it
        //expanded.value = true
    }


    expanded.value = (textLocation.value.length)>2
    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            /*.focusable(enabled = true)*/,
            expanded = expanded.value,
        onExpandedChange = {
            expanded.value = !expanded.value
        },

        ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            //.focusable(enabled = false)

              keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            value = textLocation.value, onValueChange = { value ->
                textLocation.value = value
                vm.retrievePredictions(textLocation.value)

            },


            textStyle = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),


            leadingIcon = {
                Text(text = "Location", modifier = Modifier.padding(10.dp))
            },
            trailingIcon = {

                Button(
                    onClick = {
                        vm.addCard(textLocation.value)
                        textLocation.value = ""
                        expanded.value = false
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .padding(10.dp)
                        .size(50.dp)
                ) {
                    Icon(

                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = "Add city",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(25.dp, 25.dp)
                            .padding(0.dp)


                    )

                }
            }
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            modifier = Modifier
                .requiredSizeIn(maxHeight = 200.dp)
                /*.focusable(true)*/,
            onDismissRequest = {
               // expanded.value = false
            }
        ) {
            predictions.value.forEach { s ->
                DropdownMenuItem(
                    onClick = {
                        textLocation.value = "${s.location}, ${s.country}"
                        expanded.value = false
                    },
                    //modifier = Modifier.focusable(true)
                )


                {
                    Column(
                        modifier = Modifier
                            .padding(
                                horizontal = 8.dp,
                                vertical = 0.dp
                            )
                            .background(Color.Cyan)
                    ) {
                        Text(
                            text = s.location,
                            modifier = Modifier.padding(vertical = 0.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "${s.country}, ${s.region}",
                            modifier = Modifier.padding(vertical = 0.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )


                    }


                }
            }
        }
    }


    /*Text(text = "anchor")
    IconButton(onClick = { expanded.value = true }) {
        Icon(Icons.Default.MoreVert, contentDescription = "Settings")
    }*/

}


