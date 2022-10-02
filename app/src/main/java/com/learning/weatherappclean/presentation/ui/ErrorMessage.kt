package com.learning.weatherappclean.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ErrorMessage(errorMsg: State<String>) {
    if (errorMsg.value != "") Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .padding(horizontal = 20.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = errorMsg.value,
            color =  MaterialTheme.colors.onError,
            fontWeight = FontWeight.Bold
        )
    }


}