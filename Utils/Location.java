# nuweApp/Utils/Location
This is an app I've been working; I have some bugs, but will only post SOME back-end code. 
Help will be much appreciated as I encounter more bugs :)

```
package com.example.eagle.share;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.wearable.Wearable;
import com.google.maps.android.SphericalUtil;

import java.io.InputStream;
import java.text.NumberFormat;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.wearable.Asset;
 
``` 
 
```
public class Uils {
    private static final String TAG = Uils.class.getSimpleName();

    private static final String PREFERENCES_LAT = "lat";
    private static final String PREFERENCES_LNG = "lng";
    private static final String PREFERENCES_GEOFENCE_ENABLED = "geofence";
    private static final String DISTANCE_FT_POSTFIX = "ft";
    private static final String DISTANCE_M_POSTFIX = "miles";

public static String distanceBetween (LatLng point1, LatLng point2) {
    if (point1 == null || point2 == null) {
        return null;
    }
    //GoogleMaps computes distance in km; living in the US, I converted distance to miles
    NumberFormat numberFormat = NumberFormat.getNumberInstance();
    double distance = Math.round(SphericalUtil.computeDistanceBetween(point1, point2));

    double distanceTwo = Math.round(distance*3.28);

    if(distanceTwo >=5280){
        numberFormat.setMaximumFractionDigits(1);
        return numberFormat.format(distanceTwo/5280) + DISTANCE_M_POSTFIX;
    }
    return numberFormat.format(distanceTwo) + DISTANCE_FT_POSTFIX;
}

public static void storeLocation(Context context, LatLng location){
    SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putLong(PREFERENCES_LAT, Double.doubleToRawLongBits(location.latitude));
    editor.putLong(PREFERENCES_LNG,Double.doubleToRawLongBits(location.longitude));
    editor.apply();
}

public static LatLng getlocation(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    Long lat = prefs.getLong(PREFERENCES_LAT, Long.MAX_VALUE);
    Long lng = prefs.getLong(PREFERENCES_LNG, Long.MAX_VALUE);
    if (lat != Long.MAX_VALUE && lng != Long.MAX_VALUE) {
        Double latDbl = Double.longBitsToDouble(lat);
        Double lngDbl = Double.longBitsToDouble(lng);
        return new LatLng(latDbl, lngDbl);
    }
    return null;
}

    public static void storeGeoFenceEnabled(Context context, boolean enable){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREFERENCES_GEOFENCE_ENABLED, enable);
        editor.apply();
}

    public static boolean getGeoFenceEbabled(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(PREFERENCES_GEOFENCE_ENABLED, true);
    }

    public static Bitmap loadBitmapFromAsset(GoogleApiClient googleApiClient,Asset asset ) {
        if (asset == null) {
            throw new IllegalArgumentException("Assetmust benon-null");
        }

        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(googleApiClient, asset).await().getInputStream();

        if (assetInputStream == null) {
            Log.w(TAG,"Unknown");
            return null;
        }

        return BitmapFactory.decodeStream(assetInputStream);

    }

}
```
