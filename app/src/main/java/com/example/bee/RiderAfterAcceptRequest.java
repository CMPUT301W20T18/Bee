package com.example.bee;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class RiderAfterAcceptRequest extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "accept_request_activity";
    private static final float DEFAULT_ZOOM = 15f;

    GoogleMap request_accepted_map;
    private FusedLocationProviderClient client_device;
    TextView driver_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_after_accept_request);
        initMap();
        driver_name = (TextView)findViewById(R.id.driver_name);
        driver_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiderAfterAcceptRequest.this, DriverBasicInformation.class);
                startActivityForResult(intent, 1);

            }
        });

    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_requestAccepted);
        mapFragment.getMapAsync(RiderAfterAcceptRequest.this);
    }


    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        client_device = LocationServices.getFusedLocationProviderClient(this);

        try{
            final Task location = client_device.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: found location!");
                        Location currentLocation = (Location) task.getResult();

                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                DEFAULT_ZOOM);

                    }else{
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(RiderAfterAcceptRequest.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        request_accepted_map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");

        request_accepted_map = googleMap;
        LatLng Edmonton = new LatLng(53.544388,-113.490929);
        request_accepted_map.addMarker(new MarkerOptions().position(Edmonton).title("edmonton!"));
        request_accepted_map.moveCamera(CameraUpdateFactory.newLatLng(Edmonton));

        getDeviceLocation();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        request_accepted_map.setMyLocationEnabled(true);
        request_accepted_map.getUiSettings().setCompassEnabled(true);


    }

}