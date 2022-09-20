package com.learning.weatherappclean

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner

import com.learning.weatherappclean.presentation.ui.theme.WeatherAppCleanTheme
import com.learning.weatherappclean.presentation.MainViewModel
import com.learning.weatherappclean.presentation.ui.WeatherList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()
    override fun onDestroy() {
        super.onDestroy()
        Log.d("my_tag", "activity destroyed")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("my_tag", "activity created")

        setContent {

            WeatherAppCleanTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(vm, this)
                }
            }
        }
    }
}

@Composable
fun MainScreen(vm: MainViewModel, owner: LifecycleOwner) {

    val textLocation = remember { mutableStateOf("") }
    val textError = remember { mutableStateOf("") }
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    vm.getError().observe(owner) {textError.value = it}

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "How is the weather in...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 18.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                )



                TextField(value = textLocation.value, onValueChange = { value ->
                    textLocation.value = value
                    //vm.getPredictions(textLocation.value)
                },
                    modifier = Modifier
                        .fillMaxWidth(0.9f).align(Alignment.CenterHorizontally) .padding(vertical = 10.dp),

                    textStyle = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),




                leadingIcon = {
                    Text(text = "Location", modifier = Modifier.padding(10.dp))
                },
                trailingIcon = {

                    Button(
                        onClick = {
                            vm.addCard(textLocation.value)
                            textLocation.value = ""
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = androidx.compose.ui.graphics.Color.White),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .padding(10.dp)
                            .size(50.dp)
                    ) {
                    Icon(

                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = "Add city",
                        tint = androidx.compose.ui.graphics.Color.Black,
                        modifier = Modifier
                            .size(25.dp, 25.dp)
                            .padding(0.dp)


                    )

                }
                }
                            )
                Text(text = "ERROR MESSAGE "+textError.value)



            }

        },


        drawerContent = { Text(text = "drawerContent") },


        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEEEEEE)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_alster),
                    contentDescription = "Alster logo",
                    modifier = Modifier
                        .size(60.dp, 50.dp)
                        .padding(8.dp),
                    tint = androidx.compose.ui.graphics.Color.Black
                )
            }
        }
    ){
            padding ->

            WeatherList(padding =padding,vm = vm, owner = owner)

    }








}





/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppCleanTheme {
        Greeting()
    }
}*/
