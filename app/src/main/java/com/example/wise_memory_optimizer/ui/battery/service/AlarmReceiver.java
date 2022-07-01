package com.example.wise_memory_optimizer.ui.battery.service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0109  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onReceive(Context r7, android.content.Intent r8) {
        /*
            r6 = this;
            java.lang.String r0 = r8.getAction()
            java.lang.String r1 = "com.app.action.alarmmanager"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x01ba
            java.lang.String r0 = "power"
            java.lang.Object r0 = r7.getSystemService(r0)
            android.os.PowerManager r0 = (android.os.PowerManager) r0
            java.lang.String r1 = "NAG"
            r2 = 1
            android.os.PowerManager$WakeLock r0 = r0.newWakeLock(r2, r1)
            r0.acquire()
            android.os.Bundle r8 = r8.getExtras()
            if (r8 == 0) goto L_0x004f
            java.lang.String r1 = "action_repeat_service"
            java.lang.Boolean r3 = java.lang.Boolean.FALSE
            boolean r3 = r3.booleanValue()
            boolean r1 = r8.getBoolean(r1, r3)
            if (r1 == 0) goto L_0x004f
            java.lang.Class<com.nuzastudio.batterysaver.batterydoctor.service.BatteryService> r1 = com.nuzastudio.batterysaver.batterydoctor.service.BatteryService.class
            boolean r1 = r6.isMyServiceRunning(r1, r7)
            if (r1 == 0) goto L_0x003b
            goto L_0x004f
        L_0x003b:
            android.content.Intent r1 = new android.content.Intent
            java.lang.Class<com.nuzastudio.batterysaver.batterydoctor.service.BatteryService> r3 = com.nuzastudio.batterysaver.batterydoctor.service.BatteryService.class
            r1.<init>(r7, r3)
            int r3 = android.os.Build.VERSION.SDK_INT
            r4 = 26
            if (r3 < r4) goto L_0x004c
            androidx.core.content.ContextCompat.startForegroundService(r7, r1)
            goto L_0x004f
        L_0x004c:
            r7.startService(r1)
        L_0x004f:
            if (r8 == 0) goto L_0x0111
            java.lang.String r1 = "action_check_devic_status"
            java.lang.Boolean r3 = java.lang.Boolean.FALSE
            boolean r3 = r3.booleanValue()
            boolean r1 = r8.getBoolean(r1, r3)
            if (r1 == 0) goto L_0x0111
            boolean r1 = com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.checkDNDDoing(r7)
            if (r1 == 0) goto L_0x0111
            boolean r1 = com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.checkDNDDoing(r7)
            if (r1 == 0) goto L_0x0111
            r1 = 0
            boolean r3 = com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.checkShouldDoing(r7, r1)
            if (r3 == 0) goto L_0x0111
            boolean r3 = com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.checkShouldDoing(r7, r2)
            if (r3 == 0) goto L_0x0111
            r3 = 2
            boolean r4 = com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.checkShouldDoing(r7, r3)
            if (r4 == 0) goto L_0x0111
            int r4 = com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.getBatteryLevel(r7)
            r5 = 15
            if (r4 > r5) goto L_0x00a4
            boolean r3 = com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.isScreenOn(r7)
            if (r3 == 0) goto L_0x0106
            boolean r3 = com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.checkShouldDoing(r7, r1)
            if (r3 == 0) goto L_0x0106
            com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils r3 = com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils.getInstance(r7)
            boolean r3 = r3.getLowBatteryReminder()
            if (r3 == 0) goto L_0x0106
            com.nuzastudio.batterysaver.batterydoctor.notification.NotificationDevice.showNotificationLowBattery(r7)
            com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.intSound(r7)
            goto L_0x0107
        L_0x00a4:
            boolean r4 = com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.isScreenOn(r7)
            if (r4 == 0) goto L_0x0106
            int r3 = com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.getRamdom(r3)
            switch(r3) {
                case 0: goto L_0x00ef;
                case 1: goto L_0x00d8;
                case 2: goto L_0x00b2;
                default: goto L_0x00b1;
            }
        L_0x00b1:
            goto L_0x0106
        L_0x00b2:
            boolean r3 = com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.getChargeStatus(r7)
            if (r3 != 0) goto L_0x0106
            com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils r3 = com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils.getInstance(r7)
            boolean r3 = r3.getLowBatteryReminder()
            if (r3 == 0) goto L_0x0106
            int r3 = com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.getBatteryLevel(r7)
            com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils r4 = com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils.getInstance(r7)
            int r4 = r4.getLevelScreenOn()
            int r3 = r3 - r4
            if (r3 <= r2) goto L_0x0106
            com.nuzastudio.batterysaver.batterydoctor.notification.NotificationDevice.showNotificationOptimize(r7)
            com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.intSound(r7)
            goto L_0x0107
        L_0x00d8:
            boolean r3 = com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.checkTemp(r7)
            if (r3 == 0) goto L_0x0106
            com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils r3 = com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils.getInstance(r7)
            boolean r3 = r3.getCoolDownReminder()
            if (r3 == 0) goto L_0x0106
            com.nuzastudio.batterysaver.batterydoctor.notification.NotificationDevice.showNotificationTemp(r7)
            com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.intSound(r7)
            goto L_0x0107
        L_0x00ef:
            boolean r3 = com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.checkMemory(r7)
            if (r3 == 0) goto L_0x0106
            com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils r3 = com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils.getInstance(r7)
            boolean r3 = r3.getBoostReminder()
            if (r3 == 0) goto L_0x0106
            com.nuzastudio.batterysaver.batterydoctor.notification.NotificationDevice.showNotificationMemory(r7)
            com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.intSound(r7)
            goto L_0x0107
        L_0x0106:
            r2 = 0
        L_0x0107:
            if (r2 != 0) goto L_0x0111
            java.lang.String r1 = "action_check_devic_status"
            r2 = 300000(0x493e0, float:4.2039E-40)
            com.nuzastudio.batterysaver.batterydoctor.RepeatService.AlarmUtils.setAlarm(r7, r1, r2)
        L_0x0111:
            if (r8 == 0) goto L_0x0164
            java.lang.String r1 = "action_smart_on"
            java.lang.Boolean r2 = java.lang.Boolean.FALSE
            boolean r2 = r2.booleanValue()
            boolean r1 = r8.getBoolean(r1, r2)
            if (r1 == 0) goto L_0x0164
            boolean r1 = com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.checkSystemWritePermission(r7)
            if (r1 == 0) goto L_0x0164
            com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils r1 = com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils.getInstance(r7)
            boolean r1 = r1.getSmartMode()
            if (r1 != 0) goto L_0x0132
            return
        L_0x0132:
            com.nuzastudio.batterysaver.batterydoctor.Utils.PreferenceUtil r1 = new com.nuzastudio.batterysaver.batterydoctor.Utils.PreferenceUtil
            r1.<init>()
            java.lang.String r2 = "pediod_index"
            int r2 = com.nuzastudio.batterysaver.batterydoctor.Utils.PreferenceUtil.getInt(r7, r2)
            java.util.List r1 = r1.getListBatterySaver(r7)
            java.lang.Object r3 = r1.get(r2)
            com.nuzastudio.batterysaver.batterydoctor.model.BatteryInfo r3 = (com.nuzastudio.batterysaver.batterydoctor.model.BatteryInfo) r3
            com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.setBatterySaverSelected(r7, r3)
            java.lang.Object r1 = r1.get(r2)
            com.nuzastudio.batterysaver.batterydoctor.model.BatteryInfo r1 = (com.nuzastudio.batterysaver.batterydoctor.model.BatteryInfo) r1
            java.lang.String r1 = r1.getTitle()
            com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.showToastMode(r7, r2, r1)
            java.lang.String r1 = "action_smart_on"
            com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils r2 = com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils.getInstance(r7)
            int r2 = r2.getTimeOn()
            com.nuzastudio.batterysaver.batterydoctor.RepeatService.AlarmUtils.setAlarm(r7, r1, r2)
        L_0x0164:
            if (r8 == 0) goto L_0x01b7
            java.lang.String r1 = "action_smart_off"
            java.lang.Boolean r2 = java.lang.Boolean.FALSE
            boolean r2 = r2.booleanValue()
            boolean r8 = r8.getBoolean(r1, r2)
            if (r8 == 0) goto L_0x01b7
            boolean r8 = com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.checkSystemWritePermission(r7)
            if (r8 == 0) goto L_0x01b7
            com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils r8 = com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils.getInstance(r7)
            boolean r8 = r8.getSmartMode()
            if (r8 != 0) goto L_0x0185
            return
        L_0x0185:
            com.nuzastudio.batterysaver.batterydoctor.Utils.PreferenceUtil r8 = new com.nuzastudio.batterysaver.batterydoctor.Utils.PreferenceUtil
            r8.<init>()
            java.lang.String r1 = "pediod_outside_index"
            int r1 = com.nuzastudio.batterysaver.batterydoctor.Utils.PreferenceUtil.getInt(r7, r1)
            java.util.List r8 = r8.getListBatterySaver(r7)
            java.lang.Object r2 = r8.get(r1)
            com.nuzastudio.batterysaver.batterydoctor.model.BatteryInfo r2 = (com.nuzastudio.batterysaver.batterydoctor.model.BatteryInfo) r2
            com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.setBatterySaverSelected(r7, r2)
            java.lang.Object r8 = r8.get(r1)
            com.nuzastudio.batterysaver.batterydoctor.model.BatteryInfo r8 = (com.nuzastudio.batterysaver.batterydoctor.model.BatteryInfo) r8
            java.lang.String r8 = r8.getTitle()
            com.nuzastudio.batterysaver.batterydoctor.Utils.C0769Utils.showToastMode(r7, r1, r8)
            java.lang.String r8 = "action_smart_off"
            com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils r1 = com.nuzastudio.batterysaver.batterydoctor.Utils.SharePreferenceUtils.getInstance(r7)
            int r1 = r1.getTimeOff()
            com.nuzastudio.batterysaver.batterydoctor.RepeatService.AlarmUtils.setAlarm(r7, r8, r1)
        L_0x01b7:
            r0.release()
        L_0x01ba:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nuzastudio.batterysaver.batterydoctor.receiver.AlarmReceiver.onReceive(android.content.Context, android.content.Intent):void");
    }

    private boolean isMyServiceRunning(Class<?> cls, Context context) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
