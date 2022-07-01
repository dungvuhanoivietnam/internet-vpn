package com.example.wise_memory_optimizer.ui.duplicate.same_photo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.ItemSamePhotoChildrenBinding
import com.example.wise_memory_optimizer.model.file.DuplicateFile

class DeleteSamePhotoChildrenAdapter(
    var listSamePhotoChilren: MutableList<DuplicateFile?>,
    var onClickItem: (DuplicateFile?) -> Unit,
    var context: Context
) : RecyclerView.Adapter<DeleteSamePhotoChildrenAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemSamePhotoChildrenBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = listSamePhotoChilren.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemSamePhotoChildrenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        var duplicateFile = listSamePhotoChilren[position]

        binding.run {

            Glide.with(context).load(duplicateFile?.file).into(ivSamePhoto)
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