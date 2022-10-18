package com.learning.weatherappclean.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.learning.weatherappclean.presentation.ui.theme.WeatherAppCleanTheme
import com.learning.weatherappclean.util.ErrorMessageProvider
import com.learning.weatherappclean.util.ErrorTypeUi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ErrorMessage(
    errorMessageProvider: State<ErrorMessageProvider>,
    resetError: (() -> Unit)? = null
) {
    val text: String
    if (errorMessageProvider.value.errorType != null) {
        val error = errorMessageProvider.value.getErrorMessage()
        val errorStringId = error.first

        text = if (errorMessageProvider.value.errorType == ErrorTypeUi.SAME_ITEM_ERROR) {
            if (errorStringId != null) "${error.second} ${stringResource(errorStringId)}" else ""
        } else {
            if (errorStringId != null) stringResource(errorStringId) else error.second
        }
        LaunchedEffect(Unit) {
            delay(errorMessageProvider.value.showTime)
            resetError?.invoke()
        }
        Box(
            modifier = Modifier
                .fillMaxWidth().padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onError,
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorMessagePreview(
    @PreviewParameter(ErrorMessagePreviewParameterProvider::class) errorMessageProvider: ErrorMessageProvider
) {
    WeatherAppCleanTheme {
        ErrorMessage(
            errorMessageProvider = MutableStateFlow(errorMessageProvider).collectAsState()
        )
    }
}

class ErrorMessagePreviewParameterProvider : PreviewParameterProvider<ErrorMessageProvider> {
    override val values = sequenceOf(
        ErrorMessageProvider(
            errorType = ErrorTypeUi.IO_ERROR,
            showTime = 5000,
            errorString = "test string"
        ),
        ErrorMessageProvider(
            errorType = ErrorTypeUi.UNKNOWN_ERROR,
            showTime = 5000,
            errorString = "test string"
        ),
        ErrorMessageProvider(
            errorType = ErrorTypeUi.SAME_ITEM_ERROR,
            showTime = 5000,
            errorString = "test string"
        ),
        ErrorMessageProvider(
            errorType = ErrorTypeUi.API_ERROR,
            showTime = 5000,
            errorString = "test string"
        ),
        ErrorMessageProvider(
            errorType = ErrorTypeUi.API_ERROR,
            showTime = 5000,
            errorString = "test string",
            errorCode = 101
        ),
        ErrorMessageProvider(
            errorType = ErrorTypeUi.API_ERROR,
            showTime = 5000,
            errorString = "test string",
            errorCode = 102
        ),
        ErrorMessageProvider(
            errorType = ErrorTypeUi.API_ERROR,
            showTime = 5000,
            errorString = "test string",
            errorCode = 104
        ),
        ErrorMessageProvider(
            errorType = ErrorTypeUi.API_ERROR,
            showTime = 5000,
            errorString = "test string",
            errorCode = 601
        ),
        ErrorMessageProvider(
            errorType = ErrorTypeUi.API_ERROR,
            showTime = 5000,
            errorString = "test string",
            errorCode = 602
        ),
        ErrorMessageProvider(
            errorType = ErrorTypeUi.API_ERROR,
            showTime = 5000,
            errorString = "test string",
            errorCode = 615
        ),

    )
}
