package com.learning.weatherappclean.util

import com.learning.weatherappclean.R

fun getCardResources(weatherCode: Int, isNight: Boolean): CardResources {

    return when (weatherCode) {

        395, 371, 338, 335, 332, 329 -> CardResources(R.drawable.ic_cloud_snow, R.raw.snow)
        392, 389, 386, 200 -> CardResources(R.drawable.ic_storm, R.raw.lightning)
        377, 374, 365, 362, 350, 317, 314, 311, 284, 281, 185, 182, 179 -> CardResources(R.drawable.ic_cloud_mixed, R.raw.rain) // wrong video
        368, 326, 323, 320, 227 -> CardResources(R.drawable.ic_cloud_snowflake, R.raw.snow_flake)
        359, 308, 302, 296 -> CardResources(R.drawable.ic_cloud_rain, R.raw.rain_2)
        356, 305, 299 -> CardResources(R.drawable.ic_rain, R.raw.rain_2) // wrong video
        353, 293, 266, 263, 176 -> CardResources(R.drawable.ic_cloud_drop, R.raw.rain)
        260, 248, 134, 143 -> CardResources(R.drawable.ic_fog, R.raw.fog)
        230 -> CardResources(R.drawable.ic_snow, R.raw.heavy_snow)
        122 -> CardResources(R.drawable.ic_overcast, R.raw.overcast)
        119 -> CardResources(R.drawable.ic_partly_cloudy, R.raw.partly_cloudy)
        116 -> if (isNight) CardResources(R.drawable.ic_cloud_night, R.raw.clouds_moon) else CardResources(R.drawable.ic_cloud_day, R.raw.clouds_sun)
        113 -> if (isNight) CardResources(R.drawable.ic_moon, R.raw.clear_night) else CardResources(R.drawable.ic_sun, R.raw.sunny_day)

        else -> CardResources(R.drawable.ic_unknown, R.raw.sunny_day)
    }
}
