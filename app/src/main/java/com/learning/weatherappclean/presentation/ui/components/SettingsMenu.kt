package com.learning.weatherappclean.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.learning.weatherappclean.R
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.presentation.ui.theme.WeatherAppCleanTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.reflect.KProperty1

@Composable
fun SettingsMenu(
    settings: State<Settings>,
    saveSettings: ((Boolean, KProperty1<Settings, *>) -> Unit)? = null,
    refreshCards: (() -> Unit)? = null
) {
    val checkedStateUnits = remember { mutableStateOf(settings.value.imperialUnits) }
    val checkedStateAtTop = remember { mutableStateOf(settings.value.newCardFirst) }
    val checkedStateShowDetails = remember { mutableStateOf(settings.value.detailsOnDoubleTap) }
    val checkedStateDragAndDrop = remember { mutableStateOf(settings.value.dragAndDropCards) }
    val checkedStateShowVideo = remember { mutableStateOf(settings.value.showVideo) }
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
            style = MaterialTheme.typography.h4
        )
        Divider(thickness = 3.dp)
        MenuSwitchItem(
            text = stringResource(id = R.string.imperial),
            description = stringResource(id = R.string.imperialDescription),
            checkedState = checkedStateUnits,
            saveSettings = saveSettings,
            property = Settings::imperialUnits,
            action = refreshCards
        )
        MenuSwitchItem(
            text = stringResource(id = R.string.addAtTop),
            description = stringResource(id = R.string.addAtTopDescription),
            checkedState = checkedStateAtTop,
            saveSettings = saveSettings,
            property = Settings::newCardFirst
        )
        MenuSwitchItem(
            text = stringResource(id = R.string.showDetails),
            description = stringResource(id = R.string.showDetailsDescription),
            checkedState = checkedStateShowDetails,
            saveSettings = saveSettings,
            property = Settings::detailsOnDoubleTap
        )
        MenuSwitchItem(
            text = stringResource(id = R.string.dragAndDrop),
            description = stringResource(id = R.string.dragAndDropDescription),
            checkedState = checkedStateDragAndDrop,
            saveSettings = saveSettings,
            property = Settings::dragAndDropCards
        )
        MenuSwitchItem(
            text =  stringResource(id = R.string.showVideo),
            description =  stringResource(id = R.string.showVideoDescription),
            checkedState = checkedStateShowVideo,
            saveSettings = saveSettings,
            property = Settings::showVideo
        )
    }
}

@Composable
fun MenuSwitchItem(
    text: String,
    description: String? = null,
    checkedState: MutableState<Boolean>,
    saveSettings: ((Boolean, KProperty1<Settings, *>) -> Unit)? = null,
    action: (() -> Unit)? = null,
    property: KProperty1<Settings, *>
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
                style = MaterialTheme.typography.h5
            )
            description?.let {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = description,
                    style = MaterialTheme.typography.body1
                )
            }
        }
        Switch(
            modifier = Modifier.align(Alignment.CenterEnd),
            checked = checkedState.value,
            colors = SwitchDefaults.colors(uncheckedThumbColor = Color.LightGray),
            onCheckedChange = {
                checkedState.value = it
                saveSettings?.invoke(it, property)
                action?.invoke()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsMenuPreview() {
    WeatherAppCleanTheme {
        SettingsMenu(
            settings = MutableStateFlow(
                Settings(
                    imperialUnits = false,
                    newCardFirst = true,
                    detailsOnDoubleTap = true,
                    dragAndDropCards = true,
                    showVideo = false
                )
            ).collectAsState()
        )
    }
}
