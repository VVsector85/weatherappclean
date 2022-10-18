package com.learning.weatherappclean.presentation.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    h2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 60.sp,
        fontWeight = FontWeight.W600
    ),
    h4 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 26.sp,
        fontWeight = FontWeight.Medium
    ),
    h5 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),
    h6 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    ),
    subtitle2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
    ),
)
