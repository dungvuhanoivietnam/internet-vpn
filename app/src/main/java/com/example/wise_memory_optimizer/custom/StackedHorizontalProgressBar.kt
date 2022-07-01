package com.example.wise_memory_optimizer.custom

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ProgressBar

/**
 * Created by trinhxuantuan on 18/06/2022.
 */

class StackedHorizontalProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ProgressBar(context, attrs, defStyleAttr) {

    private var primaryProgress = 0
    private var maxValue = 0
    private val paint: Paint by lazy { Paint() }

    init {
        paint.color = Color.BLACK
        primaryProgress = 0
        maxValue = 100
    }

    override fun setMax(max: Int) {
        maxValue = max
        super.setMax(max)
    }

    override fun setProgress(progress: Int) {
        var progressValue = progress
        if (progressValue > maxValue) {
            progressValue = maxValue
        }
        primaryProgress = progressValue
        super.setProgress(progressValue)
    }

    override fun setSecondaryProgress(secondaryProgress: Int) {
        var secondaryProgressValue = secondaryProgress
        if (primaryProgress + secondaryProgressValue > maxValue) {
            secondaryProgressValue = maxValue - primaryProgress
        }
        super.setSecondaryProgress(primaryProgress + secondaryProgressValue)
    }
}