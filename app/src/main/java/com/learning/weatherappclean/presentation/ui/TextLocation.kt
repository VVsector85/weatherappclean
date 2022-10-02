package com.learning.weatherappclean.presentation.ui


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.weatherappclean.R
import com.learning.weatherappclean.presentation.MainViewModel
import com.learning.weatherappclean.presentation.ui.theme.autocomplete
import com.learning.weatherappclean.presentation.ui.theme.onTextField
import com.learning.weatherappclean.presentation.ui.theme.textField
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextLocation(
    modifier: Modifier,
    vm: MainViewModel,
    textSearch: State<String>,

) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    TextField(
        modifier = modifier

            .focusRequester(focusRequester),
           // .onFocusChanged { if (it.isCaptured) vm.setShowSearch(false) },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.textField,
            focusedIndicatorColor = Color.LightGray.copy(alpha = 0f),
            cursorColor = MaterialTheme.colors.onTextField
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                vm.addCard(textSearch.value,null)

            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        value = textSearch.value,
        onValueChange = {vm.getSearchText.value = it},
        maxLines = 3,
        textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
        leadingIcon = {
            Text(text = stringResource(id = R.string.location), modifier = Modifier.padding(start = 10.dp, end = 5.dp))
        },
        /*trailingIcon = {
            Card(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(50.dp, 50.dp)
                    .clickable {
                        vm.addCard(textSearch.value)
                        keyboardController?.hide()
                    }
                    .background(MaterialTheme.colors.textField.copy(alpha = 0f)),

                ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = "Add city",
                    modifier = Modifier
                        .size(25.dp, 25.dp)
                        .padding(10.dp).background(MaterialTheme.colors.autocomplete.copy(alpha = 0.0f)),
                    tint = MaterialTheme.colors.onTextField,
                    )
            }
        }*/
    )
}