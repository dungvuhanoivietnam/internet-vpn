package com.example.wise_memory_optimizer.ui.duplicate.same_video

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wise_memory_optimizer.databinding.ItemDeleteSameVideoBinding
import com.example.wise_memory_optimizer.model.file.DuplicateFile

class DeleteSameVideoAdapter(
    var listSameVideo: HashMap<String, MutableList<DuplicateFile?>>,
    var onClickItem: (DuplicateFile?) -> Unit,
    var context: Context
) : RecyclerView.Adapter<DeleteSameVideoAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ItemDeleteSameVideoBinding) :
        RecyclerView.ViewHolder(binding.root)

    var deleteSameVideoChildrenAdapter: DeleteSameVideoChildrenAdapter? = null


    override fun getItemCount(): Int = listSameVideo.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemDeleteSameVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        var key = listSameVideo.keys.toMutableList()[position]

        binding.run {

            tvDate.text = key
            deleteSameVideoChildrenAdapter =
                listSameVideo[key]?.let {
                    DeleteSameVideoChildrenAdapter(it, { f ->
                        onClickItem.invoke(f)
                    }, context)
                }
            rvSameVideoChilren.adapter = deleteSameVideoChildrenAdapter

        }

    }
}