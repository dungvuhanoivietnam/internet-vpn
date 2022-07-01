package com.example.wise_memory_optimizer.ui.duplicate.same_file

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wise_memory_optimizer.databinding.ItemDeleteSameFileBinding
import com.example.wise_memory_optimizer.model.file.DuplicateFile

class DeleteSameFileAdapter(
    var listSameFile: HashMap<String, MutableList<DuplicateFile?>>,
    var onClickItem: (DuplicateFile?) -> Unit,
    var context: Context
) : RecyclerView.Adapter<DeleteSameFileAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ItemDeleteSameFileBinding) :
        RecyclerView.ViewHolder(binding.root)

    var deleteSameFileChildrenAdapter: DeleteSameFileChildrenAdapter? = null


    override fun getItemCount(): Int = listSameFile.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemDeleteSameFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        var key = listSameFile.keys.toMutableList()[position]

        binding.run {

            tvDate.text = key
            deleteSameFileChildrenAdapter =
                listSameFile[key]?.let {
                    DeleteSameFileChildrenAdapter(it, { f ->
                        onClickItem.invoke(f)
                    }, context)
                }
            rvSameFileChildren.adapter = deleteSameFileChildrenAdapter

        }

    }
}