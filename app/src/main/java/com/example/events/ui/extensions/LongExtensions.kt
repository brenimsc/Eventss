package com.example.events.ui.extensions

import android.content.Context
import com.example.events.R
import com.example.events.constants.Constants.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*


fun Long.formateDate(context: Context): String {
    val dateFormatted = SimpleDateFormat(DATE_FORMAT)
    val date = Date(this)
    return context.getString(
        R.string.date_detail,
        dateFormatted.format(date).capitalize().replace(".", "")
    )
}