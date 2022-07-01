package com.example.wise_memory_optimizer.ui.duplicate.same_file

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.ItemSameFileChildrenBinding
import com.example.wise_memory_optimizer.model.file.DuplicateFile
import com.example.wise_memory_optimizer.utils.StorageUtils

class DeleteSameFileChildrenAdapter(
    var listSameFileChilren: MutableList<DuplicateFile?>,
    var onClickItem: (DuplicateFile?) -> Unit,
    var context: Context
) : RecyclerView.Adapter<DeleteSameFileChildrenAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemSameFileChildrenBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = listSameFileChilren.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemSameFileChildrenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        var duplicateFile = listSameFileChilren[position]

        binding.run {
            tvNameFile.text = duplicateFile?.file?.name
            tvFileSize.text = duplicateFile?.file?.length()?.let { StorageUtils.convertBytes(it) }
            binding.ivCheck.setImageDrawable(
                if (duplicateFile!!.isSelected) context.resources.getDrawable(
                    R.drawable.ic_check
                ) else context.resources.getDrawable(R.drawable.ic_uncheck_duplicate)
            )

            ivCheck.setOnClickListener {
                var isCheck = !duplicateFile.isSelected
                if (isCheck) {
                    binding.ivCheck.setImageDrawable(context.resources.getDrawable(R.drawable.ic_check))
                } else {
                    binding.ivCheck.setImageDrawable(context.resources.getDrawable(R.drawable.ic_uncheck_duplicate))
                }
                duplicateFile.isSelected = isCheck
                notifyDataSetChanged()

                onClickItem?.invoke(duplicateFile)
            }

        }

    }
}