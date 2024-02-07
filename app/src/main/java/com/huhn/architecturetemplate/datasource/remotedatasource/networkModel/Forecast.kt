package com.huhn.architecturetemplate.datasource.remotedatasource.networkModel

data class Forecast(
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val rain: Rain,
    val sys: Sys,
)
