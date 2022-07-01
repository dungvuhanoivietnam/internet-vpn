package com.example.wise_memory_optimizer.ui.duplicate.same_file

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.constants.Constants
import com.example.wise_memory_optimizer.databinding.FragmentDeleteSameFileBinding
import com.example.wise_memory_optimizer.model.file.Duplicate
import com.example.wise_memory_optimizer.model.file.DuplicateFile
import com.example.wise_memory_optimizer.model.file.LowQualityFile
import com.example.wise_memory_optimizer.ui.duplicate.lowquality.DeleteImageLowQualityAdapter
import com.example.wise_memory_optimizer.utils.NavigationUtils
import com.example.wise_memory_optimizer.utils.StorageUtils
import java.io.File


class DeleteSameFileFragment : Fragment() {

    private var _binding: FragmentDeleteSameFileBinding? = null
    private var fileDupList: MutableList<DuplicateFile?> = ArrayList()
    private var lstChecked: MutableList<DuplicateFile?> = ArrayList()
    private val binding get() = _binding!!

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeleteSameFileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var listDupFiles =
            arguments?.getSerializable(Constants.KEY_DUPLICATE_FILES) as MutableList<Duplicate?>

        fileDupList.clear()
        for (i in 0 until listDupFiles.size) {
            fileDupList.addAll(listDupFiles[i]!!.duplicateFiles)
        }

        var mapListFile = StorageUtils.mapDuplicateFile(fileDupList)
        if (mapListFile.isEmpty()) {
            binding.tvData.visibility = VISIBLE
            binding.llToolbar.btnDelete.isEnabled = false
            context?.resources?.getColor(R.color.color_gray_555)
                ?.let { binding.llToolbar.btnDelete.setTextColor(it) }
        } else {
            binding.tvData.visibility = GONE
            context?.resources?.getColor(R.color.red)
                ?.let { binding.llToolbar.btnDelete.setTextColor(it) }
            binding.llToolbar.btnDelete.isEnabled = true
        }

        var deleteSameFileAdapter = context?.let {
            lstChecked.clear()
            DeleteSameFileAdapter(mapListFile, { f ->
                lstChecked.addAll(mapListFile[f?.dateTime]?.filter { s -> s!!.isSelected } as MutableList<DuplicateFile?>)

            }, it)
        }

        binding.rvSameFiles.adapter = deleteSameFileAdapter
        binding.llToolbar.toolbarTitle.text = getString(R.string.same_file)
        binding.llToolbar.btnDelete.visibility = View.VISIBLE

        binding.llToolbar.ivBack.setOnClickListener {
            NavigationUtils.back(binding.llToolbar.ivBack)
        }

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

            deleteSameFileAdapter?.notifyDataSetChanged()
        }

        showAllFilesDup(listDupFiles, binding.tvTotalSize)

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

    private fun showAllFilesDup(
        dups: List<Duplicate?>,
        viewTotal: TextView
    ) {
        if (dups.isNotEmpty()) {
            var lstLenghtDup: MutableList<Long> = ArrayList()
            for (i in dups.indices) {
                var d = dups[i]!!.duplicateFiles.map { dup ->
                    dup?.file?.length()
                } as MutableList<Long>
                lstLenghtDup.addAll(d)
            }

            viewTotal.text =
                StorageUtils.convertBytes(lstLenghtDup.sum())
        } else {
            viewTotal.text = "0"
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}