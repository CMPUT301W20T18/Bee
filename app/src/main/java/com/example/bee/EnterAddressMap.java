package com.example.bee;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EnterAddressMap extends FragmentActivity implements OnMapReadyCallback {

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMap map;
    private static final int REQUEST_CODE = 100;
    LatLng p1;
    LatLng p2;
    Boolean drew = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_address_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        final EditText fromEditText = findViewById(R.id.from_address);
        final EditText toEditText = findViewById(R.id.to_address);
        final Button confirmBtn = findViewById(R.id.confirm_route);
        // Hide confirm route button
        confirmBtn.setVisibility(View.GONE);
        Button showBtn = findViewById(R.id.show_route);


        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromString = fromEditText.getText().toString();
                String toString = toEditText.getText().toString();
                if (!fromString.isEmpty() && !toString.isEmpty()){
                    map.clear();
                    drew = getPoints(fromString, toString);
                    if (!drew){
                        String text = "Invalid Address";
                        Toast toast = Toast.makeText(EnterAddressMap.this, text, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        confirmBtn.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private boolean getPoints(String fromString, String toString) {
        Geocoder coder = new Geocoder(this);
        List<Address> fromAddress;
        List<Address> toAddress;

        try {
            // May throw an IOException
            fromAddress = coder.getFromLocationName(fromString, 5);
            if (fromAddress == null) {
                return false;
            }
            toAddress = coder.getFromLocationName(toString, 5);
            if (toAddress == null) {
                return false;
            }

            Address fromLocation = fromAddress.get(0);
            Address toLocation = toAddress.get(0);
            p1 = new LatLng(fromLocation.getLatitude(), fromLocation.getLongitude());

            map.addMarker(new MarkerOptions()
                    .position(p1)
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_green_placeholder)));
            p2 = new LatLng(toLocation.getLatitude(), toLocation.getLongitude());
            map.addMarker(new MarkerOptions()
                    .position(p2)
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_red_placeholder)));

            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(p1)
                    .include(p2)
                    .build();
            // Move camera to include both points
            map.setPadding(     0,      350,      0,     0);
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200));
            drawRoute(p1, p2);

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    // Convert vector drawable to bitmap
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void drawRoute(LatLng p1, LatLng p2) {
        GoogleDirection.withServerKey(getString(R.string.web_api_key))
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
                            Toast.makeText(EnterAddressMap.this, distance, Toast.LENGTH_SHORT).show();
                            PolylineOptions polylineOptions = DirectionConverter
                                    .createPolyline(EnterAddressMap.this, pointList, 5,
                                            getResources().getColor(R.color.yellow));
                            map.addPolyline(polylineOptions);
                        } else {
                            String text = direction.getStatus();
                            Toast toast = Toast.makeText(EnterAddressMap.this, text, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        String text = "Failed to get direction";
                        Toast toast = Toast.makeText(EnterAddressMap.this, text, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    }
                });
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.rider_initial_map);
                    mapFragment.getMapAsync(EnterAddressMap.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
            }
        }
    }
}
