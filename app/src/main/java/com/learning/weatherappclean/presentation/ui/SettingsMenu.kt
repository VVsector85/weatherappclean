package com.learning.weatherappclean.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsMenu() {
    val checkedState = remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {
        Text(
            modifier = Modifier
                .padding(bottom = 15.dp)
                .align(Alignment.CenterHorizontally),
            text = "Settings",
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium
        )
        Divider(startIndent = 8.dp, thickness = 2.dp)
        Box(modifier = Modifier
            .fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .align(Alignment.CenterStart),
                text = "Fahrenheit",
                fontSize = 20.sp
            )
            Switch(
                modifier = Modifier.align(Alignment.CenterEnd),
                checked = checkedState.value,
                onCheckedChange = { checkedState.value = it }
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .align(Alignment.CenterStart),
                text = "Add on top",
                fontSize = 20.sp
            )
            Switch(
                modifier = Modifier.align(Alignment.CenterEnd),
                checked = checkedState.value,
                onCheckedChange = { checkedState.value = it }
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .align(Alignment.CenterStart),
                text = """Show "feels like" """,
                fontSize = 20.sp
            )
            Switch(
                modifier = Modifier.align(Alignment.CenterEnd),
                checked = checkedState.value,
                onCheckedChange = { checkedState.value = it }
            )
        }
        Divider(startIndent = 8.dp, thickness = 1.dp)
        Box(modifier = Modifier
            .fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .align(Alignment.CenterStart),
                text = "Show country",
                fontSize = 20.sp
            )
            Switch(
                modifier = Modifier.align(Alignment.CenterEnd),
                checked = checkedState.value,
                onCheckedChange = { checkedState.value = it }
            )
        }
    }
















}