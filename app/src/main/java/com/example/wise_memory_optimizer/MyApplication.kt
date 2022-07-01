package com.example.wise_memory_optimizer

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.example.wise_memory_optimizer.custom.BroadcastReceiver
import com.example.wise_memory_optimizer.custom.QiscusNumberUtil
import com.example.wise_memory_optimizer.ui.battery.service.HawkHelper
import com.example.wise_memory_optimizer.ui.internet.check.CheckInternetSpeedViewModel
import com.example.wise_memory_optimizer.utils.ChargeUtils
import com.example.wise_memory_optimizer.utils.Const

class MyApplication : AppCompatActivity(), Application.ActivityLifecycleCallbacks {
    fun showInternet() {
        val connManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (mWifi!!.isConnected) {
            HawkHelper.setInternet(true)
        } else {
            HawkHelper.setInternet(false)
        }
    }


    private fun checkWifiOnAndConnected(): Boolean {
        val wifiMgr = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        return if (wifiMgr.isWifiEnabled) { // Wi-Fi adapter is ON
            val wifiInfo = wifiMgr.connectionInfo
            wifiInfo.networkId != -1
            // Connected to an access point
        } else {
            false // Wi-Fi adapter is OFF
        }
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}
    override fun onActivityDestroyed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {} //    public void onBillingBuyFailure(BillingError billingError) {


    companion object {
        var item: String? = null
        var battery_level: String? = null

        const val NOTIFICATION_ID = 103

        /* renamed from: a */ //    public static /* synthetic */ void m28068a(InitializationStatus initializationStatus) {
        //    }
        var appContext: Context? = null
        fun setContext(context: Context?) {
            appContext = context
            initReceiver()
        }

        private fun initReceiver() {
            var batteryReceiver = object : BroadcastReceiver() {
                @SuppressLint("SetTextI18n")
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.action == "android.intent.action.BATTERY_CHANGED") {
                        val temperature: Int = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
                        item = (temperature / 10).toString() + ("°C")
                        battery_level =  intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1).toString() + "%"
                        Log.e("battery_level :", battery_level!!)
                    }
                }
            }
            IntentFilter("android.intent.action.BATTERY_CHANGED").apply {
                addAction("android.intent.action.ACTION_POWER_DISCONNECTED")
                addAction("android.intent.action.ACTION_POWER_CONNECTED")
            }.also {
                (appContext)?.registerReceiver(batteryReceiver, it)
            }
        }

        //        if (!HawkHelper.isBlueTooth() || !ChargeUtils.isBluetoothEnabled(getAppContext())) {
//            return !HawkHelper.isRotate() || ChargeUtils.isRotateOff(getAppContext());
//        }


        @SuppressLint("WrongConstant")
        fun showLowBatteryNotification() {
            val cVar = NotificationCompat.Builder(
                appContext!!
            )
            cVar.setSmallIcon(R.mipmap.ic_launcher)
            //        cVar.setContentTitle((CharSequence) getAppContext().getString(R.string.smart_txt_popup_hint));
//        cVar.setContentText((CharSequence) getAppContext().getString(R.string.charge_txt_fast));
            val intent = Intent(appContext, MainActivity::class.java)
            intent.action = "android.intent.action.MAIN"
            val create = TaskStackBuilder.create(appContext)
            create.addParentStack(MainActivity::class.java)
            create.addNextIntent(intent)
            cVar.setDeleteIntent(create.getPendingIntent(0, 134217728))
            (appContext!!.getSystemService(MediaStore.Audio.AudioColumns.IS_NOTIFICATION) as NotificationManager).notify(
                103,
                cVar.notification
            )
        }

        @SuppressLint("WrongConstant", "RemoteViewLayout")
        fun showNotificationOptimize() {
            val remoteViews = RemoteViews(
                appContext!!.packageName, R.layout.layout_remote_view
            )
            val intent = Intent(appContext, MainActivity::class.java)
            intent.putExtra(Const.EXTRA_NOTIFICATION, Const.EXTRA_NOTIFICATION)
            intent.action = Const.OPTIMIZE_NOTIFIED
            intent.flags = 268435456
            intent.flags = 67108864
            val i: Int =
                if (HawkHelper.isChargerConnected()) R.string.pc_charging_status else R.string.all_charging_problem_was_fixed
            val cVar = NotificationCompat.Builder(
                appContext!!, "noti_my_service"
            )
            cVar.setSmallIcon(R.mipmap.ic_launcher)
            cVar.setAutoCancel(true)
            cVar.setContent(remoteViews)
            var activity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getBroadcast(
                    appContext, QiscusNumberUtil.convertToInt(0),
                    intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
                )
            } else {
                PendingIntent.getBroadcast(
                    appContext, QiscusNumberUtil.convertToInt(0),
                    intent, PendingIntent.FLAG_CANCEL_CURRENT
                )
            }
            cVar.setContentIntent(activity)
//            remoteViews.setTextViewText(R.id.tvNotificationTitle, appContext!!.getString(i))
//            remoteViews.setOnClickPendingIntent(R.id.btnOptimize, activity)
            //        ((NotificationManager) getAppContext().getSystemService(IS_NOTIFICATION)).notify(103, cVar.getNotification());
        }
    }
}