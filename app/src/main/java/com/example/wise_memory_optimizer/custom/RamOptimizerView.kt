package com.example.wise_memory_optimizer.custom

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.wise_memory_optimizer.databinding.RamOptimizerViewBinding
import com.example.wise_memory_optimizer.tasks.TotalRamTask
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.Viewport
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class RamOptimizerView : LinearLayout {
    private var _binding: RamOptimizerViewBinding? = null
    var chart: GraphView? = null
    var series: LineGraphSeries<DataPoint>? = null
    var lastX = 0.0
    private var mCleanerService: CleanerService? = null
    var clearCacheListener: ClearCacheListener? = null
        get() = field
        set(value) {
            field = value
        }
    private val CACHE_APP = Long.MAX_VALUE


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    constructor(context: Context) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        _binding = RamOptimizerViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    interface ClearCacheListener {
        fun clearCache()
    }



    fun updateChartView() {

    }




}

