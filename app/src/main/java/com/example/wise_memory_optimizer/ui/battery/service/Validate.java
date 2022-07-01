package com.example.wise_memory_optimizer.ui.battery.service;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.Collection;
import java.util.List;

public final class Validate {
    public static final String CONTENT_PROVIDER_BASE = "com.facebook.app.FacebookContentProvider";
    public static final String CONTENT_PROVIDER_NOT_FOUND_REASON = "A ContentProvider for this app was not set up in the AndroidManifest.xml, please add %s as a provider to your AndroidManifest.xml file. See https://developers.facebook.com/docs/sharing/android for more info.";
    public static final String CUSTOM_TAB_REDIRECT_URI_PREFIX = "fbconnect://cct.";
    public static final String FACEBOOK_ACTIVITY_NOT_FOUND_REASON = "FacebookActivity is not declared in the AndroidManifest.xml. If you are using the facebook-common module or dependent modules please add com.facebook.FacebookActivity to your AndroidManifest.xml file. See https://developers.facebook.com/docs/android/getting-started for more info.";
    public static final String NO_INTERNET_PERMISSION_REASON = "No internet permissions granted for the app, please add <uses-permission android:name=\"android.permission.INTERNET\" /> to your AndroidManifest.xml.";
    public static final String TAG = "com.facebook.internal.Validate";


    public static <T> void containsNoNulls(Collection<T> collection, String str) {
        notNull(collection, str);
        for (T t : collection) {
            if (t == null) {
                throw new NullPointerException("Container '" + str + "' cannot contain null values");
            }
        }
    }

    public static String hasAppID() {
//        String applicationId = FacebookSdk.getApplicationId();
//        if (applicationId != null) {
//            return applicationId;
//        }
        throw new IllegalStateException("No App ID found, please set the App ID.");
    }


    public static void hasInternetPermissions(Context context, boolean z) {
        notNull(context, "context");
        if (context.checkCallingOrSelfPermission("android.permission.INTERNET") == PackageManager.PERMISSION_DENIED && z) {
            throw new IllegalStateException(NO_INTERNET_PERMISSION_REASON);
        }
    }

    public static boolean hasLocationPermission(Context context) {
        return hasPermission(context, "android.permission.ACCESS_COARSE_LOCATION") || hasPermission(context, "android.permission.ACCESS_FINE_LOCATION");
    }

    public static boolean hasPermission(Context context, String str) {
        return context.checkCallingOrSelfPermission(str) == PackageManager.PERMISSION_GRANTED;
    }

    public static void notNull(Object obj, String str) {
        if (obj == null) {
            throw new NullPointerException("Argument '" + str + "' cannot be null");
        }
    }

}
