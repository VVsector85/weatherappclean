package com.learning.weatherappclean.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = PrimaryNight,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = BackgroundNight,
    surface = Color.Black,
    onPrimary = Color.White,
    onSecondary = Color.DarkGray,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = OnErrorNight
)

private val LightColorPalette = lightColors(
    primary = PrimaryDay,
    primaryVariant = Color.LightGray,
    secondary = Teal200,

    background = BackgroundDay,
    surface = Color.White,
    onPrimary = Color.Black,
    onSecondary = Color.DarkGray,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = OnErrorDay
)

val Colors.hot: Color
    get() = if (isLight) HotDay else HotNight
val Colors.warm: Color
    get() = if (isLight) WarmDay else WarmNight
val Colors.cold: Color
    get() = if (isLight) ColdDay else ColdNight
val Colors.onCard: Color
    get() = if (isLight) OnCardDay else OnCardNight
val Colors.textField: Color
    get() = if (isLight) TextFieldDay else TextFieldNight
val Colors.onTextField: Color
    get() = if (isLight) OnTextFieldDay else OnTextFieldNight
val Colors.autocomplete: Color
    get() = if (isLight) AutocompleteDay else AutocompleteNight
val Colors.onAutocomplete: Color
    get() = if (isLight) OnAutocompleteDay else OnAutocompleteNight

@Composable
fun WeatherAppCleanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
