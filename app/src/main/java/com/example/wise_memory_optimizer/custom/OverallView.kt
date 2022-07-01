package com.example.wise_memory_optimizer.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.OverallViewBinding
import com.example.wise_memory_optimizer.databinding.RamOptimizerViewBinding

class OverallView : LinearLayout {


    private var _binding: OverallViewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    constructor(context: Context) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        _binding = OverallViewBinding.inflate(LayoutInflater.from(context), this, true)
    }
}