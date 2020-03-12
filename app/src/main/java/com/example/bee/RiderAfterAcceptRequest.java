package com.example.bee;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * This is a class that shows the situation after the rider confirms the driver's acceptance
 */

public class RiderAfterAcceptRequest extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "accept_request_activity";

    GoogleMap request_accepted_map;
    TextView driver_name;


    MarkerOptions place1, place2;
    Boolean drew = false;

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
                startActivity(intent);
            }
        });


    }

    /**
     * This is a method to initialize the map
     */

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_requestAccepted);
        mapFragment.getMapAsync(RiderAfterAcceptRequest.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");

        request_accepted_map = googleMap;
        request_accepted_map.setMyLocationEnabled(true);
        //request_accepted_map.getUiSettings().setCompassEnabled(true);

        LatLng place1_postion = new LatLng(53.523220,-113.526321);
        place1 = new MarkerOptions().position(place1_postion).title("Orientation");
        LatLng place2_postion = new LatLng(53.484300,-113.517250);
        place2 = new MarkerOptions().position(place2_postion).title("Destination");
        drew = getPoints(place1, place2);

        if (!drew){
            String text = "Invalid Address";
            Toast toast = Toast.makeText(RiderAfterAcceptRequest.this, text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    /**
     * This is a method to get the position of two points and draw a line between two points
     * @param from
     * this is the place to start
     * @param to
     * this is the place to end
     */

    private boolean getPoints(MarkerOptions from, MarkerOptions to) {
        try {

            LatLng from_position = from.getPosition();
            LatLng to_position = to.getPosition();

            request_accepted_map.addMarker(from
                    .position(from_position)
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_green_placeholder)));
            request_accepted_map.addMarker(to
                    .position(to_position)
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_red_placeholder)));

            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(from_position)
                    .include(to_position)
                    .build();
            // Move camera to include both points
            request_accepted_map.setPadding(     0,      350,      0,     0);
            request_accepted_map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200));
            drawRoute(from_position, to_position);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true; }
    /**
     * This is a method to set the model of marker
     */

    // Convert vector drawable to bitmap
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /**
     * This is a method to draw route between two positions
     * @param p1
     * first place
     * @param p2
     * second place
     */

    private void drawRoute(LatLng p1, LatLng p2) {
        GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                .from(p1)
                .to(p2)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction) {
                        if(direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> pointList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter
                                    .createPolyline(RiderAfterAcceptRequest.this, pointList, 5,
                                            getResources().getColor(R.color.yellow));
                            request_accepted_map.addPolyline(polylineOptions);
                        } else {
                            String text = direction.getStatus();
                            Toast toast = Toast.makeText(RiderAfterAcceptRequest.this, text, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        String text = "Failed to get direction";
                        Toast toast = Toast.makeText(RiderAfterAcceptRequest.this, text, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    }
                });
    }




}