package com.example.wise_memory_optimizer.ui.menu.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.wise_memory_optimizer.databinding.DialogAutomaticOptiomizationBinding

class DialogAutomaticOptimizationMode(
    internal var context: Context, val width: Int, var title : String?
) : Dialog(context) {

    val binding = DialogAutomaticOptiomizationBinding.inflate(LayoutInflater.from(context))

    init {
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.label.text = title
        binding.txtCancel.setOnClickListener {
            dismiss()
        }
    }
}
