package com.learning.weatherappclean.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.learning.weatherappclean.R
import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.presentation.ui.theme.WeatherAppCleanTheme
import com.learning.weatherappclean.presentation.ui.theme.onTextField
import com.learning.weatherappclean.presentation.ui.theme.textField
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextLocation(
    modifier: Modifier,
    textSearch: State<String>,
    setExpanded: ((Boolean) -> Unit)? = null,
    addCard: ((String, AutocompletePrediction?) -> Unit)? = null,
    setSearchText: ((String) -> Unit)? = null

) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    TextField(
        modifier = modifier.focusRequester(focusRequester),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.textField,
            focusedIndicatorColor = Color.LightGray.copy(alpha = 0f),
            cursorColor = MaterialTheme.colors.onTextField,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                setExpanded?.invoke(false)
                addCard?.invoke(textSearch.value, null)
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        value = textSearch.value,
        onValueChange = { setSearchText?.invoke(it) },
        maxLines = 3,
        textStyle = MaterialTheme.typography.h5,
        leadingIcon = {
            Text(
                text = stringResource(id = R.string.location),
                modifier = Modifier.padding(start = 10.dp, end = 5.dp)
            )
        },
        /**The "+" button seems to be redundant */
        /* trailingIcon = {
             Card(
                 modifier = Modifier
                     .padding(end = 10.dp)
                     .size(50.dp, 50.dp)
                     .clickable {
                         vm.addCard(location = textSearch.value, prediction = null)
                         keyboardController?.hide()
                         vm.setExpanded(false)
                     }
                     .background(MaterialTheme.colors.textField),

                 ) {
                 Icon(
                     painter = painterResource(id = R.drawable.ic_plus),
                     contentDescription = stringResource(id = R.string.addLocation),
                     modifier = Modifier
                         .size(25.dp, 25.dp)
                         .padding(10.dp)
                         .background(MaterialTheme.colors.textField),
                     tint = MaterialTheme.colors.onTextField,
                     )
             }
         }*/
    )
}

@Preview(showBackground = true)
@Composable
fun TextLocationPreview() {
    WeatherAppCleanTheme {
        TextLocation(
            modifier = Modifier,
            textSearch = MutableStateFlow("Some text").collectAsState()
        )
    }
}
