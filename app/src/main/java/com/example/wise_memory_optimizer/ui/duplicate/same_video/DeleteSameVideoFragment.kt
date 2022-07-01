package com.example.wise_memory_optimizer.ui.duplicate.same_video

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
import com.example.wise_memory_optimizer.databinding.FragmentDeleteSameVideoBinding
import com.example.wise_memory_optimizer.model.file.Duplicate
import com.example.wise_memory_optimizer.model.file.DuplicateFile
import com.example.wise_memory_optimizer.ui.duplicate.same_photo.DeleteSamePhotoAdapter
import com.example.wise_memory_optimizer.utils.StorageUtils
import com.google.android.material.card.MaterialCardView
import java.io.File


class DeleteSameVideoFragment : Fragment() {

    private var _binding: FragmentDeleteSameVideoBinding? = null
    private var listDupFiles: MutableList<DuplicateFile?> = ArrayList()
    private var lstChecked: MutableList<DuplicateFile?> = ArrayList()
    private val binding get() = _binding!!

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeleteSameVideoBinding.inflate(inflater, container, false)
        val root: View = binding.root


        var listDupVideos =
            arguments?.getSerializable(Constants.KEY_DUPLICATE_VIDEO) as MutableList<Duplicate?>

        listDupFiles.clear()
        for (i in 0 until listDupVideos.size) {
            listDupFiles.addAll(listDupVideos[i]!!.duplicateFiles)
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

        var deleteSameVideoAdapter = context?.let {
            lstChecked.clear()
            DeleteSameVideoAdapter(mapListFile, { f ->
                lstChecked.addAll(mapListFile[f?.dateTime]?.filter { s -> s!!.isSelected } as MutableList<DuplicateFile?>)
            }, it)
        }
        binding.rvSameVideo.adapter = deleteSameVideoAdapter

        binding.llToolbar.toolbarTitle.text = getString(R.string.same_video)
        binding.llToolbar.btnDelete.visibility = View.VISIBLE

        showAllVideoDup(
            listDupVideos,
            binding.thumVideo1,
            binding.thumVideo2,
            binding.thumVideo3,
            binding.thumVideo4,
            binding.tvTotalSameVideo,
            binding.cardVideo3,
            binding.cardVideo4
        )


        binding.llToolbar.btnDelete.setOnClickListener {
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

            deleteSameVideoAdapter?.notifyDataSetChanged()
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

    private fun showAllVideoDup(
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