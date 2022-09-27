package com.learning.weatherappclean.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.presentation.MainViewModel
import kotlin.reflect.KProperty1

@Composable
fun SettingsMenu(vm:MainViewModel) {
    val settings = vm.getSettings.collectAsState()
    val checkedStateUnits = remember { mutableStateOf(settings.value.fahrenheit) }
    val checkedStateAtTop = remember { mutableStateOf(settings.value.newCardFirst) }
    val checkedStateFeelsLike = remember { mutableStateOf(settings.value.showFeelsLike) }
    val checkedStateShowCountry = remember { mutableStateOf(settings.value.showCountry) }
    val newSettings = remember { mutableStateOf(settings.value)}

fun setV()   {




}

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {
        Text(
            modifier = Modifier
                .padding(bottom = 15.dp)
                .align(Alignment.CenterHorizontally),
            text = "Settings",
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )
        Divider(startIndent = 8.dp, thickness = 2.dp)
        MenuSwitchItem(text ="Fahrenheit",details = "default temperature scale is Celsius",checkedState = checkedStateUnits,setValue =vm::saveSettings,field =Settings::fahrenheit)
        MenuSwitchItem(text ="Add at the top",details ="Show new added weather card at the top of the list",checkedState = checkedStateAtTop,setValue =vm::saveSettings,field =Settings::newCardFirst)
        MenuSwitchItem(text ="Show \"feels like\"",checkedState = checkedStateFeelsLike,setValue =vm::saveSettings,field =Settings::showFeelsLike)
        Divider(startIndent = 8.dp, thickness = 1.dp)
        MenuSwitchItem(text ="Show country",checkedState = checkedStateShowCountry, setValue = vm::saveSettings,field =Settings::showCountry)




        }



    }





@Composable
fun MenuSwitchItem(text:String,
                   details:String?=null,
                   checkedState: MutableState<Boolean>,
                   setValue: (Boolean,KProperty1<Settings, *>) -> Unit,
                   field:KProperty1<Settings, *>
) {
     Box(modifier = Modifier
         .fillMaxWidth()
         .padding(top = 15.dp)) {
        Column( modifier = Modifier
            .fillMaxWidth(0.75f)
            .align(Alignment.CenterStart),) {
            Text(
                text = text,
                fontSize = 22.sp
            )
           details?.let {
               Text(
                   modifier = Modifier.padding(top = 4.dp),
                   text = details,
                   // fontSize = 20.sp
               )
           }
        }

        Switch(
            modifier = Modifier.align(Alignment.CenterEnd),
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it

               setValue(it,field)
            }
        )
    }
}
