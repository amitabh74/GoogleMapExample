package com.example.rmsi.googlemapapp;



import android.app.Activity;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity { //implements
    //GoogleApiClient.ConnectionCallbacks,
    //GoogleApiClient.OnConnectionFailedListener,
    //LocationListener{

    private static LatLng lat_lon = new LatLng(52.407976, -4.071223);
    private static final float ZOOM_FACTOR = 15.0f;
    private static final String LOG_TAG = "TAG";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleMap googleMap;
    private Button btnMapType;
    private Marker marker;

    private boolean isMarkupOptionSelected;
    private PolylineOptions polylineOptions;
    private List<PolyLineMarker> lstPolyLineMarkers = new ArrayList<PolyLineMarker>();
    private PolyLineMarker polyLineMarker;
    private Polyline polyline;


    private boolean isMarkupPolyOptionSelected;
    private Polygon polygon;
    private List<PolygonMarker> lstPolygonMarkers = new ArrayList<PolygonMarker>();
    private PolygonOptions polygonOptions;
    private PolygonMarker polygonMarker;

    //Following are the declarations for finding current location
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private MarkerOptions mo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpMapIfNeeded();
        initializeDefault();

       /* googleApiClient = new GoogleApiClient.Builder(this)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(2 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        */

        ImageButton btnSearch = (ImageButton)findViewById(R.id.searchButton);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //First hide the soft Keyboard
                hideSoftKeyboad(v);

                EditText searchText = (EditText) findViewById(R.id.searchText);
                String strSearchText = searchText.getText().toString();
                initializeDefault();

                if (strSearchText != null && !strSearchText.isEmpty()) {
                    Geocoder geocoder = new Geocoder(getApplicationContext());

                    try {
                        List<Address> lstAddress = geocoder.getFromLocationName(strSearchText, 1);
                        Address address = lstAddress.get(0);
                        String locality = address.getLocality();

                        setMarker(locality, address.getCountryName(), address.getLatitude(), address.getLongitude());

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(),
                                address.getLongitude()), ZOOM_FACTOR));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    /**
     * Click Handler of ShowMyLocation.
     * @param v
     */
    /*public void showMyLocation(View v){
        isMarkupOptionSelected = false;

        if (!googleApiClient.isConnected()){
            googleApiClient.connect();
        }
    }*/

    /**
     * Remove all markers
     * @param v
     */
    public void clearMarkers(View v) {
        initializeDefault();

        if(lstPolyLineMarkers.size() > 0){
            for(PolyLineMarker polyLineMarker:lstPolyLineMarkers){
                polyLineMarker.removePolyLineMarker();
                lstPolyLineMarkers.remove(polyLineMarker);
                polyLineMarker= null;
            }
        }

        //Case where polyline has not been added to lstPolyLineMarker collection.
        if(polyLineMarker != null && polyLineMarker.getMarkerCount() > 0){
            polyLineMarker.removePolyLineMarker();
            polyLineMarker = null;
        }

        if(lstPolygonMarkers.size() > 0){
            for(PolygonMarker polygonMarker:lstPolygonMarkers){
                polygonMarker.removePolygonMarker();
                lstPolygonMarkers.remove(polygonMarker);
                polygonMarker = null;
            }
        }

        //Case where polygon has not been added to lstPolyLineMarker collection.
        if(polygonMarker != null && polygonMarker.getMarkerCount() > 0){
            polygonMarker.removePolygonMarker();
            polygonMarker = null;
        }

    }


    public void setMarker(String locality, String country, double lat, double lon){

        if (marker != null && !isMarkupOptionSelected) {
            marker.remove();
            marker = null;
        }

        //Toast.makeText(getApplicationContext(), "Value: " + isMarkupOptionSelected, Toast.LENGTH_LONG).show();

        if(isMarkupOptionSelected) {
            //if polyline markup option is selected
            MarkerOptions option = new MarkerOptions().position(new LatLng(lat, lon));
            Marker lineMarker = googleMap.addMarker(option);

            //Add the marker to collection for removal
            polyLineMarker.setMarker(lineMarker);
            drawLine(lineMarker);
        }else if(isMarkupPolyOptionSelected){
            MarkerOptions option = new MarkerOptions().position(new LatLng(lat, lon));
            Marker polyMarker = googleMap.addMarker(option);

            //Add the marker to collection for removal
            polygonMarker.setMarker(polyMarker);
            drawPolygon(polyMarker);
        }else{
            //Add marker at new location
            mo = new MarkerOptions().position(new LatLng(lat, lon))
                    .title(locality).draggable(true).snippet(country);

            marker = googleMap.addMarker(mo);
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.my_location));

        }
    }

    private void setUpMapIfNeeded() {
        // Initial Map
        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Set infowindow adpater
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View view = getLayoutInflater().inflate(R.layout.info_window, null);

                TextView tvLocality = (TextView) view.findViewById(R.id.tv_locality);
                TextView tvLat = (TextView) view.findViewById(R.id.tv_lat);
                TextView tvLon = (TextView) view.findViewById(R.id.tv_lon);
                TextView tvSnippet = (TextView) view.findViewById(R.id.tv_snippet);

                LatLng ll = marker.getPosition();
                tvLocality.setText(marker.getTitle());
                tvLat.setText("Latitude: " + ll.latitude);
                tvLon.setText("Longitude: " + ll.longitude);
                tvSnippet.setText(marker.getSnippet());
                return view;
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                List<Address> lstAddress = null;
                Geocoder geocoder = new Geocoder(MainActivity.this);

                try {
                    lstAddress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                if (lstAddress != null && lstAddress.size() > 0) {
                    Address address = lstAddress.get(0);
                    MainActivity.this.setMarker(address.getLocality(), address.getCountryName(),
                            latLng.latitude, latLng.longitude);
                }

            }
        });

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                List<Address> lstAddress = null;
                Geocoder geocoder = new Geocoder(MainActivity.this);
                LatLng latLng = marker.getPosition();

                try {
                    lstAddress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                if (lstAddress != null && lstAddress.size() > 0) {
                    Address address = lstAddress.get(0);
                    marker.setTitle(address.getLocality());
                    marker.setSnippet(address.getCountryName());
                    marker.showInfoWindow();
                }
            }
        });

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Put a dot on my current location
        //googleMap.setMyLocationEnabled(true); Commented to implement this programmatically, for this GooglePlayServicesClient interface is implemented.
        googleMap.setIndoorEnabled(true);
        googleMap.setTrafficEnabled(true);
        // 3D building
        googleMap.setBuildingsEnabled(true);
        // Get zoom button
        googleMap.getUiSettings().setZoomControlsEnabled(true);

    }

    private void hideSoftKeyboad(View v){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menuStreet:
                if(item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;

            case R.id.menuSatellite:
                if(item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;

            case R.id.menuHybrid:
                if(item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;

            case R.id.menuTerrain:
                if(item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {

        // googleApiClient.disconnect();
        super.onStop();
        MapStateManager mapStateManager = new MapStateManager(this);
        mapStateManager.saveMapState(googleMap);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initializeDefault();
        setUpMapIfNeeded();
        //googleApiClient.connect(); //connect for current location

        MapStateManager mapStateManager = new MapStateManager(this);
        CameraPosition cameraPosition = mapStateManager.getSavedCameraPosition();

        int mapType = mapStateManager.getSavedMapType();

        if(cameraPosition != null){
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

            if(mapType != -1 ){
                googleMap.setMapType(mapType);
            }

            googleMap.moveCamera(cameraUpdate);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

       /* if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }*/
    }

    //@Override
   /* public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        //Last location will be null if the google play serviced is accessing the location for the first time.
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
        else {
            handleNewLocation(location);
        }
    }*/

   /* private void handleNewLocation(Location location) {
        Log.d(LOG_TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        mo = new MarkerOptions()
                .position(latLng)
                .title("Current Location");


        /*In order to have a customized marker*/
    //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        /* Or from resource path*/
    //options.icon(BitmapDescriptorFactory.fromResource(R.id.ic_Plaimarker);

       /* if(marker != null){
            marker.remove();
        }
        marker = googleMap.addMarker(mo);
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.my_location));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_FACTOR));

        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        googleApiClient.disconnect();

    }*/

    //@Override
   /* public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "Google APIClient Location connection has been suspended");
    }*/

    // @Override
   /* public void onLocationChanged(Location location) {
        if(googleApiClient.isConnected()) {
            handleNewLocation(location);
        }
        Log.i(LOG_TAG, "New Location Recieved " + location.toString());
    }*/

    //@Override
   /* public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "Google APIClient Location connection has been failed");

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(LOG_TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }*/

    private void drawLine(Marker lineMarker){

        polylineOptions
                .add(lineMarker.getPosition());

        polyline = googleMap.addPolyline(polylineOptions);
        polyLineMarker.setPolyLine(polyline);
    }

    public void createPolyLine(View view){
        initializeDefault();
        isMarkupOptionSelected = true;

        if(polyLineMarker != null &&
                polyLineMarker.getMarkerCount() > 0){

            lstPolyLineMarkers.add(polyLineMarker);
        }
        polyLineMarker = new PolyLineMarker(); //Initialize PolyLineMarker class

        polylineOptions = new PolylineOptions()
                .width(2.5f)
                .color(Color.BLUE)
                .geodesic(true)
                .zIndex(100);

    }

    public void drawPolygon(Marker polyMarker){

        if(polygon != null){
            polygon.remove();
        }
        polygonOptions.add(polyMarker.getPosition());
        polygon = googleMap.addPolygon(polygonOptions);

        //Store the polygon for removal on clear button selection
        polygonMarker.setPolygon(polygon);
    }

    public void createPolygon(View view){
        initializeDefault();
        isMarkupPolyOptionSelected = true;

        if(polygonMarker != null &&
                polygonMarker.getMarkerCount() > 0){

            lstPolygonMarkers.add(polygonMarker);
        }
        polygonMarker = new PolygonMarker(); //Initialize PolygonMarker class

        polygonOptions = new PolygonOptions()
                .strokeColor(Color.BLACK)
                .fillColor(0x220000FF)
                .strokeWidth(3.0f)
                .geodesic(true);

        polygon = null;
    }

    public void initializeDefault(){
        isMarkupOptionSelected = false;
        isMarkupPolyOptionSelected = false;
    }


}
