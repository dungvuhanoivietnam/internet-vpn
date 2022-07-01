package com.example.wise_memory_optimizer.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

public class TypefacesUtils {
    private static final String TAG = TypefacesUtils.class.getSimpleName();

    // cache
    private static final Hashtable<String, Typeface> caches = new Hashtable<>();

    // default font
    public static final String FONT_DEFAULT = "fonts/san_francisco_display_regular.otf";

    /**
     * get typeface
     *
     * @param context
     * @param assetPath
     * @return
     */
    public static Typeface get(Context context, String assetPath) {
        try {
            synchronized (caches) {
                if (!caches.containsKey(assetPath)) {
                    try {
                        Typeface tf = Typeface.createFromAsset(context.getAssets(), assetPath);
                        caches.put(assetPath, tf);
                    } catch (Exception e) {
                        Log.e(TAG, "Could not get typeface '" + assetPath + "' because " + e.getMessage());
                        return null;
                    }
                }

                return caches.get(assetPath);
            }
        } catch (Exception e) {
        }

        return null;
    }
}
