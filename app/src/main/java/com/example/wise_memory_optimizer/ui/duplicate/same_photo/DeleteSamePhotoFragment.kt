package com.example.wise_memory_optimizer.ui.duplicate.same_photo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.constants.Constants
import com.example.wise_memory_optimizer.custom.roundimage.RoundedImageView
import com.example.wise_memory_optimizer.databinding.FragmentDeleteSamePhotoBinding
import com.example.wise_memory_optimizer.model.file.Duplicate
import com.example.wise_memory_optimizer.model.file.DuplicateFile
import com.example.wise_memory_optimizer.ui.duplicate.same_file.DeleteSameFileAdapter
import com.example.wise_memory_optimizer.utils.NavigationUtils
import com.example.wise_memory_optimizer.utils.StorageUtils
import com.google.android.material.card.MaterialCardView
import java.io.File


class DeleteSamePhotoFragment : Fragment() {

    private var _binding: FragmentDeleteSamePhotoBinding? = null
    private var listDupFiles: MutableList<DuplicateFile?> = ArrayList()
    private var lstChecked: MutableList<DuplicateFile?> = ArrayList()

    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeleteSamePhotoBinding.inflate(inflater, container, false)
        val root: View = binding.root


        var listDupImage =
            arguments?.getSerializable(Constants.KEY_DELETE_IMAGE) as MutableList<Duplicate?>

        listDupFiles.clear()
        for (i in 0 until listDupImage.size) {
            listDupFiles.addAll(listDupImage[i]!!.duplicateFiles)
        }

        var mapListFile = StorageUtils.mapDuplicateFile(listDupFiles)

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

        var deleteSamePhotoAdapter = context?.let {
            lstChecked.clear()
            DeleteSamePhotoAdapter(mapListFile, { f ->
                lstChecked.addAll(mapListFile[f?.dateTime]?.filter { s -> s!!.isSelected } as MutableList<DuplicateFile?>)
            }, it)
        }

        binding.llToolbar.ivBack.setOnClickListener {
            NavigationUtils.back(binding.llToolbar.ivBack)
        }

        binding.rvSamePhoto.adapter = deleteSamePhotoAdapter

        binding.llToolbar.toolbarTitle.text = getString(R.string.same_photo)
        binding.llToolbar.btnDelete.visibility = View.VISIBLE

        showAllImageDup(
            listDupImage,
            binding.rvImage1,
            binding.rvImage2,
            binding.rvImage3,
            binding.rvImage4,
            binding.tvTotalSamePhoto,
            binding.cardImage3,
            binding.cardImage4
        )


        binding.llToolbar.btnDelete.setOnClickListener {
            lstChecked.forEach { item ->
                val listFile = mapListFile[item?.dateTime]?.toMutableList() ?: mutableListOf()
                val newListFile = mapListFile[item?.dateTime]?.toMutableList() ?: mutableListOf()

                for (itemFile in listFile) {
                    if (item?.file?.path == itemFile?.file?.path) {
                        newListFile.remove(item)
                        deleteFile(itemFile?.file!!)
                    }
                }

                if (newListFile.isNotEmpty()) {
                    mapListFile[item?.dateTime ?: ""] = newListFile
                } else {
                    mapListFile.remove(item?.dateTime ?: 0L)
                }
            }

            deleteSamePhotoAdapter?.notifyDataSetChanged()
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

    private fun showAllImageDup(
        dups: List<Duplicate?>,
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
            if (dups.size > 1) {
                context?.let {
                    Glide.with(it)
                        .load(dups[0]!!.duplicateFiles[0]?.file)
                        .into(rv1)

                    Glide.with(it)
                        .load(dups[0]!!.duplicateFiles[1]?.file)
                        .into(rv2)

                    Glide.with(it)
                        .load(dups[1]!!.duplicateFiles[0]?.file)
                        .into(rv3)

                    Glide.with(it)
                        .load(dups[1]!!.duplicateFiles[1]?.file)
                        .into(rv4)

                }
            } else {
                context?.let {
                    Glide.with(it)
                        .load(dups[0]!!.duplicateFiles[0]?.file)
                        .into(rv1)

                    Glide.with(it)
                        .load(dups[0]!!.duplicateFiles[1]?.file)
                        .into(rv2)
                    card3.visibility = View.GONE
                    card4.visibility = View.GONE
                }
            }

            var lstLenghtDup: MutableList<Long> = ArrayList()
            for (i in dups.indices) {
                var d = dups[i]!!.duplicateFiles.map { dup ->
                    dup?.file?.length()
                } as MutableList<Long>
                lstLenghtDup.addAll(d)
            }

            viewTotal.text =
                StorageUtils.convertBytes(lstLenghtDup.sum())

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}