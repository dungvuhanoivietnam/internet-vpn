package com.example.wise_memory_optimizer.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.LayoutExceedsBinding
import com.example.wise_memory_optimizer.databinding.LayoutSwitchCompatBinding

class SeekBarCompatCustomer(context: Context, private var attrs: AttributeSet) : ConstraintLayout(context,attrs) {

    private var _binding: LayoutExceedsBinding? = null
    private val binding get() = _binding!!
    private var listener : Listener?= null

    fun setListener(listen : Listener){
        listener = listen
    }

    init {
        _binding = LayoutExceedsBinding.inflate(LayoutInflater.from(context), this, true)
        initView()
    }

    private fun initView() {
        val typedArray: TypedArray = context
            .obtainStyledAttributes(attrs, R.styleable.SeekBarCompatCustomer)

        val title = typedArray.getString(R.styleable.SeekBarCompatCustomer_txt_seekbar)

        binding.label.text = title

//        binding.switchCompat.setOnClickListener {
//            listener?.onClick()
//        }

        binding.seekBarLuminosite.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                binding.txtPercentage.text = "${i}%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
        typedArray.recycle()
    }

    fun getProgress() : Int{
        return binding.seekBarLuminosite.progress
    }

    @SuppressLint("SetTextI18n")
    fun setProgress(progress : Int){
        binding.txtPercentage.text = "${progress}%"
        binding.seekBarLuminosite.progress = progress
    }

    interface Listener{
        fun onClick()
    }
}

