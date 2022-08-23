package com.unina.natour.controllers.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

import org.osmdroid.util.GeoPoint;

public class GPSUtils {

    public static boolean hasGPSFeature(Context context){
        PackageManager packageManager = context.getPackageManager();
        return (packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS));
    }

    public static boolean isGPSEnabled(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean hasGPSPermissionGranted(Context context){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
