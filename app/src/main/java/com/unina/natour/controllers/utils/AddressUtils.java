package com.unina.natour.controllers.utils;

import android.location.Address;

import com.unina.natour.models.RouteLegModel;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class AddressUtils {

    public static String getAddressName(Address address) {
        if (address == null) return null;

        StringBuilder stringBuilder = new StringBuilder();
        int n = address.getMaxAddressLineIndex();
        for (int i = 0; i <= n; i++) {
            if (i != 0) stringBuilder.append(", ");
            stringBuilder.append(address.getAddressLine(i));
        }

        return stringBuilder.toString();
    }

    public static boolean arePointsInRange(GeoPoint geoPoint1, GeoPoint geoPoint2, double range){
        double distance = geoPoint1.distanceToAsDouble(geoPoint2);
        if(distance > range) return false;
        return true;
    }
/*
    public static boolean isPointCloseToRoute(GeoPoint geoPoint, List<RouteLegModel> route, double range){
        for(RouteLegModel routeLeg : route){
            List<GeoPoint> trackGeoPoints = routeLeg.getTrack();
            for(GeoPoint trackGeoPoint : trackGeoPoints){
                if(arePointsInRange(geoPoint,trackGeoPoint,range)) return true;
            }
        }
        return false;
    }
*/
    public static boolean isPointCloseToRoute(GeoPoint geoPoint, List<GeoPoint> route, double range){
        for(GeoPoint routeGeoPoint : route){
            if(arePointsInRange(geoPoint,routeGeoPoint,range)) return true;
        }
        return false;
    }

}
