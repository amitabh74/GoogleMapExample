package com.example.rmsi.googlemapapp;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amitabh.Basu on 03-08-2015.
 */
public class PolygonMarker {
    private List<Marker> lstPolyMarkers;
    private Polygon polygon;

    public PolygonMarker(){
        lstPolyMarkers = new ArrayList<Marker>();
    }

    public void setMarker(Marker marker){
        lstPolyMarkers.add(marker);
    }

    public void getMarker(int pos){
        lstPolyMarkers.get(pos);
    }

    public void setPolygon(Polygon polygon){
        this.polygon = polygon;
    }

    public  int getMarkerCount(){
        return lstPolyMarkers.size();
    }

    public void removePolygonMarker(){
        //Remove Markers
        if(lstPolyMarkers.size() > 0){
            for(Marker marker: lstPolyMarkers){
                marker.remove();
                marker = null;
            }
        }

       //Remove the polygon
        polygon.remove();
    }

}
