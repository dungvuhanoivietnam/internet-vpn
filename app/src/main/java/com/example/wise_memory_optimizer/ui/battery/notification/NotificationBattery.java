package com.example.wise_memory_optimizer.ui.battery.notification;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.internal.view.SupportMenu;

import com.example.wise_memory_optimizer.MainActivity;
import com.example.wise_memory_optimizer.R;
import com.example.wise_memory_optimizer.utils.SharePreferenceUtils;
import com.example.wise_memory_optimizer.utils.Utils;

public class NotificationBattery extends NotificationCompat.Builder {
    public static final int NOTIFYCATION_BATTERY_ID = 1;
    private static NotificationBattery notifycationBattery;
    private int[] iconRes = {R.drawable.ic_p0, R.drawable.ic_p1, R.drawable.ic_p2, R.drawable.ic_p3, R.drawable.ic_p4, R.drawable.ic_p5, R.drawable.ic_p6, R.drawable.ic_p7, R.drawable.ic_p8, R.drawable.ic_p9, R.drawable.ic_p10, R.drawable.ic_p11, R.drawable.ic_p12, R.drawable.ic_p13, R.drawable.ic_p14, R.drawable.ic_p15, R.drawable.ic_p16, R.drawable.ic_p17, R.drawable.ic_p18, R.drawable.ic_p19, R.drawable.ic_p20, R.drawable.ic_p21, R.drawable.ic_p22, R.drawable.ic_p23, R.drawable.ic_p24, R.drawable.ic_p25, R.drawable.ic_p26, R.drawable.ic_p27, R.drawable.ic_p28, R.drawable.ic_p29, R.drawable.ic_p30, R.drawable.ic_p31, R.drawable.ic_p32, R.drawable.ic_p33, R.drawable.ic_p34, R.drawable.ic_p35, R.drawable.ic_p36, R.drawable.ic_p37, R.drawable.ic_p38, R.drawable.ic_p39, R.drawable.ic_p40, R.drawable.ic_p41, R.drawable.ic_p42, R.drawable.ic_p43, R.drawable.ic_p44, R.drawable.ic_p45, R.drawable.ic_p46, R.drawable.ic_p47, R.drawable.ic_p48, R.drawable.ic_p49, R.drawable.ic_p50, R.drawable.ic_p51, R.drawable.ic_p52, R.drawable.ic_p53, R.drawable.ic_p54, R.drawable.ic_p55, R.drawable.ic_p56, R.drawable.ic_p57, R.drawable.ic_p58, R.drawable.ic_p59, R.drawable.ic_p60, R.drawable.ic_p61, R.drawable.ic_p62, R.drawable.ic_p63, R.drawable.ic_p64, R.drawable.ic_p65, R.drawable.ic_p66, R.drawable.ic_p67, R.drawable.ic_p68, R.drawable.ic_p69, R.drawable.ic_p70, R.drawable.ic_p71, R.drawable.ic_p72, R.drawable.ic_p73, R.drawable.ic_p74, R.drawable.ic_p75, R.drawable.ic_p76, R.drawable.ic_p77, R.drawable.ic_p78, R.drawable.ic_p79, R.drawable.ic_p80, R.drawable.ic_p81, R.drawable.ic_p82, R.drawable.ic_p83, R.drawable.ic_p84, R.drawable.ic_p85, R.drawable.ic_p86, R.drawable.ic_p87, R.drawable.ic_p88, R.drawable.ic_p89, R.drawable.ic_p90, R.drawable.ic_p91, R.drawable.ic_p92, R.drawable.ic_p93, R.drawable.ic_p94, R.drawable.ic_p95, R.drawable.ic_p96, R.drawable.ic_p97, R.drawable.ic_p98, R.drawable.ic_p99, R.drawable.ic_p100};
    Context mContext;
    NotificationManager notificationManager;

    public String getTempF() {
        return null;
    }

    public NotificationBattery(@NonNull Context context, @NonNull String str) {
        super(context, str);
        this.mContext = context.getApplicationContext();
        this.notificationManager = (NotificationManager) context.getSystemService("notification");
    }

    public static NotificationBattery getInstance(Context context) {
        if (notifycationBattery == null) {
            notifycationBattery = new NotificationBattery(context, "my_channel_fast_charger");
        }
        return notifycationBattery;
    }

