package com.example.wise_memory_optimizer.ui.internet.list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.ItemLocationCheckedBinding
import com.example.wise_memory_optimizer.model.location_speed_test.LocationTestingModel
import com.example.wise_memory_optimizer.utils.showStateTesting

class InternetSpeedAdapter(
    var listLocationChecked: MutableList<LocationTestingModel>,
    var onClickEditName: (LocationTestingModel, Int , view : View) -> Unit,
    var onClickFavorite: (LocationTestingModel, Boolean, Int) -> Unit
) : RecyclerView.Adapter<InternetSpeedAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ItemLocationCheckedBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = listLocationChecked.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemLocationCheckedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val locationCheckedModel = listLocationChecked[position]
        var isFavorite = locationCheckedModel.isFavorite

        binding.run {
            txtRoomName.text = locationCheckedModel.roomName
            txtTime.text = locationCheckedModel.getDateFormat()
            locationCheckedModel.ping?.let { ping ->
                txtPing.text = "${ping.toInt()} ms"
                imgPingState.showStateTesting(ping)
            }

            if (isFavorite) {
                imgFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context,
                        R.drawable.ic_star_active
                    )
                )
            } else {
                imgFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context,
                        R.drawable.ic_star_inactive
                    )
                )
            }

            imgEdit.setOnClickListener {
                onClickEditName.invoke(locationCheckedModel,position,binding.root)
            }

            imgFavorite.setOnClickListener {
                isFavorite = !isFavorite
                onClickFavorite.invoke(locationCheckedModel, isFavorite,position)
            }
        }

    }
}