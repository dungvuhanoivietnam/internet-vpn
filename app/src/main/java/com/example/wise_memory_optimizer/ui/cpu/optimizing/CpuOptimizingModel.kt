package com.example.wise_memory_optimizer.ui.cpu.optimizing

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import com.example.wise_memory_optimizer.ui.battery.service.HawkHelper
import com.example.wise_memory_optimizer.utils.ChargeUtils

class CpuOptimizingModel : ViewModel() {

    @SuppressLint("QueryPermissionsNeeded")
    fun killAllProcess(context: Context){
        val packages: List<ApplicationInfo>
        val pm: PackageManager = context.packageManager
        //get a list of installed apps.
        packages = pm.getInstalledApplications(0)
        val mActivityManager: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val myPackage: String = context.packageName
        for (packageInfo in packages) {
            if (packageInfo.packageName.equals(myPackage)) continue
            mActivityManager.killBackgroundProcesses(packageInfo.packageName)
        }
    }

    fun clearMem(context: Context) {
        if (HawkHelper.isClearMem()) {
            ChargeUtils.clearMem(context)
        }
    }

    fun reduceBrightness(context: Context) {
        if (HawkHelper.isBrightness()) {
//            showTextWhenOptimize(R.string.function_min_brightness);
            ChargeUtils.reduceBrightness(context)
        }
        HawkHelper.setKeyCountRate(HawkHelper.getKeyCountRate() + 1)
    }

    fun offBluetooth(context: Context) {
        if (HawkHelper.isBlueTooth()) {
//            showTextWhenOptimize(R.string.function_off_bluetooth);
            ChargeUtils.offBluetooth(context)
        }
    }

    fun offRotate(context: Context) {
        if (HawkHelper.isRotate()) {
//            showTextWhenOptimize(R.string.function_off_rotate);
            ChargeUtils.offRotate(context)
        }
    }
}