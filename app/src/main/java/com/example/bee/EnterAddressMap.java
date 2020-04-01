package com.example.bee;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *  This class takes user input of addresses and show the route on the map
 */
public class EnterAddressMap extends FragmentActivity implements OnMapReadyCallback,
        SetCost.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "TAG";
    private static final int REQUEST_CODE = 100;
    private FirebaseDatabase database;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap map;
    private EditText fromEditText;
    private EditText toEditText;
    private Button confirmBtn;
    private String originAddress;
    private String destAddress;
    private LatLng p1;
    private LatLng p2;
    private int markers;
    private Boolean drew = false;
    private double oldCost;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String userID;
    private Routes routes;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        database = Utils.getDatabase();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        UserProfile profile = new UserProfile(userID);
        firebaseAuth = FirebaseAuth.getInstance();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        TextView displayName = navigationView.getHeaderView(0).findViewById(R.id.profileName);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        fromEditText = findViewById(R.id.from_address);
        toEditText = findViewById(R.id.to_address);
        confirmBtn = findViewById(R.id.confirm_route);
        // Hide confirm route button
        confirmBtn.setVisibility(View.GONE);
        Button showBtn = findViewById(R.id.show_route);
        ImageView profileBtn = findViewById(R.id.profile_btn);

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(EnterAddressMap.this);
                originAddress = fromEditText.getText().toString();
                destAddress = toEditText.getText().toString();
                if (!originAddress.isEmpty() && !destAddress.isEmpty()){
                    if (map != null) {
                        map.clear();
                    }

                    // drew is true if no exception is shown and the route is drawn
                    drew = getPoints(originAddress, destAddress);
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
                    // At least one of the editText is empty
                    String text = "Please fill in the address";
                    Toast toast = Toast.makeText(EnterAddressMap.this, text, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display name in drawer activity
                displayName.setText(profile.getFirstName()+" "+profile.getLastName());
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        // Confirm button for confirming route, invokes set cost dialog
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldCost = routes.getCost();
                new SetCost(oldCost).show(getSupportFragmentManager(), "set_cost");
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.profile:
                Toast.makeText(EnterAddressMap.this, "Profile Selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EnterAddressMap.this, EditProfileActivity.class));
                return true;
            case R.id.rating:
                Toast.makeText(EnterAddressMap.this, "My Rating Selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EnterAddressMap.this, RatingActivity.class));
                return true;
            case R.id.history:
                Toast.makeText(EnterAddressMap.this, "History Selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EnterAddressMap.this, ConfirmOfferDialog.class);
                intent.putExtra("riderID",userID);
                startActivity(intent);
                return true;
            case R.id.sRider:
                Toast.makeText(EnterAddressMap.this, "Switch to Rider", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EnterAddressMap.this, EnterAddressMap.class));
                return true;
            case R.id.sDriver:
                Toast.makeText(EnterAddressMap.this, "Switch to Driver", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EnterAddressMap.this, DriverMain.class));
                return true;
            case R.id.logout:
                Toast.makeText(EnterAddressMap.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EnterAddressMap.this, LoginActivity.class));
                firebaseAuth.signOut();
                finish();
                return true;

            default:
                break;
        }
        return false;
    }

    /*
    StackOverflow post by Navneeth G https://stackoverflow.com/users/1135909/navneeth-g
    Answer https://stackoverflow.com/a/11656129

    Hides keyboard after pressing show routes button
     */
    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Get latitude and longitude from the input location and call draw route function
     * @param originString
     * @param destString
     * @return whether or not the route is successfully drawn
     */
    /*
    Github library for Google Maps API Web Services by Google Maps https://github.com/googlemaps
    Library page: https://github.com/googlemaps/google-maps-services-java
     */
    private boolean getPoints(String originString, String destString) {
        LocationConverter converter = new LocationConverter(getApplicationContext());
        p1 = converter.addressToLatlng(originString);
        p2 = converter.addressToLatlng(destString);

        if (p1 == null || p2 == null) {
            return false;
        }

        map.addMarker(new MarkerOptions()
                .position(p1)
                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_red_placeholder)));

        map.addMarker(new MarkerOptions()
                .position(p2)
                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_green_placeholder)));
        markers = 2;
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(p1)
                .include(p2)
                .build();

        // Move camera to include both points
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200));
        routes = new Routes(EnterAddressMap.this, map, p1, p2);
        routes.drawRoute();
        return true;

    }

    /*
    StackOverflow post by Leo Droidcoder https://stackoverflow.com/users/5730321/leo-droidcoder
    Answer https://stackoverflow.com/a/45564994

    Convert vector drawable to bitmap for markers
     */
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /**
     * Show the last location of the device on the map
     */
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
                } else {
                    String text = "Poor network connection";
                    Toast toast = Toast.makeText(EnterAddressMap.this, text, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
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

    @Override
    public void onResume() {
        super.onResume();
        DatabaseReference ref = database.getReference("requests").child(userID).child("request");
        // Check if the user has post any request
        // If there is a request then jump to the correct activity
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Boolean status = dataSnapshot.child("status").getValue(Boolean.class);
                    Boolean reached = dataSnapshot.child("reached").getValue(Boolean.class);
                    if (!status) {
                        startActivity(new Intent(EnterAddressMap.this, WaitingForDriver.class));
                    } else if (!reached) {
                        startActivity(new Intent(EnterAddressMap.this, RiderAfterAcceptRequest.class));
                    }
                } else {
                    CheckNetwork check = new CheckNetwork(getApplicationContext());
                    boolean result = check.isNetworkAvailable();
                    if (!result){
                        String text = "You are offline, some functionalities may be unavailable";
                        Toast toast = Toast.makeText(EnterAddressMap.this, text, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
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

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                confirmBtn.setVisibility(View.GONE);

                // If two markers are on the map then clear everything
                if (markers == 2) {
                    markers = 0;
                    map.clear();
                    fromEditText.setText("");
                    toEditText.setText("");
                }

                // Fill in the address box
                String address;
                String inputFrom = fromEditText.getText().toString();
                String inputTo = toEditText.getText().toString();
                if (inputFrom.isEmpty()) {
                    // First text box is empty
                    // if both text box is empty, the first text box has priority
                    LocationConverter converter = new LocationConverter(getApplicationContext());
                    address = converter.latlngToAddress(latLng);
                    fromEditText.setText(address);
                    map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(bitmapDescriptorFromVector(EnterAddressMap.this, R.drawable.ic_red_placeholder)));
                    markers += 1;

                } else if (!inputFrom.isEmpty() && inputTo.isEmpty()) {
                    // destination text box is empty
                    LocationConverter converter = new LocationConverter(getApplicationContext());
                    address = converter.latlngToAddress(latLng);
                    toEditText.setText(address);
                    map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(bitmapDescriptorFromVector(EnterAddressMap.this, R.drawable.ic_green_placeholder)));
                    markers += 1;
                }
            }
        });
    }

    /**
     * Ask for user's permission to use their location
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
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

    /**
     * Post request to Firebase with possibly increased cost from user
     * @param newCost
     */
    @Override
    public void postRequest(double newCost) {
        DatabaseReference requestRef = database.getReference("requests").child(userID);
        HashMap<String,Request> request = new HashMap<>();

        // Get the points of route in String
        ArrayList<String> points = routes.getPointList();

        // Convert Latlng of locations to String
        String p1Latlng = String.format("%f,%f",p1.latitude,p1.longitude);
        String p2Latlng = String.format("%f,%f",p2.latitude,p2.longitude);
        request.put("request", new Request(userID, originAddress, destAddress, p1Latlng, p2Latlng,
                points, routes.getDistance(), routes.getTime(), newCost));
        requestRef.setValue(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: request posted for"+ userID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });

        startActivity(new Intent(EnterAddressMap.this, WaitingForDriver.class));
    }

    @Override
    public void onBackPressed() {} // Prevent activity from going back to login
}