    public void updateNotify(int i, int i2, int i3, int i4, boolean z) {
        Intent intent = new Intent(this.mContext, MainActivity.class);
        intent.setFlags(268435456);
        PendingIntent activity = PendingIntent.getActivity(this.mContext, 0, intent, 0);
        Intent intent2 = new Intent(this.mContext, MainActivity.class);
        intent.setFlags(268435456);
        PendingIntent activity2 = PendingIntent.getActivity(this.mContext, 0, intent2, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.mContext);
        builder.setSmallIcon(this.iconRes[i]);
        builder.setTicker((CharSequence) null);
        builder.setOnlyAlertOnce(true);
        builder.setContentTitle("fff");
        builder.setContentText("fff");
        builder.setContentIntent(activity);
        builder.setDefaults(0);
        RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), R.layout.custom_notification);
        remoteViews.setOnClickPendingIntent(R.id.img_clean, activity2);
        remoteViews.setTextViewText(R.id.tvBattery, String.valueOf(i) + "%");
        remoteViews.setTextViewText(R.id.tvTemp, getTemp(i2));
        remoteViews.setTextViewText(R.id.tvHour, String.format("%02d", new Object[]{Integer.valueOf(i3)}));
        remoteViews.setTextViewText(R.id.tvMin, String.format("%02d", new Object[]{Integer.valueOf(i4)}));
        remoteViews.setTextViewText(R.id.tv_status, getStatusTime(z));
        if (z) {
            if (Utils.Companion.getChargeFull(this.mContext)) {
                remoteViews.setViewVisibility(R.id.view_time_left, 8);
                remoteViews.setViewVisibility(R.id.tvFullCharge, 0);
                remoteViews.setTextViewText(R.id.tvFullCharge, this.mContext.getString(R.string.power_full));
            }
            remoteViews.setImageViewResource(R.id.img_battery, R.drawable.ic_battery_notification_charge);
        } else if (i < 20) {
            remoteViews.setImageViewResource(R.id.img_battery, R.drawable.ic_battery_notification_low);
        } else {
            remoteViews.setImageViewResource(R.id.img_battery, R.drawable.ic_battery_notification_normal);
        }
        if (SharePreferenceUtils.getInstance(this.mContext).getCoolNotification()) {
            remoteViews.setImageViewResource(R.id.img_temp, R.drawable.ic_temperature_notification_hot);
        } else {
            remoteViews.setImageViewResource(R.id.img_temp, R.drawable.ic_temperature_notification_normal);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            createChanelID();
            builder.setChannelId("my_channel_fast_charger");
        }
        builder.setCustomContentView(remoteViews);
        this.notificationManager.notify(1, builder.build());
    }

    private String getStatusTime(boolean z) {
        StringBuilder sb = new StringBuilder();
        if (z) {
            sb.append(this.mContext.getResources().getString(R.string.noti_battery_charging_timer_left));
        } else {
            sb.append(this.mContext.getResources().getString(R.string.time_left));
        }
        return sb.toString();
    }

    private String convertTime(int i, int i2) {
        return " " + i + "h" + i2 + "m";
    }

    public String getTemp(int i) {
        if (!SharePreferenceUtils.getInstance(this.mContext).getTempFormat()) {
            String valueOf = String.valueOf(Math.ceil((double) (((((((float) i) / 10.0f) * 9.0f) / 5.0f) + 32.0f) * 100.0f)) / 100.0d);
            return valueOf + this.mContext.getString(R.string.fahrenheit);
        }
        String d = Double.toString(Math.ceil((double) ((((float) i) / 10.0f) * 100.0f)) / 100.0d);
        return d + this.mContext.getString(R.string.celsius);
    }

    public void cancel() {
        this.notificationManager.cancel(1);
    }

    @SuppressLint({"WrongConstant"})
    @TargetApi(26)
    private void createChanelID() {
        try {
            String string = this.mContext.getString(R.string.app_name);
            String string2 = this.mContext.getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel("my_channel_fast_charger", string, 2);
            notificationChannel.setDescription(string2);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            this.notificationManager.createNotificationChannel(notificationChannel);
        } catch (Exception unused) {
        }
    }
}
