package com.example.wise_memory_optimizer

import androidx.lifecycle.ViewModel
import com.example.wise_memory_optimizer.local.FavoriteInternetSpeedPreferences
import com.example.wise_memory_optimizer.model.location_speed_test.LocationTestingModel

class MainViewModel : ViewModel() {
    val listRecentlyTesting = mutableListOf<LocationTestingModel>()
    private val listFavoriteTesting = mutableListOf<LocationTestingModel>()
    var resultTestingModel: LocationTestingModel? = null

    fun addRecentTestingModel(data: LocationTestingModel) {
        resultTestingModel = data
        listRecentlyTesting.add(data)
    }

    fun deleteRecentTestingModel(data: LocationTestingModel) {
        resultTestingModel = data
        listRecentlyTesting.remove(data)
    }

    fun getListFavoriteTesting(): MutableList<LocationTestingModel> {
        return FavoriteInternetSpeedPreferences.getListSpeedTestFavorite().toMutableList()
    }

    fun addFavoriteInternetTesting(locationTestingModel: LocationTestingModel) {
        FavoriteInternetSpeedPreferences.saveFavoriteInternetSpeed(locationTestingModel)
    }

    fun deleteFavoriteInternetTesting(locationTestingModel: LocationTestingModel) {
        FavoriteInternetSpeedPreferences.deleteFavoriteInternetSpeed(locationTestingModel)
    }
}