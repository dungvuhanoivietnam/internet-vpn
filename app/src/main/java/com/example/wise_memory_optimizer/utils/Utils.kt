package com.example.wise_memory_optimizer.utils

import android.app.ActivityManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.wise_memory_optimizer.MainActivity
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.ui.battery.BatteryInfo
import com.example.wise_memory_optimizer.ui.battery.PreferenceUtil
import com.example.wise_memory_optimizer.ui.battery.notification.NotificationDevice
import java.text.SimpleDateFormat
import java.util.*


class Utils {
    companion object {
        val DOUBLE_EPSILON = java.lang.Double.longBitsToDouble(1)
        fun getRunningApps(context: Context): HashSet<String>? {
            val hashSet = HashSet<String>()
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val packageManager = context.packageManager
            val runningTasks = activityManager.getRunningTasks(Int.MAX_VALUE)
            for (runningTaskInfo in runningTasks) {
                val packageName = runningTaskInfo.baseActivity!!.packageName
                activityManager.killBackgroundProcesses(packageName)
                try {
                    val appName =
                        packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager)
                            .toString()
                    hashSet.add(appName)
                } catch (exception: PackageManager.NameNotFoundException) {
                    // handle Exception
                }
            }
            return hashSet
        }

        fun getCapacity(context: Context): Int {
            val obj: Any?
            var batteryManager: BatteryManager? = null
            obj = try {
                Class.forName("com.android.internal.os.PowerProfile").getConstructor(
                    *arrayOf<Class<*>>(
                        Context::class.java
                    )
                ).newInstance(*arrayOf<Any>(context))
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            try {
                val doubleValue = (Class.forName("com.android.internal.os.PowerProfile")
                    .getMethod("getAveragePower", String::class.java)
                    .invoke(obj, "battery.capacity") as Double)
                    .toDouble()
                if (doubleValue > 1000.0) {
                    return doubleValue.toInt()
                }
            } catch (e2: Exception) {
                e2.printStackTrace()
            }
            if (Build.VERSION.SDK_INT < 21 || (context.getSystemService("batterymanager") as BatteryManager?).also {
                    batteryManager = it!!
                } == null) {
                return 3000
            }
            val intProperty = batteryManager?.getIntProperty(1)?.toDouble()
            val intProperty2 = batteryManager?.getIntProperty(4)?.toDouble()
            if (intProperty != null) {
                if (intProperty <= Utils.DOUBLE_EPSILON || intProperty2 == -2.147483648E9) {
                    return 3000
                }
            }
            if (intProperty != null) {
                java.lang.Double.isNaN(intProperty)
            }
            if (intProperty2 != null) {
                java.lang.Double.isNaN(intProperty2)
            }
            return (intProperty!! / intProperty2!!).toInt() / 10

        }

        fun getBatteryLevel(context: Context): Int {
            return try {
                val registerReceiver = context.applicationContext.registerReceiver(
                    null as BroadcastReceiver?,
                    IntentFilter("android.intent.action.BATTERY_CHANGED")
                )
                val intExtra = registerReceiver!!.getIntExtra("level", -1)
                val intExtra2 = registerReceiver.getIntExtra("scale", -1)
                if (intExtra == -1 || intExtra2 == -1) {
                    50
                } else (intExtra.toFloat() / intExtra2.toFloat() * 100.0f).toInt()
            } catch (unused: java.lang.Exception) {
                50
            }
        }


        fun getChargeFull(context: Context): Boolean {
            return context.registerReceiver(
                null as BroadcastReceiver?,
                IntentFilter("android.intent.action.BATTERY_CHANGED")
            )!!
                .getIntExtra(NotificationCompat.CATEGORY_STATUS, -1) == 5
        }

        fun intPowerConnected(context: Context) {
            if (SharePreferenceUtils.getInstance(context).fsAutoRun && !getChargeFull(
                    context
                )
            ) {
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(268435456)
                context.startActivity(intent)
            }
            SharePreferenceUtils.getInstance(context).chargeType =
                getChargeType(context)
            SharePreferenceUtils.getInstance(context).levelIn =
                getBatteryLevel(context)
                    .toLong()
            SharePreferenceUtils.getInstance(context).timeIn = System.currentTimeMillis()
            SharePreferenceUtils.getInstance(context).time = 0
        }

        fun getChargeType(context: Context): String? {
            val intExtra = context.registerReceiver(
                null as BroadcastReceiver?,
                IntentFilter("android.intent.action.BATTERY_CHANGED")
            )!!
                .getIntExtra("plugged", -1)
            var z = false
            val z2 = intExtra == 2
            if (intExtra == 1) {
                z = true
            }
            if (z2) {
                return "USB"
            }
            return if (z) "AC" else "AC"
        }


        fun intSound(context: Context?) {
            try {
                RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(2)).play()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        fun fullPower(context: Context?) {
            if (SharePreferenceUtils.getInstance(context).time == 0L) {
                SharePreferenceUtils.getInstance(context).time = System.currentTimeMillis()
                if (checkDNDDoing(context) && SharePreferenceUtils.getInstance(
                        context
                    ).chargeFullReminder
                ) {
                    NotificationDevice.showNotificationBatteryFull(context)
                    intSound(context)
                }
            }
        }

        fun checkDNDDoing(context: Context?): Boolean {
            val instance = SharePreferenceUtils.getInstance(context)
            val instance2 = Calendar.getInstance()
            var i = instance2[11] * 100 + instance2[12]
            val dndStart = instance.dndStart
            var dndStop = instance.dndStop
            if (dndStop < dndStart) {
                dndStop += 2400
            }
            if (i < dndStart) {
                i += 2400
            }
            return i < dndStart || i >= dndStop || !instance.dnd
        }

        fun saveModeLowBattery(context: Context?) {
            if (SharePreferenceUtils.getInstance(context).autoRunSaverMode && SharePreferenceUtils.getInstance(
                    context
                ).flagSaveMode && getBatteryLevel(context!!) <= SharePreferenceUtils.getInstance(
                    context
                ).saveLevel && checkSystemWritePermission(
                    context
                )
            ) {
                val preferenceUtil = PreferenceUtil()
                val batterySaveModeIndex =
                    SharePreferenceUtils.getInstance(context).batterySaveModeIndex
                val listBatterySaver: List<BatteryInfo> =
                    preferenceUtil.getListBatterySaver(context)
                showToastMode(
                    context,
                    batterySaveModeIndex,
                    listBatterySaver[batterySaveModeIndex].title
                )
//                setBatterySaverSelected(
//                    context,
//                    listBatterySaver[batterySaveModeIndex]
//                )
                SharePreferenceUtils.getInstance(context).flagSaveMode = false
            }
        }

        fun checkSystemWritePermission(context: Context?): Boolean {
            return if (Build.VERSION.SDK_INT >= 23) {
                Settings.System.canWrite(context)
            } else true
        }

        fun showToastMode(context: Context, i: Int, str: String) {
            if (i == 0) {
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_mode_title) + " " + context.getString(
                        R.string.super_saving
                    ),
                    1
                ).show()
            } else if (i == 1) {
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_mode_title) + " " + context.getString(
                        R.string.normal
                    ),
                    1
                ).show()
            } else if (i == 2) {
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_mode_title) + " " + context.getString(
                        R.string.custom
                    ),
                    1
                ).show()
            } else if (i == 3) {
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_mode_title) + " " + context.getString(
                        R.string.my_mode
                    ),
                    1
                ).show()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.toast_mode_title) + " " + str,
                    1
                ).show()
            }
        }

        fun isAndroid26(): Boolean {
            return Build.VERSION.SDK_INT >= 26
        }

        fun powerDisconnected(context: Context) {
            SharePreferenceUtils.getInstance(context).flagSaveMode = true
            if (getBatteryLevel(
                    context
                ) == 100
            ) {
                (context.getSystemService("notification") as NotificationManager).cancel(6)
                if (SharePreferenceUtils.getInstance(context).time != 0L) {
                    SharePreferenceUtils.getInstance(context).chargeFull =
                        SimpleDateFormat("HH:mm, d MMM yyyy").format(
                            java.lang.Long.valueOf(
                                SharePreferenceUtils.getInstance(context).time
                            )
                        )
                }
                if (SharePreferenceUtils.getInstance(context).time == 0L || System.currentTimeMillis() - SharePreferenceUtils.getInstance(
                        context
                    ).timeIn <= 1800000 || SharePreferenceUtils.getInstance(context).timeIn == 0L
                ) {
                    SharePreferenceUtils.getInstance(context).chargeHealthy =
                        SharePreferenceUtils.getInstance(context).chargeHealthy + 1
                } else {
                    SharePreferenceUtils.getInstance(context).chargeOver =
                        SharePreferenceUtils.getInstance(context).chargeOver + 1
                }
            } else {
                SharePreferenceUtils.getInstance(context).chargeNormal =
                    SharePreferenceUtils.getInstance(context).chargeNormal + 1
            }
            SharePreferenceUtils.getInstance(context).setLevelOut(getBatteryLevel(
                    context
                ).toLong()
            )
            SharePreferenceUtils.getInstance(context).chargeQuantity =
                SharePreferenceUtils.getInstance(context)
                    .levelOut - SharePreferenceUtils.getInstance(context).levelIn
            if (SharePreferenceUtils.getInstance(context).timeIn != 0L) {
                SharePreferenceUtils.getInstance(context).timeCharge =
                    System.currentTimeMillis() - SharePreferenceUtils.getInstance(context).timeIn
            }
        }
        fun getTimeRemainMls(plugged: Int, capacity: Int, isFullCharging: Boolean): Int {
            return if (plugged == 0 || isFullCharging) {
                if (capacity <= 3100) {
                    return 660000
                }
                if (capacity <= 3300) {
                    return 720000
                }
                if (capacity <= 3500) {
                    return 780000
                }
                if (capacity <= 3700) {
                    return 840000
                }
                if (capacity <= 3900) 900000 else 960000
            } else if (plugged == BatteryManager.BATTERY_PLUGGED_AC) {
                if (capacity <= 3100) {
                    return 80000
                }
                if (capacity <= 3300) {
                    return 85000
                }
                if (capacity <= 3500) {
                    return 90000
                }
                if (capacity <= 3700) {
                    return 95000
                }
                if (capacity <= 3900) 100000 else 105000
            } else if (plugged == BatteryManager.BATTERY_PLUGGED_USB) {
                if (capacity <= 3100) {
                    return 160000
                }
                if (capacity <= 3300) {
                    return 165000
                }
                if (capacity <= 3500) {
                    return 170000
                }
                if (capacity <= 3700) {
                    return 175000
                }
                if (capacity <= 3900) 180000 else 185000
            } else if (plugged != BatteryManager.BATTERY_PLUGGED_WIRELESS) {
                780000
            } else {
                if (capacity <= 3100) {
                    return 120000
                }
                if (capacity <= 3300) {
                    return 125000
                }
                if (capacity <= 3500) {
                    return 130000
                }
                if (capacity <= 3700) {
                    return 135000
                }
                if (capacity <= 3900) 140000 else 145000
            }
        }
        fun openAndroidPermissionsMenu(context: Context) {
            try {
                context.startActivity(
                    Intent(
                        "android.settings.action.MANAGE_WRITE_SETTINGS",
                        Uri.parse("package:" + context.packageName)
                    )
                )
                //            context.startActivity(new Intent(context, PermissionActivity.class));
            } catch (unused: java.lang.Exception) {
            }
        }

    }
}