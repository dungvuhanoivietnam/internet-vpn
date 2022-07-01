package com.example.wise_memory_optimizer.ui.battery.service;

public class LocationPackageRequestParams {
    public static final boolean DEFAULT_BLUETOOTH_ENABLED = true;
    public static final long DEFAULT_BLUETOOTH_FLUSH_RESULTS_TIMEOUT_MS = 300;
    public static final int DEFAULT_BLUETOOTH_MAX_SCAN_RESULTS = 25;
    public static final long DEFAULT_BLUETOOTH_SCAN_DURATION_MS = 500;
    public static final long DEFAULT_LAST_LOCATION_MAX_AGE_MS = 60000;
    public static final boolean DEFAULT_LOCATION_ENABLED = true;
    public static final float DEFAULT_LOCATION_MAX_ACCURACY_METERS = 100.0f;
    public static final String[] DEFAULT_LOCATION_PROVIDERS = {"network", "gps"};
    public static final long DEFAULT_LOCATION_REQUEST_TIMEOUT_MS = 30000;
    public static final boolean DEFAULT_WIFI_ACTIVE_SCAN_ALLOWED = true;
    public static final boolean DEFAULT_WIFI_ACTIVE_SCAN_FORCED = false;
    public static final boolean DEFAULT_WIFI_ENABLED = true;
    public static final int DEFAULT_WIFI_MAX_SCAN_RESULTS = 25;
    public static final long DEFAULT_WIFI_SCAN_MAX_AGE_MS = 30000;
    public static final long DEFAULT_WIFI_SCAN_TIMEOUT_MS = 6000;
    public long bluetoothFlushResultsTimeoutMs;
    public int bluetoothMaxScanResults;
    public long bluetoothScanDurationMs;
    public boolean isBluetoothScanEnabled;
    public boolean isLocationScanEnabled;
    public boolean isWifiActiveScanAllowed;
    public boolean isWifiActiveScanForced;
    public boolean isWifiScanEnabled;
    public long lastLocationMaxAgeMs;
    public float locationMaxAccuracyMeters;
    public final String[] locationProviders;
    public long locationRequestTimeoutMs;
    public int wifiMaxScanResults;
    public long wifiScanMaxAgeMs;
    public long wifiScanTimeoutMs;

    public static class Builder {
        public long bluetoothFlushResultsTimeoutMs = 300;
        public int bluetoothMaxScanResults = 25;
        public long bluetoothScanDurationMs = 500;
        public boolean isBluetoothScanEnabled = true;
        public boolean isLocationScanEnabled = true;
        public boolean isWifiActiveScanAllowed = true;
        public boolean isWifiActiveScanForced = false;
        public boolean isWifiScanEnabled = true;
        public long lastLocationMaxAgeMs = LocationPackageRequestParams.DEFAULT_LAST_LOCATION_MAX_AGE_MS;
        public float locationMaxAccuracyMeters = 100.0f;
        public String[] locationProviders = LocationPackageRequestParams.DEFAULT_LOCATION_PROVIDERS;
        public long locationRequestTimeoutMs = 30000;
        public int wifiMaxScanResults = 25;
        public long wifiScanMaxAgeMs = 30000;
        public long wifiScanTimeoutMs = 6000;

        public LocationPackageRequestParams build() {
            return new LocationPackageRequestParams(this);
        }

        public Builder setBluetoothFlushResultsTimeoutMs(long j) {
            this.bluetoothFlushResultsTimeoutMs = j;
            return this;
        }

        public Builder setBluetoothMaxScanResults(int i) {
            this.bluetoothMaxScanResults = i;
            return this;
        }

        public Builder setBluetoothScanDurationMs(long j) {
            this.bluetoothScanDurationMs = j;
            return this;
        }

        public Builder setBluetoothScanEnabled(boolean z) {
            this.isBluetoothScanEnabled = z;
            return this;
        }

        public Builder setLastLocationMaxAgeMs(long j) {
            this.lastLocationMaxAgeMs = j;
            return this;
        }

