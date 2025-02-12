package com.huhn.fetchrewards.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import java.util.Locale


fun Context.getActivity(): ComponentActivity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is ComponentActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

fun convertEpochToTime(epoch: Long): String {
    val date = Date(epoch)
    val sdf = SimpleDateFormat("hh:mm a", Locale.US)
    //    sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
    //    val dateString = getDateTimeInstance().format(date)
    //    val return dateString
    return sdf.format(date)
}
fun convertEpochToDaysSinceEpoch(epoch: Long): Long {
    val instant = Instant.ofEpochMilli(epoch)
    val zoneId = ZoneId.systemDefault() // Use the system default time zone

    val date = instant.atZone(zoneId).toLocalDate()
    val daysSinceEpoch = date.toEpochDay()
    return daysSinceEpoch
}

fun convertEpochToDayMonth(epoch: Long): String {

    val instant = Instant.ofEpochMilli(epoch)
    val zoneId = ZoneId.systemDefault() // Use the system default time zone

    val date = instant.atZone(zoneId).toLocalDate()

    val dayOfWeek = date.dayOfWeek
    val month = date.month
    val dayOfMonth = date.dayOfMonth
    val displayString = "$dayOfWeek, $month $dayOfMonth"

   return displayString
}


fun roundTemp(temp: String): String {
    return temp.substringBefore(".")
}

fun deepCopyFloatArray(array: FloatArray): FloatArray {
    val copiedArray = FloatArray(array.size)
    for (i in array.indices) {
        copiedArray[i] = array[i]
    }
    return copiedArray
}
