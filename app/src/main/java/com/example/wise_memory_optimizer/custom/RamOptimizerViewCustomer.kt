package com.example.wise_memory_optimizer.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.LayoutRamOptimizerBinding
import com.example.wise_memory_optimizer.tasks.TotalRamTask

class RamOptimizerViewCustomer(context: Context, private var attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    private var _binding: LayoutRamOptimizerBinding? = null
    private val binding get() = _binding!!
    private var title : String?= null

    init {
        _binding = LayoutRamOptimizerBinding.inflate(LayoutInflater.from(context), this, true)
        initView()
    }

    private fun initView() {
        val typedArray: TypedArray = context
            .obtainStyledAttributes(attrs, R.styleable.RamOptimizerViewCustomer)

        title = typedArray.getString(R.styleable.RamOptimizerViewCustomer_txt_option)
        binding.title.text = title

        when (title) {
            "RAM" -> {
                loadRam()
            }
            "CPU" -> {
                loadRam()
            }
            "PIN" -> {
                loadRam()
            }
        }
        typedArray.recycle()
    }

    private fun loadRam() {
        val totalRamTask = TotalRamTask(context, object : TotalRamTask.IRamTaskListener {

            @SuppressLint("SetTextI18n")
            override fun onGetRamDataCompleted(
                freeRam: String?,
                totalRam: String?,
                percentRam: Int
            ) {
                binding.progressRam.progress = percentRam
                binding.txtPercentageRam.text = "${percentRam}%"

                // fake
                when (title) {
                    "RAM" -> {
                        binding.progressRam.progress = percentRam
                        binding.txtPercentageRam.text = "${percentRam}%"
                    }
                    "CPU" -> {
                        binding.progressRam.progress = 35
                        binding.txtPercentageRam.text = "${35}%"
                    }
                    "PIN" -> {
                        binding.progressRam.progress = 20
                        binding.txtPercentageRam.text = "${20}%"
                    }
                }
                // end fake
                Log.d("AAAAAAAAAAAAA", "${freeRam} -- ${totalRam} --- ${percentRam}")
            }

            @SuppressLint("SetTextI18n")
            override fun onRamPercentUpdate(percentRam: Int) {
                binding.progressRam.progress = percentRam
                binding.txtPercentageRam.text = "${percentRam}%"
            }
        })
        totalRamTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }
}

