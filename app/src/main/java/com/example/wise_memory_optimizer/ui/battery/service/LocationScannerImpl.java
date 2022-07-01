package com.example.wise_memory_optimizer.ui.battery.service;


import static android.Manifest.permission_group.LOCATION;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.HandlerThread;

import java.util.ArrayList;
import java.util.List;


public class LocationScannerImpl implements LocationScanner, LocationListener {
    public static final float MIN_DISTANCE_BETWEEN_UPDATES = 0.0f;
    public static final long MIN_TIME_BETWEEN_UPDATES = 100;
    public Context context;
    public List<String> enabledProviders;
    public Location freshLocation;
    public LocationManager locationManager;
    public LocationPackageRequestParams params;
    public final Object scanLock = new Object();

    @SuppressLint("WrongConstant")
    public LocationScannerImpl(Context context2, LocationPackageRequestParams locationPackageRequestParams) {
        this.context = context2;
        this.params = locationPackageRequestParams;
        this.locationManager = (LocationManager) context2.getSystemService(LOCATION);
    }

    /* JADX INFO: finally extract failed */
    @SuppressLint("MissingPermission")
    private Location getFreshLocation() throws ScannerException {
        this.freshLocation = null;
        HandlerThread handlerThread = new HandlerThread("LocationScanner");
        try {
            handlerThread.start();
            for (String requestLocationUpdates : this.enabledProviders) {
                this.locationManager.requestLocationUpdates(requestLocationUpdates, 100, MIN_DISTANCE_BETWEEN_UPDATES, this, handlerThread.getLooper());
            }
            try {
                synchronized (this.scanLock) {
                    this.scanLock.wait(this.params.getLocationRequestTimeoutMs());
                }
            } catch (Exception unused) {
            }
            this.locationManager.removeUpdates(this);
            handlerThread.quit();
            Location location = this.freshLocation;
            if (location != null) {
                return location;
            }
            throw new ScannerException(ScannerException.Type.TIMEOUT);
        } catch (Throwable th) {
            this.locationManager.removeUpdates(this);
            handlerThread.quit();
            throw th;
        }
    }

    private Location getLastLocation(String str) {
        @SuppressLint("MissingPermission") Location lastKnownLocation = this.locationManager.getLastKnownLocation(str);
        if (lastKnownLocation == null) {
            return null;
        }
        if (System.currentTimeMillis() - lastKnownLocation.getTime() < this.params.getLastLocationMaxAgeMs()) {
            return lastKnownLocation;
        }
        return null;
    }

    public Location getLocation() throws ScannerException {
        for (String lastLocation : this.enabledProviders) {
            Location lastLocation2 = getLastLocation(lastLocation);
            if (lastLocation2 != null) {
                return lastLocation2;
            }
        }
        return getFreshLocation();
    }

    public void initAndCheckEligibility() throws ScannerException {
        if (Validate.hasLocationPermission(this.context)) {
            this.enabledProviders = new ArrayList(this.params.getLocationProviders().length);
            for (String str : this.params.getLocationProviders()) {
                if (this.locationManager.isProviderEnabled(str)) {
                    this.enabledProviders.add(str);
                }
            }
            if (this.enabledProviders.isEmpty()) {
                throw new ScannerException(ScannerException.Type.DISABLED);
            }
            return;
        }
        throw new ScannerException(ScannerException.Type.PERMISSION_DENIED);
    }

    public void onLocationChanged(Location location) {
        if (this.freshLocation == null && location.getAccuracy() < this.params.getLocationMaxAccuracyMeters()) {
            synchronized (this.scanLock) {
                this.freshLocation = location;
                this.scanLock.notify();
            }
        }
    }

    public void onProviderDisabled(String str) {
    }

    public void onProviderEnabled(String str) {
    }

    public void onStatusChanged(String str, int i, Bundle bundle) {
    }
}
