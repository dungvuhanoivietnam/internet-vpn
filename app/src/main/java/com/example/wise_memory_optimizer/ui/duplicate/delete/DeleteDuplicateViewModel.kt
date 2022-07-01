package com.example.wise_memory_optimizer.ui.duplicate.delete

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.wise_memory_optimizer.model.file.Duplicate
import com.example.wise_memory_optimizer.model.file.LowQualityFile
import com.example.wise_memory_optimizer.utils.StorageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DeleteDuplicateViewModel : ViewModel() {
    fun getAllFiles(directory: File): LiveData<List<File>?> = liveData {
        withContext(Dispatchers.IO) {
            StorageUtils.allFileList.clear()
            StorageUtils.loadDirectoryFiles(directory)
        }
        emit(StorageUtils.allFileList)
    }

    fun getDuplicateFiles(f: List<File?>?): LiveData<List<Duplicate?>?> = liveData {
        var files = withContext(Dispatchers.IO) {
            StorageUtils.getDuplicateFiles(f)
        }
        Log.e("dupFiles", files?.size.toString())
        emit(files)
    }

    fun getLowQualityFiles(f: List<File?>?): LiveData<MutableList<LowQualityFile?>?> = liveData {
        var files = withContext(Dispatchers.IO) {
            f?.let { StorageUtils.getLowQualityFiles(it) }
        }
        emit(files)
    }
}