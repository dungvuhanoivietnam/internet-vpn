package com.example.wise_memory_optimizer.ui.menu.preference

import android.content.Context

class SharedPreferenceUtils(private var context: Context) {

    fun saveSetting(key : String, value : String){
        val sharedPreference =  context.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("username","Anupam")
        editor.putLong("l",100L)
        editor.apply()
    }

    fun getSetting(key : String , value : String){
        val sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE)

    }
}