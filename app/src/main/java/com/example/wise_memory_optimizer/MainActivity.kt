package com.example.wise_memory_optimizer

import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.wise_memory_optimizer.databinding.ActivityMainBinding
import com.example.wise_memory_optimizer.ui.battery.PreferenceUtil
import com.example.wise_memory_optimizer.ui.battery.service.PowerService
import com.example.wise_memory_optimizer.utils.ScreenUtils


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ScreenUtils.transparentStatusAndNavigation(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MyApplication.setContext(this)
        if (!PowerService.isServiceRunning(applicationContext, PowerService::class.java)) {
            PowerService.startMy(applicationContext)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSupportNavigateUp(): Boolean {
        return false
    }

    override fun attachBaseContext(newBase: Context?) {
        val language = PreferenceUtil.getString(newBase, PreferenceUtil.SETTING_ENGLISH,"")
        super.attachBaseContext(MyContextWrapper.wrap(newBase,language));
    }

}