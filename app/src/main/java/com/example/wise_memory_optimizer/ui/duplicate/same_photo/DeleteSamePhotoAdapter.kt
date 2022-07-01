package com.example.wise_memory_optimizer.ui.duplicate.same_photo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wise_memory_optimizer.databinding.ItemDeleteSamePhotoBinding
import com.example.wise_memory_optimizer.model.file.DuplicateFile

class DeleteSamePhotoAdapter(
    var listSamePhoto: HashMap<String, MutableList<DuplicateFile?>>,
    var onClickItem: (DuplicateFile?) -> Unit,
    var context: Context
) : RecyclerView.Adapter<DeleteSamePhotoAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ItemDeleteSamePhotoBinding) :
        RecyclerView.ViewHolder(binding.root)

    var deleteSamePhotoChildrenAdapter: DeleteSamePhotoChildrenAdapter? = null


    override fun getItemCount(): Int = listSamePhoto.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemDeleteSamePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        var key = listSamePhoto.keys.toMutableList()[position]

        binding.run {

            tvDate.text = key
            deleteSamePhotoChildrenAdapter =
                listSamePhoto[key]?.let {
                    DeleteSamePhotoChildrenAdapter(it, { f ->
                        onClickItem.invoke(f)
                    }, context)
                }
            rvSamePhotoChilren.adapter = deleteSamePhotoChildrenAdapter

        }

    }
}