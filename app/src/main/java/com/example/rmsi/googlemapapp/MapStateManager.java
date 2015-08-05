package com.example.rmsi.googlemapapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Amitabh.Basu on 24-07-2015.
 */
public class MapStateManager {

    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String ZOOM = "zoom";
    private static final String BEARING = "bearing";
    private static final String TILT = "tilt";
    private static final String MAPTYPE = "MAPTYPE";

    private static final String PREFS_NAME = "MapCameraState";

    private SharedPreferences mapStatePrefs;

    public MapStateManager(Context context) {
        this.mapStatePrefs = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
    }

    /**
     * Save the state of map in SharedPreferences
     * @param gMap
     */
    public void saveMapState(GoogleMap gMap){
        SharedPreferences.Editor editor = mapStatePrefs.edit();
        CameraPosition cameraPosition = gMap.getCameraPosition();

        editor.putFloat(LATITUDE, (float)cameraPosition.target.latitude);
        editor.putFloat(LONGITUDE, (float)cameraPosition.target.longitude);
        editor.putFloat(ZOOM, cameraPosition.zoom);
        editor.putFloat(BEARING, cameraPosition.bearing);
        editor.putFloat(TILT, cameraPosition.tilt);
        editor.putInt(MAPTYPE, gMap.getMapType());

        editor.commit();
    }

    /**
     * Gets the Saved state of the Camera
     * @return
     */
    public CameraPosition getSavedCameraPosition(){
        float latitude = mapStatePrefs.getFloat(LATITUDE, 0);
        if(latitude == 0){
            return null;
        }

        float longitude = mapStatePrefs.getFloat(LONGITUDE, 0);
        LatLng target = new LatLng(latitude, longitude);

        float zoom = mapStatePrefs.getFloat(ZOOM, 0);
        float bearing = mapStatePrefs.getFloat(BEARING, 0);
        float tilt = mapStatePrefs.getFloat(TILT, 0);

        CameraPosition cameraPosition = new CameraPosition(target, zoom, tilt, bearing);
        return cameraPosition;
    }

    public int getSavedMapType(){
       int mapType = mapStatePrefs.getInt(MAPTYPE, -1);
       return mapType;
    }
}
