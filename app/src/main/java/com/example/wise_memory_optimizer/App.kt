package com.example.wise_memory_optimizer

import android.app.Application
import com.example.wise_memory_optimizer.ui.battery.service.HawkHelper
import com.orhanobut.hawk.Hawk

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(applicationContext).build()
    }
}