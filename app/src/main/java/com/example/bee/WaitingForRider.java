package com.example.bee;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
/**
 * waiting for the rider response
 */
public class WaitingForRider extends FragmentActivity implements OnMapReadyCallback {
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String originAddress;
    private String destAddress;
    TextView riderName;
    private DatabaseReference ref;
    TextView RequestMoneyAmount;
    GoogleMap map;
    TextView RequestStatus;
//    RiderDecision riderDecision;
    Boolean riderResponse = true;
    ArrayList<Request> request;
    RelativeLayout driverCard;
    Boolean myLocationPermission = false;
    MarkerOptions place1;
    MarkerOptions place2;
    Button finishButton;
    private String distance;
    private String time;
    double requestAmount;
    String passRiderName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_for_rider);
        // set up the map to the activity
        driverCard = findViewById(R.id.rider_card);

        RequestMoneyAmount = findViewById(R.id.request_money_amount2);
        SetMoneyAmount(requestAmount);
        initMap();
        riderResponse = true;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("requests").child("cGwYgfbxtjcMgFSWvGoZDbe6SSK2").child("request");


        riderName = findViewById(R.id.rider_name);
        finishButton = findViewById(R.id.finish_button);
        finishButton.setVisibility(View.GONE);
//        hide the finish button until the rider make response
        RequestStatus = findViewById(R.id.request_status);




        Bundle bundle = getIntent().getExtras();
        passRiderName = bundle.getString("passRiderName");
        String passMoneyAmount = bundle.getString("passMoneyAmount");
        RequestMoneyAmount.setText(passMoneyAmount);
        riderName.setText(passRiderName);





        // Depends rider response to process to next activity
        if(riderResponse){
            RequestStatus.setText("Confirmed ride offer");
            finishButton.setVisibility(View.VISIBLE);
            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Intent intent = new Intent(WaitingForRider.this, );
                    finish();
                }
            });
        }else if(!riderResponse){
            RequestStatus.setText("Declined ride offer");
            finishButton.setVisibility(View.VISIBLE);
            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Intent intent = new Intent(WaitingForRider.this, )
                    finish();
                }
            });

        }else{
            RequestStatus.setText("Waiting for comfirmation......");
        }
        driverCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WaitingForRider.this, SearchRide.class));
            }
        });


    }
    /**
     * This set up the amount of request money amount
     * @param MoneyAmount
     * This is a candidate money amount to store
     */
    public void SetMoneyAmount(double MoneyAmount){
        RequestMoneyAmount.setText("$ "+ MoneyAmount);
    }
    /**
     * This initialize the map for waiting riders
     */
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    private void initMap(){
        Log.d(TAG, "Initializing map");
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_requestAccepted);

        supportMapFragment.getMapAsync(WaitingForRider.this);

    }

    /**
     * This set up the map
     * @param googleMap
     * google map to display the map content
     */
    @Override
    public void onMapReady(GoogleMap googleMap){
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready");

        map = googleMap;
        LatLng place1_position = new LatLng(53.523220, -113.526321);
        LatLng place2_position = new LatLng(53.484300, -113.517250);
        getDeviceLocation();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
//        initialize the starting position and destination on the map displayed
        place1 = new MarkerOptions().position(place1_position).title("Starting position");

        place2 = new MarkerOptions().position(place2_position).title("Destination");

        boolean drew = getPoints(place1, place2);
//      display message of invalid address
        if (!drew) {
            String text = "Invalid Address";
            Toast toast = Toast.makeText(WaitingForRider.this, text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }
    }
    /**
     * This locate the current location for the user device
     */
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        FusedLocationProviderClient client_device = LocationServices.getFusedLocationProviderClient(this);
//        locate the current driver position
        try{
            if(myLocationPermission){
                final Task location = client_device.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();


                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(WaitingForRider.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    /**
     * This set up the marker points on the map
     * @param fromAddress
     * address of starting position
     * @param toAddress
     * address of the destination
     */

    private boolean getPoints(MarkerOptions fromAddress, MarkerOptions toAddress) {

        try {
            // May throw an IOException
            LatLng from_position = fromAddress.getPosition();
            LatLng to_position = toAddress.getPosition();
            map.addMarker(fromAddress.position(from_position)
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_red_placeholder)));

            map.addMarker(toAddress.position(to_position)
                    .icon(bitmapDescriptorFromVector(this,R.drawable.ic_green_placeholder)));
//            add markers on the map for starting position and ending position
            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(from_position)
                    .include(to_position)
                    .build();
            map.setPadding(0, 150, 0, 0);
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200));
            drawRoute(from_position, to_position);
//            generate the route between starting position and destination
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }




    /**
     * This return draw the route between two locations
     * @param p1
     * coordinate of starting location
     * @param p2
     * coordinate of destination
     */



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
                            Info durationInfo = leg.getDuration();
                            distance = distanceInfo.getText();
                            time = durationInfo.getText();
//                            display the route as line on the map
                            PolylineOptions polylineOptions = DirectionConverter
                                    .createPolyline(WaitingForRider.this, pointList, 5,
                                            getResources().getColor(R.color.route));
                            map.addPolyline(polylineOptions);
                        } else {
                            String text = direction.getStatus();
                            Toast toast = Toast.makeText(WaitingForRider.this, text, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        String text = "Failed to get direction";
                        Toast toast = Toast.makeText(WaitingForRider.this, text, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    }
                });

    }
}
