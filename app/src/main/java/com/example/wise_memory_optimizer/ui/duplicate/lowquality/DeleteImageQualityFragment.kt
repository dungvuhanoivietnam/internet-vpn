package com.example.wise_memory_optimizer.ui.duplicate.lowquality

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.constants.Constants
import com.example.wise_memory_optimizer.custom.roundimage.RoundedImageView
import com.example.wise_memory_optimizer.databinding.FragmentDeleteImageLowQualityBinding
import com.example.wise_memory_optimizer.model.file.LowQualityFile
import com.example.wise_memory_optimizer.utils.NavigationUtils
import com.example.wise_memory_optimizer.utils.StorageUtils
import com.google.android.material.card.MaterialCardView
import java.io.File


class DeleteImageQualityFragment : Fragment() {

    private var _binding: FragmentDeleteImageLowQualityBinding? = null
    private var lstChecked: MutableList<LowQualityFile?> = ArrayList()
    private val binding get() = _binding!!

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeleteImageLowQualityBinding.inflate(inflater, container, false)
        val root: View = binding.root


        var listLowQualitys =
            arguments?.getSerializable(Constants.KEY_LOW_QUALITY) as MutableList<LowQualityFile?>

        var mapListFile = StorageUtils.mapLowQualityFile(listLowQualitys)

        if (mapListFile.isEmpty()) {
            binding.tvData.visibility = View.VISIBLE
            binding.llToolbar.btnDelete.isEnabled = false
            context?.resources?.getColor(R.color.color_gray_555)
                ?.let { binding.llToolbar.btnDelete.setTextColor(it) }
        } else {
            binding.tvData.visibility = View.GONE
            context?.resources?.getColor(R.color.red)
                ?.let { binding.llToolbar.btnDelete.setTextColor(it) }
            binding.llToolbar.btnDelete.isEnabled = true
        }

        var deleteLowQualityAdapter = context?.let {
            lstChecked.clear()
            DeleteImageLowQualityAdapter(mapListFile, { f ->
                lstChecked.addAll(mapListFile[f?.dateTime]?.filter { s -> s!!.isSelected } as MutableList<LowQualityFile?>)

            }, it)
        }

        binding.rvLowQualityPhoto.adapter = deleteLowQualityAdapter

        binding.llToolbar.toolbarTitle.text = getString(R.string.low_quality)
        binding.llToolbar.btnDelete.visibility = View.VISIBLE

        binding.llToolbar.ivBack.setOnClickListener {
            NavigationUtils.back(binding.llToolbar.ivBack)
        }

        showAllLowQuality(
            listLowQualitys,
            binding.rvImage1,
            binding.rvImage2,
            binding.rvImage3,
            binding.rvImage4,
            binding.tvTotalLowQualityPhoto,
            binding.cardImage3,
            binding.cardImage4
        )


        binding.llToolbar.btnDelete.setOnClickListener {
            Log.e("listCheck", lstChecked.toString())
            lstChecked.forEach { item ->
                val listFile = mapListFile[item?.dateTime]?.toMutableList() ?: mutableListOf()
                val newListFile = mapListFile[item?.dateTime]?.toMutableList() ?: mutableListOf()

                for (itemFile in listFile) {
                    if (item?.file?.path == itemFile?.file?.path) {
                        newListFile.remove(itemFile)
                        deleteFile(itemFile?.file!!)
                    }
                }

                if (newListFile.isNotEmpty()) {
                    mapListFile[item?.dateTime ?: ""] = newListFile
                } else {
                    mapListFile.remove(item?.dateTime ?: 0L)
                }

            }

            deleteLowQualityAdapter?.notifyDataSetChanged()
        }

        return root
    }

    private fun deleteFile(fdelete: File) {
        val myFile = File(fdelete.absolutePath)
        if (myFile.exists()) {
            if (myFile.delete()) {
                Log.e("file Deleted :", myFile.path)
            } else {
                Log.e("file not Deleted :", myFile.path)
            }
        }
    }

    private fun showAllLowQuality(
        dups: List<LowQualityFile?>,
        rv1: RoundedImageView,
        rv2: RoundedImageView,
        rv3: RoundedImageView,
        rv4: RoundedImageView,
        viewTotal: TextView,
        card3: MaterialCardView,
        card4: MaterialCardView
    ) {
        if (dups.isEmpty()) {
            viewTotal.text = "0"
        } else {
            if (dups.size >= 4) {
                context?.let {
                    Glide.with(it)
                        .load(dups[0]!!.file)
                        .into(rv1)

                    Glide.with(it)
                        .load(dups[1]!!.file)
                        .into(rv2)

                    Glide.with(it)
                        .load(dups[2]!!.file)
                        .into(rv3)

                    Glide.with(it)
                        .load(dups[3]!!.file)
                        .into(rv4)

                }
            } else {
                context?.let {
                    Glide.with(it)
                        .load(dups[0]?.file)
                        .into(rv1)

                    Glide.with(it)
                        .load(dups[1]?.file)
                        .into(rv2)
                    card3.visibility = View.GONE
                    card4.visibility = View.GONE
                }
            }

            var lstLenghtDup: MutableList<Long> = ArrayList()

            var d = dups.map { dup ->
                dup?.file?.length()
            } as MutableList<Long>
            lstLenghtDup.addAll(d)


            viewTotal.text =
                StorageUtils.convertBytes(lstLenghtDup.sum())

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}