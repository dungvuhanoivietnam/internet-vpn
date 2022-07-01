package com.example.wise_memory_optimizer.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.LayoutToolbarBinding

class ToolbarCustomer(context: Context, private var attrs: AttributeSet) : ConstraintLayout(context,attrs) {

    private var _binding: LayoutToolbarBinding? = null
    private val binding get() = _binding!!
    private var listener : Listener?= null

    fun setListener(listen : Listener){
        listener = listen
    }

    init {
        _binding = LayoutToolbarBinding.inflate(LayoutInflater.from(context), this, true)
        initView()
    }

    private fun initView() {
        val typedArray: TypedArray = context
            .obtainStyledAttributes(attrs, R.styleable.ToolbarCustomer)

        val title = typedArray.getString(R.styleable.ToolbarCustomer_txt_toolbar)

        binding.title.text = title

        binding.root.setOnClickListener {
            listener?.onClick()
        }
        typedArray.recycle()
    }

    interface Listener{
        fun onClick()
    }
}

