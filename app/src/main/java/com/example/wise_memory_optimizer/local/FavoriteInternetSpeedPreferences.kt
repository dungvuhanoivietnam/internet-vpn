package com.example.wise_memory_optimizer.local

import android.content.Context
import android.content.SharedPreferences
import com.blankj.utilcode.util.LogUtils
import com.example.wise_memory_optimizer.App
import com.example.wise_memory_optimizer.model.location_speed_test.LocationTestingModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object FavoriteInternetSpeedPreferences {

    private const val REFERENCES_NAME = "InternetSpeedPreferences"
    private const val LIST_INTERNET_KEY = "ListInternetSpeed"

    var preferences: SharedPreferences =
        App.getInstance().getSharedPreferences(REFERENCES_NAME, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()

    fun getListSpeedTestFavorite(): List<LocationTestingModel> {
        var items = listOf<LocationTestingModel>()
        preferences.getString(LIST_INTERNET_KEY, null)?.let { jsonObject ->
            val gson = Gson()
            val type = object : TypeToken<List<LocationTestingModel?>?>() {}.type
            items = gson.fromJson(jsonObject, type)
        }

        return items
    }

    fun saveFavoriteInternetSpeed(favoriteSpeedTesting: LocationTestingModel) {
        try {
            val listLocationTestingModel = getListSpeedTestFavorite().toMutableList().apply {
                add(favoriteSpeedTesting)
            }
            val jsonList = Gson().toJson(listLocationTestingModel)
            editor.putString(LIST_INTERNET_KEY, jsonList)
            editor.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteFavoriteInternetSpeed(favoriteSpeedTesting: LocationTestingModel) {
        try {
            getListSpeedTestFavorite().indexOfLast {
                it.roomName == favoriteSpeedTesting.roomName &&
                it.ping == favoriteSpeedTesting.ping &&
                it.downloadPoint == favoriteSpeedTesting.downloadPoint &&
                it.uploadPoint == favoriteSpeedTesting.uploadPoint
            }.also { indexRemove ->
                if (indexRemove == -1){
                    return
                }

                val listLocationTestingModel = getListSpeedTestFavorite().toMutableList().apply {
                    removeAt(indexRemove)
                }
                val jsonList = Gson().toJson(listLocationTestingModel)
                editor.putString(LIST_INTERNET_KEY, jsonList)
                editor.commit()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}