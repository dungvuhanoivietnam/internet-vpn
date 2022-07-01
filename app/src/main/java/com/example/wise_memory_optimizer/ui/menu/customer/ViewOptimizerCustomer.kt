package com.example.wise_memory_optimizer.ui.menu.customer

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.LayoutRamBinding
import com.example.wise_memory_optimizer.tasks.TotalRamTask

class ViewOptimizerCustomer(context: Context, private var attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    private var _binding: LayoutRamBinding? = null
    private val binding get() = _binding!!
    private var title: String? = null

    init {
        _binding = LayoutRamBinding.inflate(LayoutInflater.from(context), this, true)
        initView()
    }

    private fun initView() {
        val typedArray: TypedArray = context
            .obtainStyledAttributes(attrs, R.styleable.ViewOptimizerCustomer)

        title = typedArray.getString(R.styleable.ViewOptimizerCustomer_txt_title_remote)
        binding.title.text = title

        when (title) {
            context.getString(R.string.txt_option_ram) -> {
                loadRam()
                ContextCompat.getDrawable(context, R.drawable.progressbar_bg)?.let {
                    setProgressDrawable(it)
                }
            }
            context.getString(R.string.txt_option_cpu) -> {
                loadRam()
                ContextCompat.getDrawable(context, R.drawable.progressbar_bg_cpu)?.let {
                    setProgressDrawable(it)
                }
            }
            context.getString(R.string.txt_option_pin) -> {
                loadRam()
                ContextCompat.getDrawable(context, R.drawable.progressbar_bg)?.let {
                    setProgressDrawable(it)
                }
            }
            context.getString(R.string.txt_option_pin) -> {
                loadRam()
                ContextCompat.getDrawable(context, R.drawable.progressbar_bg)?.let {
                    setProgressDrawable(it)
                }
            }

            context.getString(R.string.txt_option_kbs) -> {
                loadRam()
                ContextCompat.getDrawable(context, R.drawable.progressbar_bg_kbs)?.let {
                    setProgressDrawable(it)
                }
            }
        }
        typedArray.recycle()
    }

    private fun setProgressDrawable(d: Drawable) {
        binding.progressRam.progressDrawable = d
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

