package com.example.wise_memory_optimizer.model.location_speed_test

import android.text.format.DateFormat
import com.example.wise_memory_optimizer.utils.DateUtils
import java.io.Serializable
import java.util.*

data class LocationTestingModel(
    var roomName: String? = null,
    var isFavorite : Boolean = false,
    var ping : Float? = null,
    var downloadPoint: Float? = null,
    var uploadPoint: Float? = null,
    var timestamp: Long = DateUtils.getCurrentTimeStampSecond(),
) : Serializable {

    fun getDateFormat(): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        return DateFormat.format(DATE_FORMAT, calendar).toString()
    }

    companion object{
        const val DATE_FORMAT = "HH:mm dd/MM/yyyy"
    }
}