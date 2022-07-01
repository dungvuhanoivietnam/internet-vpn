package com.example.wise_memory_optimizer.utils

import android.annotation.SuppressLint
import android.icu.text.StringPrepParseException
import android.text.format.DateFormat
import com.example.wise_memory_optimizer.constants.Constants.Companion.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {
    fun convertDayOfMonth(calendar: Calendar): String {
        return calendar[Calendar.DAY_OF_MONTH].toString() + ""
    }

    fun getIdNow(): Long {
        return TimeUnit.MILLISECONDS.toDays(Calendar.getInstance().timeInMillis)
    }

    fun getIdDay(calendar: Calendar): Long {
        return TimeUnit.MILLISECONDS.toDays(calendar.timeInMillis)
    }

    val birthday: Long
        get() {
            val calendar = Calendar.getInstance().clone() as Calendar
            calendar[Calendar.YEAR] = 1990
            calendar[Calendar.MONTH] = 0
            calendar[Calendar.DAY_OF_YEAR] = 1
            return getIdDay(calendar)
        }

    fun convertIdToDate(id: Long): Calendar {
        val calendar = Calendar.getInstance().clone() as Calendar
        calendar.timeInMillis = id * 86400000
        return calendar
    }

    @SuppressLint("SimpleDateFormat")
    fun formatBirthday(id: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return simpleDateFormat.format(convertIdToDate(id).time)
    }

    fun getCurrentTimeStampSecond(): Long {
        return System.currentTimeMillis() / 1000
    }

    fun getDateFormatByLong(dateTime: Long): String {
        return DateFormat.format(DATE_FORMAT, dateTime).toString()
    }
}