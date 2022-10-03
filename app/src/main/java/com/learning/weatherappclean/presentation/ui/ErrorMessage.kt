package com.learning.weatherappclean.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.learning.weatherappclean.presentation.MainViewModel
import com.learning.weatherappclean.util.ErrorMessage
import com.learning.weatherappclean.util.ErrorTypeUi
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ErrorMessage(errorMsg: State<ErrorMessage>, resetError:()->Unit) {
    var text:String = ""
    if (errorMsg.value.errorType != null) {
        val error = errorMsg.value.getErrorMessage()
        val errorStringId = error.first

        text = if (errorMsg.value.errorType == ErrorTypeUi.SAME_ITEM_ERROR) {
            "${error.second} ${stringResource(errorStringId!!)}"
        }else{
            if (errorStringId != null) stringResource(errorStringId) else error.second
        }

        LaunchedEffect(Unit) {
            delay(errorMsg.value.showTime)
            resetError()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .padding(horizontal = 20.dp)
        ) {


            Text(
                modifier = Modifier.align(Alignment.Center),
                text = text,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onError,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

