package com.example.wise_memory_optimizer.ui.battery.service;

import android.location.Location;

public interface LocationScanner {
    Location getLocation() throws ScannerException;

    void initAndCheckEligibility() throws ScannerException;
}
