package com.example.wise_memory_optimizer.utils

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.wise_memory_optimizer.R
import de.hdodenhof.circleimageview.CircleImageView
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

fun BigDecimal.toMbps(): Float {
    val result = (this.toFloat()/1024)/1024

    return try {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        df.format(result).toFloat()
    }catch (e : Exception){
        result
    }
}

fun Float.formatMbps(): String{
    return try {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        df.format(this).toString()
    }catch (e : Exception){
        this.toString()
    }
}

fun TextView.setTextPing(ping : Float){
    val colorResource = when (ping) {
        in 1F..50F ->  R.color.core_green
        in 50F..80F -> R.color.color_yellowff
        in 80F..99999F -> R.color.color_red_status
        else -> R.color.core_green
    }

    this.setTextColor(ContextCompat.getColor(context,colorResource))
    this.text = ping.toInt().toString()
}

fun CircleImageView.showStateTesting(ping: Float){
    val colorResource = when (ping) {
        in 1F..50F ->  R.color.core_green
        in 50F..80F -> R.color.color_yellowff
        in 80F..99999F -> R.color.color_red_status
        else -> R.color.core_green
    }

    this.setImageResource(colorResource)
}
