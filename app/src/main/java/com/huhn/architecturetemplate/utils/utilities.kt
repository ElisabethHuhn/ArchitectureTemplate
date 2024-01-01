package com.huhn.architecturetemplate.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

//const val NUMBER_OF_FILE_ENTRIES = 20000
//const val LOW_BRIGHTNESS = 10
//const val HIGH_BRIGHTNESS = 250

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

fun deepCopyFloatArray(array: FloatArray): FloatArray {
    val copiedArray = FloatArray(array.size)
    for (i in array.indices) {
        copiedArray[i] = array[i]
    }
    return copiedArray
}
