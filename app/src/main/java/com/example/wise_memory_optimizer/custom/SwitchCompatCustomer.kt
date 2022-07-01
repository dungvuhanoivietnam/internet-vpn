package com.example.wise_memory_optimizer.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.LayoutSwitchCompatBinding

class SwitchCompatCustomer(context: Context, private var attrs: AttributeSet) : ConstraintLayout(context,attrs) {

    private var _binding: LayoutSwitchCompatBinding? = null
    private val binding get() = _binding!!
    private var listener : Listener?= null

    fun setListener(listen : Listener){
        listener = listen
    }

    init {
        _binding = LayoutSwitchCompatBinding.inflate(LayoutInflater.from(context), this, true)
        initView()
    }

     fun checked(boolean: Boolean){
        binding.switchCompat.isChecked = boolean
    }

     fun getChecked() : Boolean{
         return binding.switchCompat.isChecked
     }

    private fun initView() {
        val typedArray: TypedArray = context
            .obtainStyledAttributes(attrs, R.styleable.SwitchCompatCustomer)

        val title = typedArray.getString(R.styleable. SwitchCompatCustomer_txt_title)

        binding.txtTitle.text = title

        binding.switchCompat.setOnClickListener {
            listener?.onClick()
        }
        typedArray.recycle()
    }

    interface Listener{
        fun onClick()
    }
}

