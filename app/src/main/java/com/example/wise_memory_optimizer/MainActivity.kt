package com.example.wise_memory_optimizer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.wise_memory_optimizer.custom.MyCustomOnboarder
import com.example.wise_memory_optimizer.databinding.ActivityMainBinding
import com.example.wise_memory_optimizer.ui.battery.PreferenceUtil
import com.example.wise_memory_optimizer.ui.battery.service.PowerService
import com.example.wise_memory_optimizer.utils.ScreenUtils
import com.example.wise_memory_optimizer.utils.SharePreferenceUtils
import com.example.wise_memory_optimizer.utils.SharePrefrenceUtils


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //        ScreenUtils.transparentStatusAndNavigation(this);

        //check open app first time
        val firstTime =  PreferenceUtil.getBoolean(applicationContext,PreferenceUtil.OPEN_APP_FIRST_TIME,true)
        if (firstTime) {
            var onboarding = false
            if (intent != null && intent.extras != null) {
                onboarding = intent.extras!!.getBoolean("onboarding", false)
            }
            if (!onboarding) {
                initOnboarding()
            }
        }

        ScreenUtils.transparentStatusAndNavigation(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MyApplication.setContext(this)
        if (!PowerService.isServiceRunning(applicationContext, PowerService::class.java)) {
            PowerService.startMy(applicationContext)
        }
    }

    private fun initOnboarding() {
        val intent = Intent(
            this,
            MyCustomOnboarder::class.java
        )
        startActivity(intent)
        finish()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (item in  supportFragmentManager.fragments){
            for (mItem in item.childFragmentManager.fragments)
                mItem.onActivityResult(resultCode, resultCode,data)
        }
    }

}