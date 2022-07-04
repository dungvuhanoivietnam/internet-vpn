package com.example.wise_memory_optimizer

import android.app.Application
import com.example.wise_memory_optimizer.ui.battery.service.HawkHelper
import com.orhanobut.hawk.Hawk

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Hawk.init(applicationContext).build()
    }

    companion object{
        @Volatile
        private var instance: App? = null

        @JvmStatic
        fun getInstance(): App = instance ?: synchronized(this) {
            instance ?: App().also {
                instance = it
            }
        }
    }
}