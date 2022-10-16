package com.learning.weatherappclean.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.weatherappclean.R
import com.learning.weatherappclean.domain.model.Settings
import kotlin.reflect.KProperty1

@Composable
fun SettingsMenu(
    settings: State<Settings>,
    saveSettings: (Boolean, KProperty1<Settings, *>) -> Unit,
    refreshCards: () -> Unit
) {
    val checkedStateUnits = remember { mutableStateOf(settings.value.imperialUnits) }
    val checkedStateAtTop = remember { mutableStateOf(settings.value.newCardFirst) }
    val checkedStateShowDetails = remember { mutableStateOf(settings.value.detailsOnDoubleTap) }
    val checkedStateDragAndDrop = remember { mutableStateOf(settings.value.dragAndDropCards) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 15.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.settings),
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )
        Divider(startIndent = 8.dp, thickness = 3.dp)
        MenuSwitchItem(
            text = stringResource(id = R.string.imperial),
            description = stringResource(id = R.string.imperialDescription),
            checkedState = checkedStateUnits,
            saveSettings = saveSettings,
            field = Settings::imperialUnits,
            action = refreshCards
        )
        MenuSwitchItem(
            text = stringResource(id = R.string.addAtTop),
            description = stringResource(id = R.string.addAtTopDescription),
            checkedState = checkedStateAtTop,
            saveSettings = saveSettings,
            field = Settings::newCardFirst
        )
        MenuSwitchItem(
            text = stringResource(id = R.string.showDetails),
            description = stringResource(id = R.string.showDetailsDescription),
            checkedState = checkedStateShowDetails,
            saveSettings = saveSettings,
            field = Settings::detailsOnDoubleTap
        )
        MenuSwitchItem(
            text = stringResource(id = R.string.dragAndDrop),
            description = stringResource(id = R.string.dragAndDropDescription),
            checkedState = checkedStateDragAndDrop,
            saveSettings = saveSettings,
            field = Settings::dragAndDropCards
        )
    }
}

@Composable
fun MenuSwitchItem(
    text: String,
    description: String? = null,
    checkedState: MutableState<Boolean>,
    saveSettings: (Boolean, KProperty1<Settings, *>) -> Unit,
    action: (() -> Unit)? = null,
    field: KProperty1<Settings, *>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .align(Alignment.CenterStart),
        ) {
            Text(
                text = text,
                fontSize = 22.sp
            )
            description?.let {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = description,
                )
            }
        }
        Switch(
            modifier = Modifier.align(Alignment.CenterEnd),
            checked = checkedState.value,
            colors = SwitchDefaults.colors(uncheckedThumbColor = Color.LightGray),
            onCheckedChange = {
                checkedState.value = it
                saveSettings(it, field)
                if (action != null) action()
            }
        )
    }
}
