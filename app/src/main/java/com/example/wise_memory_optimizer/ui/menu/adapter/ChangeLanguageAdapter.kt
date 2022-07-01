package com.example.wise_memory_optimizer.ui.menu.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.ItemLanguageBinding

class ChangeLanguageAdapter(
    var context: Context,
    var onClickItem: (Language) -> Unit,
) : RecyclerView.Adapter<ChangeLanguageAdapter.ViewHolder>() {

    private var listLanguage = ArrayList<Language>()

    fun updateData(list: ArrayList<Language>) {
        listLanguage.clear()
        listLanguage.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(var binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = listLanguage.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val item = listLanguage[position]

        binding.tvEnglish.text = item.language

        binding.rootView.setBackgroundColor(
            if (item.selected) ContextCompat.getColor(
                context,
                R.color.black
            ) else ContextCompat.getColor(context, R.color.color_5252)
        )

        binding.tvEnglish.setTextColor(
            if (item.selected) ContextCompat.getColor(context, R.color.core_green)
            else ContextCompat.getColor(context, R.color.white)
        )

        binding.rootView.setOnClickListener {
            updateView(position, item)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateView(index: Int, language: Language) {
        listLanguage.forEach { item ->
            item.selected = false
        }
        onClickItem.invoke(language)
        language.selected = !(language.selected)
        notifyDataSetChanged()
    }

}

data class Language(var language: String?, var selected: Boolean, var value : String?= "")