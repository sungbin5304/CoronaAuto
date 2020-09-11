package me.sungbin.spakchat.util

import android.widget.EditText
import java.util.*


/**
 * Created by SungBin on 2020-09-11.
 */

fun Date.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar
}

fun EditText.isBlank() = this.text.toString().isBlank()