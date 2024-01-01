package com.huhn.architecturetemplate.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity


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
