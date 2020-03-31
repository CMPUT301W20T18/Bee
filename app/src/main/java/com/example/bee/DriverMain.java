package com.example.bee;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 *This is a class that used for Driver, the map with current location
 */

// This class is used to display driver's map
// it requests permission of GPS/location usage to get current location
// and move camero to it
// issue so far is current location is set to googleplex
// instead of real current location


public class DriverMain extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    GoogleMap map;
    Location currentPos;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 100;
    private FirebaseAuth firebaseAuth;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private String uid;
    private UserProfile profile;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_with_drawer);

        //mapFragment.getMapAsync(this);
        ImageView profileButton = findViewById(R.id.profile_btn);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

        // assign current user id and profile object
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        profile = new UserProfile(uid);




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


//        profileButton.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                Intent profile = new Intent(DriverMain.this,DrawerActivity.class);
//                startActivity(profile);
//            }
//        });

        TextView displayName = navigationView.getHeaderView(0).findViewById(R.id.profileName);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display drawer activity
                displayName.setText(String.format("%s %s", profile.getFirstName(), profile.getLastName()));
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.profile:
                Toast.makeText(DriverMain.this, "Profile Selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DriverMain.this, EditProfileActivityD.class));
                return true;
            case R.id.rating:
                Toast.makeText(DriverMain.this, "My Rating Selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DriverMain.this, RatingActivity.class));
                return true;
            case R.id.history:
                Toast.makeText(DriverMain.this, "History Selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DriverMain.this, DriverHistory.class));
                return true;
            case R.id.sRider:
                Toast.makeText(DriverMain.this, "Switch to Rider", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DriverMain.this, EnterAddressMap.class));
                return true;
            case R.id.sDriver:
                Toast.makeText(DriverMain.this, "Switch to Driver", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DriverMain.this, DriverMain.class));
                return true;
            case R.id.logout:
                Toast.makeText(DriverMain.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DriverMain.this, LoginActivity.class));
                firebaseAuth.signOut();
                finish();
                return true;

            default:
                break;
        }
        return false;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
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

    /**
     * This initialize google map and set & move camera to current location
     * @param googleMap
     *  This is the googleMap parameter
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latlng = new LatLng(currentPos.getLatitude(),currentPos.getLongitude());
        //LatLng place = new LatLng(53.486094, -113.506367);
        map.setMyLocationEnabled(true);
        //map.addMarker(new MarkerOptions().position(latlng).title("HI"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,15));
    }


    /**This method is asking for permission of locating services
     *
     * @param requestCode
     *  request Code
     * @param permissions
     *  permission variable String
     * @param grantResults
     * grantResult nonNull int
     */

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
