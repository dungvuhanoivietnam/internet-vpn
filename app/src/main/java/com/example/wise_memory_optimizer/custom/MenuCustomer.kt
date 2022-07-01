package com.example.wise_memory_optimizer.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.LayoutMenuBinding

class MenuCustomer(context: Context, private var attrs: AttributeSet) : ConstraintLayout(context,attrs) {

    private var _binding: LayoutMenuBinding? = null
    private val binding get() = _binding!!
    private var listener : Listener?= null

    fun setListener(listen : Listener){
        listener = listen
    }

    init {
        _binding = LayoutMenuBinding.inflate(LayoutInflater.from(context), this, true)
        initView()
    }

    private fun initView() {
        val typedArray: TypedArray = context
            .obtainStyledAttributes(attrs, R.styleable.MenuCustomer)

        val title = typedArray.getString(R.styleable.MenuCustomer_title)

        when(title){
            "Home" -> {
                binding.iv.setImageResource(R.drawable.ic_group_39572)
            }
            "Auto Optimize" -> {
                binding.iv.setImageResource(R.drawable.ic_outline_motion_photos_auto)
            }

            "Notification" -> {
                binding.iv.setImageResource(R.drawable.ic_ph_notification_fill)
                binding.switchCompat.visibility = VISIBLE
                binding.ivAr.visibility = GONE

            }

            "Language" -> {
                binding.iv.setImageResource(R.drawable.ic_translate_2_1)

            }
            "Privacy" -> {
                binding.iv.setImageResource(R.drawable.ic_shield)
            }
            "About App" -> {
                binding.iv.setImageResource(R.drawable.ic_info)
            }
        }

        binding.txtTitle.text = title

        binding.root.setOnClickListener {
            listener?.onClick()
        }
        typedArray.recycle()
    }

    interface Listener{
        fun onClick()
    }
}

