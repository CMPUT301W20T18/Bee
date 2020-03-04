package com.example.bee;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
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
    double dist;

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
                hideSoftKeyboard(EnterAddressMap.this);
                String fromString = fromEditText.getText().toString();
                String toString = toEditText.getText().toString();
                if (!fromString.isEmpty() && !toString.isEmpty()){
                    if (map != null) {
                        map.clear();
                    }
                    drew = getPoints(fromString, toString);
                    if (!drew){
                        String text = "Invalid Address";
                        Toast toast = Toast.makeText(EnterAddressMap.this, text, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        confirmBtn.setVisibility(View.GONE);
                    } else {
                        confirmBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    String text = "Please fill in the address";
                    Toast toast = Toast.makeText(EnterAddressMap.this, text, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetCost(dist*2).show(getSupportFragmentManager(), "set_cost");
            }
        });
    }

    /*
    StackOverflow post by Navneeth G https://stackoverflow.com/users/1135909/navneeth-g
    Answer https://stackoverflow.com/a/11656129

    Hides keyboard after pressing show routes button
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    // Using Geocoder to convert address to latitude and longitude
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
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_red_placeholder)));
            p2 = new LatLng(toLocation.getLatitude(), toLocation.getLongitude());
            map.addMarker(new MarkerOptions()
                    .position(p2)
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_green_placeholder)));
            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(p1)
                    .include(p2)
                    .build();

            // Move camera to include both points
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

    /*
    StackOverflow post by Leo Droidcoder https://stackoverflow.com/users/5730321/leo-droidcoder
    Answer https://stackoverflow.com/a/45564994

    Convert vector drawable to bitmap
     */
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /*
    Github libray by Akexorcist https://github.com/akexorcist
    Library page: https://github.com/akexorcist/Android-GoogleDirectionLibrary
     */
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
                            dist = Double.parseDouble(distance.substring(0, distance.length() - 3));
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
        map.setPadding(     0,      400,      0,     50);
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
