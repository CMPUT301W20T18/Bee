package com.example.bee;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PopUpMap extends FragmentActivity implements OnMapReadyCallback{
    private FusedLocationProviderClient client_device;
    GoogleMap mapPop;
    MarkerOptions place1;
    MarkerOptions place2;
    Boolean drew = false;
    Button AcceptButton;
    Button CancelButton;
    private TextView RequestMoneyAmount;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_map);
        initMap();
        AcceptButton = findViewById(R.id.accept_button);
        CancelButton = findViewById(R.id.cancel_button);
        RequestMoneyAmount = findViewById(R.id.request_money_amount1);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * .6));

        AcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(PopUpMap.this, WaitingForRider.class);
                startActivity(intent);
            }
        });
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(PopUpMap.this, WaitingForRider.class);
                startActivity(intent);
            }
        });


    }
    public void SetMoneyAmount(double MoneyAmount){
        RequestMoneyAmount.setText("$ "+ MoneyAmount);
    }

    private void initMap(){
        Log.d(TAG, "Initializing map");
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map_pop);

        supportMapFragment.getMapAsync(PopUpMap.this);

    }
//    @Override
    public void onMapReady(GoogleMap googleMap){
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready");
        mapPop = googleMap;

        LatLng place1_position = new LatLng(53.523220, -113.526321);
        LatLng place2_position = new LatLng(53.484300,-113.517250);

        place1 = new MarkerOptions().position(place1_position).title("Starting position");

        place2 = new MarkerOptions().position(place2_position).title("Destination");

        drew = getPoints(place1, place2);

        if (!drew) {
            String text = "Invalid Address";
            Toast toast = Toast.makeText(PopUpMap.this, text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }
    }



    private boolean getPoints(MarkerOptions fromAddress, MarkerOptions toAddress) {


        try {
            // May throw an IOException
            LatLng from_position = fromAddress.getPosition();
            LatLng to_position = toAddress.getPosition();
            mapPop.addMarker(fromAddress.position(from_position)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            mapPop.addMarker(toAddress.position(to_position)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(from_position)
                    .include(to_position)
                    .build();
            System.out.println("iopiop");
            mapPop.setPadding(0, 150, 0, 0);
            mapPop.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200));
            drawRoute(from_position, to_position);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }







    private void drawRoute(LatLng p1, LatLng p2) {
        GoogleDirection.withServerKey(getString(R.string.google_api_key))
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
                            Info distanceInfo = leg.getDistance();
                            String distance = distanceInfo.getText();
                            Toast.makeText(PopUpMap.this, distance, Toast.LENGTH_SHORT).show();
                            PolylineOptions polylineOptions = DirectionConverter
                                    .createPolyline(PopUpMap.this, pointList, 5,
                                            getResources().getColor(R.color.yellow));
                            mapPop.addPolyline(polylineOptions);
//                            Route route = direction.getRouteList().get(0);
//                            Leg leg = route.getLegList().get(0);
//                            ArrayList<LatLng> pointList = leg.getDirectionPoint();
//                            PolylineOptions polylineOptions = DirectionConverter
//                                    .createPolyline(PopUpMap.this, pointList, 5,
//                                            getResources().getColor(R.color.yellow));
//                            mapPop.addPolyline(polylineOptions);
                        } else {
                            String text = direction.getStatus();
                            Toast toast = Toast.makeText(PopUpMap.this, text, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        String text = "Failed to get direction";
                        Toast toast = Toast.makeText(PopUpMap.this, text, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    }
                });

    }
}
