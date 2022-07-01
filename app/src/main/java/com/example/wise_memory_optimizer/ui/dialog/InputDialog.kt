package com.example.wise_memory_optimizer.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.example.wise_memory_optimizer.databinding.DialogInputBinding

class InputDialog(
    context: Context,
    var title: String? = null,
    var positiveText: String? = null,
    var onPositiveClick : ((String) -> Unit)? = null,
    var negativeText: String? = null,
) : Dialog(context) {

    lateinit var binding: DialogInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        binding = DialogInputBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        title?.let {
            binding.txtTitle.text = title
        }

        positiveText?.let {
            binding.btnOk.text = it
        }
        binding.btnOk.setOnClickListener {
            onPositiveClick?.invoke(binding.edtInput.text.toString())
            dismiss()
        }

        negativeText?.let {
            binding.btnCancel.text = it
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

}