package com.learning.weatherappclean.util

import com.learning.weatherappclean.R

fun getIcon(weatherCode:Int, isNight:Boolean):Int{

   return when (weatherCode){

       395,371,338,335,332,329 ->  R.drawable.ic_cloud_snow
       392,389,386,200 ->R.drawable.ic_storm
       377,374,365,362,350,317,314,311,284,281,185,182,179->R.drawable.ic_cloud_mixed
       368,326,323,320,227 -> R.drawable.ic_cloud_snowflake
       359,308,302,296 -> R.drawable.ic_cloud_rain
       356,305,299 -> R.drawable.ic_rain
       353,293,266,263,176 -> R.drawable.ic_cloud_drop
       260,248,134,143 -> R.drawable.ic_fog
       230 -> R.drawable.ic_snow
       122 -> R.drawable.ic_overcast
       119 -> R.drawable.ic_partly_cloudy
       116 -> if (isNight) R.drawable.ic_cloud_night else R.drawable.ic_cloud_day
       113 -> if (isNight) R.drawable.ic_moon else R.drawable.ic_sun

        else -> R.drawable.ic_unknown
    }




}