        public Builder setLocationMaxAccuracyMeters(float f) {
            this.locationMaxAccuracyMeters = f;
            return this;
        }

        public Builder setLocationProviders(String[] strArr) {
            this.locationProviders = strArr;
            return this;
        }

        public Builder setLocationRequestTimeoutMs(long j) {
            this.locationRequestTimeoutMs = j;
            return this;
        }

        public Builder setLocationScanEnabled(boolean z) {
            this.isLocationScanEnabled = z;
            return this;
        }

        public Builder setWifiActiveScanAllowed(boolean z) {
            this.isWifiActiveScanAllowed = z;
            return this;
        }

        public Builder setWifiActiveScanForced(boolean z) {
            this.isWifiActiveScanForced = z;
            return this;
        }

        public Builder setWifiMaxScanResults(int i) {
            this.wifiMaxScanResults = i;
            return this;
        }

        public Builder setWifiScanEnabled(boolean z) {
            this.isWifiScanEnabled = z;
            return this;
        }

        public Builder setWifiScanMaxAgeMs(long j) {
            this.wifiScanMaxAgeMs = j;
            return this;
        }

        public Builder setWifiScanTimeoutMs(long j) {
            this.wifiScanTimeoutMs = j;
            return this;
        }
    }

    public LocationPackageRequestParams(Builder builder) {
        this.isLocationScanEnabled = builder.isLocationScanEnabled;
        this.locationProviders = builder.locationProviders;
        this.locationMaxAccuracyMeters = builder.locationMaxAccuracyMeters;
        this.locationRequestTimeoutMs = builder.locationRequestTimeoutMs;
        this.lastLocationMaxAgeMs = builder.lastLocationMaxAgeMs;
        this.isWifiScanEnabled = builder.isWifiScanEnabled;
        this.wifiScanMaxAgeMs = builder.wifiScanMaxAgeMs;
        this.wifiMaxScanResults = builder.wifiMaxScanResults;
        this.wifiScanTimeoutMs = builder.wifiScanTimeoutMs;
        this.isWifiActiveScanAllowed = builder.isWifiActiveScanAllowed;
        this.isWifiActiveScanForced = builder.isWifiActiveScanForced;
        this.isBluetoothScanEnabled = builder.isBluetoothScanEnabled;
        this.bluetoothScanDurationMs = builder.bluetoothScanDurationMs;
        this.bluetoothMaxScanResults = builder.bluetoothMaxScanResults;
        this.bluetoothFlushResultsTimeoutMs = builder.bluetoothFlushResultsTimeoutMs;
    }

    public long getBluetoothFlushResultsTimeoutMs() {
        return this.bluetoothFlushResultsTimeoutMs;
    }

    public int getBluetoothMaxScanResults() {
        return this.bluetoothMaxScanResults;
    }

    public long getBluetoothScanDurationMs() {
        return this.bluetoothScanDurationMs;
    }

    public long getLastLocationMaxAgeMs() {
        return this.lastLocationMaxAgeMs;
    }

    public float getLocationMaxAccuracyMeters() {
        return this.locationMaxAccuracyMeters;
    }

    public String[] getLocationProviders() {
        return this.locationProviders;
    }

    public long getLocationRequestTimeoutMs() {
        return this.locationRequestTimeoutMs;
    }

    public int getWifiMaxScanResults() {
        return this.wifiMaxScanResults;
    }

    public long getWifiScanMaxAgeMs() {
        return this.wifiScanMaxAgeMs;
    }

    public long getWifiScanTimeoutMs() {
        return this.wifiScanTimeoutMs;
    }

    public boolean isBluetoothScanEnabled() {
        return this.isBluetoothScanEnabled;
    }

    public boolean isLocationScanEnabled() {
        return this.isLocationScanEnabled;
    }

    public boolean isWifiActiveScanAllowed() {
        return this.isWifiActiveScanAllowed;
    }

    public boolean isWifiActiveScanForced() {
        return this.isWifiActiveScanForced;
    }

    public boolean isWifiScanEnabled() {
        return this.isWifiScanEnabled;
    }
}
