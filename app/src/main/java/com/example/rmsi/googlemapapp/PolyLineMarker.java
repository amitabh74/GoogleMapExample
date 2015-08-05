package com.example.rmsi.googlemapapp;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amitabh.Basu on 30-07-2015.
 */
public class PolyLineMarker {
    private List<Marker> lstMarkers;
    private List<Polyline> lstPolyLines;

    public PolyLineMarker(){
        lstMarkers = new ArrayList<Marker>();
        lstPolyLines = new ArrayList<Polyline>();
    }

    public void setMarker(Marker marker){
        lstMarkers.add(marker);
    }

    public void getMarker(int pos){
        lstMarkers.get(pos);
    }

    public void setPolyLine(Polyline line){
        lstPolyLines.add(line);
    }

    public  int getMarkerCount(){
        return lstMarkers.size();
    }

    public void removePolyLineMarker(){
        //Remove Markers
        if(lstMarkers.size() > 0){
            for(Marker marker: lstMarkers){
                marker.remove();
                marker = null;
            }
        }

       //Remove Polylines
        if(lstPolyLines.size() > 0){
            for(Polyline line:lstPolyLines){
                line.remove();;
                line = null;
            }
        }
    }

}
