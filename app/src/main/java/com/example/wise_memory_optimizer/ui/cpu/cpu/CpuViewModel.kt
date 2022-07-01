package com.example.wise_memory_optimizer.ui.cpu.cpu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wise_memory_optimizer.model.cpu.CpuInfo
import kotlinx.coroutines.*


class CpuViewModel : ViewModel() {

    private val _cpuUsedPercent = MutableLiveData<Int>()
    val cpuUsedPercent: LiveData<Int> = _cpuUsedPercent

    private var jobCheckCPU: Job? = null

    companion object {
        const val TIME_CHECK_CPU = 500L
    }

    fun startCheckCpu() {
        jobCheckCPU = viewModelScope.launch {
            while (isActive) {
                val cpuTotal = CpuInfo.cpuUsageFromFreq
                _cpuUsedPercent.postValue(cpuTotal)
                delay(TIME_CHECK_CPU)
            }
        }
    }

    override fun onCleared() {
        jobCheckCPU?.cancel()
        super.onCleared()
    }
}