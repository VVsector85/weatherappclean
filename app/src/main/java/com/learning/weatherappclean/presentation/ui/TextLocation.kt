package com.learning.weatherappclean.presentation.ui


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.weatherappclean.R
import com.learning.weatherappclean.presentation.MainViewModel
import com.learning.weatherappclean.presentation.ui.theme.onTextField
import com.learning.weatherappclean.presentation.ui.theme.textField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextLocation(
    vm: MainViewModel,
    expanded: MutableState<Boolean>,
    textLocation: MutableState<String>
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .alpha(1f),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.textField,
            focusedIndicatorColor = Color.LightGray,
            cursorColor = Color.DarkGray
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                vm.addCard(textLocation.value)
                textLocation.value = ""
                expanded.value = false
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        value = textLocation.value, onValueChange = { value ->
            textLocation.value = value
            vm.retrievePredictions(textLocation.value)
            expanded.value = true
        },
        maxLines = 3,
        textStyle = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
        leadingIcon = {
            Text(text = "Location", modifier = Modifier.padding(10.dp))
        },
        trailingIcon = {
            Card(
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxWidth(0.2f)
                    .height (60.dp)
                    .clickable {
                        vm.addCard(textLocation.value)
                        keyboardController?.hide()
                        textLocation.value = ""
                        expanded.value = false
                    },
                backgroundColor = MaterialTheme.colors.textField
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = "Add city",
                    modifier = Modifier
                        .size(25.dp, 25.dp)
                        .padding(15.dp),
                    tint = MaterialTheme.colors.onTextField,


                    )
            }
        }
    )


}