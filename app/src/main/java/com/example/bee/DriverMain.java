package com.example.bee;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

<<<<<<< HEAD
=======
/**
 *This is a class that used for Driver, the map with current location
 */
>>>>>>> master

// This class is used to display driver's map
// it requests permission of GPS/location usage to get current location
// and move camero to it
// issue so far is current location is set to googleplex
// instead of real current location

<<<<<<< HEAD
=======

>>>>>>> master
public class DriverMain extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    Location currentPos;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_main);

        //mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();


        //  Search button method
        final Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                get new Intent and start new activity
                Intent search = new Intent(DriverMain.this, SearchRide.class);
                startActivity(search);
            }
        });

    }

//    This method is used to get current location for use
    private void getCurrentLocation(){
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            currentPos = location;
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.driverMap);
                            mapFragment.getMapAsync(DriverMain.this);
                        }
                    }
                });
    }

<<<<<<< HEAD
=======
    /**
     * This initialize google map and set & move camera to current location
     * @param googleMap
     *  This is the googleMap parameter
     */

>>>>>>> master
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latlng = new LatLng(currentPos.getLatitude(),currentPos.getLongitude());
        //LatLng place = new LatLng(53.486094, -113.506367);
        map.setMyLocationEnabled(true);
        //map.addMarker(new MarkerOptions().position(latlng).title("HI"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,15));
    }


<<<<<<< HEAD

=======
    /**This method is asking for permission of locating services
     *
     * @param requestCode
     *  request Code
     * @param permissions
     *  permission variable String
     * @param grantResults
     * grantResult nonNull int
     */
>>>>>>> master

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
                break;
            }
        }
    }
}
