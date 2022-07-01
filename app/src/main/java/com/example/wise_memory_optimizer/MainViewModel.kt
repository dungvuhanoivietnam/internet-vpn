package com.example.wise_memory_optimizer

import androidx.lifecycle.ViewModel
import com.example.wise_memory_optimizer.model.location_speed_test.LocationTestingModel

class MainViewModel : ViewModel() {
    val listSpeedTest = mutableListOf<LocationTestingModel>()
    var resultTestingModel : LocationTestingModel? = null

    fun saveResultTestingModel(data : LocationTestingModel){
        resultTestingModel = data
        listSpeedTest.add(data)
    }
}