package com.example.wise_memory_optimizer.ui.duplicate.lowquality

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wise_memory_optimizer.databinding.ItemDeleteSamePhotoBinding
import com.example.wise_memory_optimizer.model.file.LowQualityFile

class DeleteImageLowQualityAdapter(
    var listLowQualityPhoto: HashMap<String, MutableList<LowQualityFile?>>,
    var onClickItem: (LowQualityFile?) -> Unit,
    var context: Context
) : RecyclerView.Adapter<DeleteImageLowQualityAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ItemDeleteSamePhotoBinding) :
        RecyclerView.ViewHolder(binding.root)

    var deleteLowQualityPhotoChildrenAdapter: DeleteImageQualityChildrenAdapter? = null

    override fun getItemCount(): Int = listLowQualityPhoto.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemDeleteSamePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        var key = listLowQualityPhoto.keys.toMutableList()[position]

        binding.run {
            tvDate.text = key
            deleteLowQualityPhotoChildrenAdapter =
                listLowQualityPhoto[key]?.let {
                    DeleteImageQualityChildrenAdapter(it, { f ->
                        onClickItem.invoke(f)
                    }, context)
                }
            rvSamePhotoChilren.adapter = deleteLowQualityPhotoChildrenAdapter

        }

    }
}