package com.example.bee;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

/**
 * Pop up the map windows to display locations and route
 */
public class PopUpMap extends FragmentActivity implements OnMapReadyCallback{
    private FusedLocationProviderClient client_device;
    EditText riderID, requestMoneyAmount;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    String userID;
    private FirebaseDatabase firebaseDatabase;
    GoogleMap mapPop;
    MarkerOptions place1;
    private DatabaseReference ref;

    MarkerOptions place2;
    Boolean drew = false;
    Button AcceptButton;
    Button CancelButton;
    private TextView RequestMoneyAmount;


        @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
            riderID = findViewById(R.id.rider_id);
            requestMoneyAmount = findViewById(R.id.money_amount_in_pop);
            super.onCreate(savedInstanceState);

            user = firebaseAuth.getInstance().getCurrentUser();
            userID = user.getUid();




            FirebaseDatabase database = FirebaseDatabase.getInstance();
            ref = database.getReference("requests").child(userID).child("request");







            setContentView(R.layout.pop_up_map);
            initMap();


//            setUpGUI();
            AcceptButton = findViewById(R.id.accept_button);
            CancelButton = findViewById(R.id.cancel_button);
//        initialize the map as a pop up window
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

//            requestMoneyAmount.setText("1");
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;

            getWindow().setLayout((int) (width * 0.8), (int) (height * .6));
//        set up the accept button
            AcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    String MoneyString = requestMoneyAmount.getText().toString();
//                    if (!MoneyString.isEmpty()) {
//                        double newCost = Double.parseDouble(MoneyString);
                        startActivity(new Intent(PopUpMap.this, WaitingForRider.class));
//                    } else {
//                        String text = "Invalid Amount";
//                        Toast toast = Toast.makeText(PopUpMap.this, text, Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.TOP, 0, 0);
//                        toast.show();
//                        final Intent show = new Intent(PopUpMap.this, WaitingForRider.class);
//                        startActivity(show);
//                    }


                }
            });
//        set up the cancel button
            CancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent cancel = new Intent(PopUpMap.this, SearchRide.class);
                    startActivity(cancel);
                }
            });


        }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
//        set up the User information on the interface
//    public void setUpGUI() {
////        user = FirebaseAuth.getInstance().getCurrentUser();
////        if(user != null){
////            userID = firebaseAuth.getCurrentUser().getUid();
////        }
////
////        final FirebaseDatabase database = FirebaseDatabase.getInstance();
////        final DatabaseReference ref = database.getReference("users");
////        ref.child(userID).addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                    riderID.setText(userID);
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////            }
////        });
//        userID =
//        riderID.setText(userID);
//
//    }

    public void SetMoneyAmount(double MoneyAmount){
        RequestMoneyAmount.setText("$ "+ MoneyAmount);
    }
    /**
     * This initialize the map to start
     */
    private void initMap(){
        Log.d(TAG, "Initializing map");
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map_pop);
//        synchronize the map in the activity
        supportMapFragment.getMapAsync(PopUpMap.this);

    }
    /**
     * This locate the current location for the user device
     */

//    https://stackoverflow.com/questions/30708036/delete-the-last-two-characters-of-the-string
    @Override
    public void onMapReady(GoogleMap googleMap){
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready");
        mapPop = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot s : dataSnapshot.getChildren()){
//                    Request request = s.getValue(Request.class);
//                    String originTempString = ref.child("originLatlng").getKey();
//                    String originString = originTempString.substring(1).substring(0, originTempString.length() - 2 );
//
//                    String[] afterSplitLoc = originString.split(",");
//
//                    double originLatitude = Double.parseDouble(afterSplitLoc[0]);
//
//                    double originLongitude = Double.parseDouble(afterSplitLoc[1]);
//                    LatLng originCoordinate = new LatLng(originLatitude,originLongitude);
//
//
//
//
//
//
//
//
//
//
//
//
//                    place1 = new MarkerOptions().position(originCoordinate).title("Starting position");
//
//                    String destTempString = ref.child("dest").getKey();
//                    String destString = destTempString.substring(1).substring(0, destTempString.length() - 2 );
//
//                    String[] afterSplitLoc1 = originString.split(",");
//
//                    double destLatitude = Double.parseDouble(afterSplitLoc1[0]);
//
//                    double deseLongitude = Double.parseDouble(afterSplitLoc[1]);
//                    LatLng destCoordinate = new LatLng(originLatitude,originLongitude);
//
//
//
//                    place2 = new MarkerOptions().position(destCoordinate).title("Destination");
//
//
//
//
//
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });





















        LatLng place1_postion = new LatLng(53.523220,-113.526321);
        place1 = new MarkerOptions().position(place1_postion).title("Orientation");
        LatLng place2_postion = new LatLng(53.484300,-113.517250);
        place2 = new MarkerOptions().position(place2_postion).title("Destination");

//        initialize the starting position and destination
//        LatLng place1_position = new LatLng(53.523220, -113.526321);


        drew = getPoints(place1, place2);

        if (!drew) {
            String text = "Invalid Address";
            Toast toast = Toast.makeText(PopUpMap.this, text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

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

            mapPop.addMarker(fromAddress.position(from_position)
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_red_placeholder)));
            mapPop.addMarker(toAddress.position(to_position)
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_green_placeholder)));
//            display the two locations as the marker in the map
            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(from_position)
                    .include(to_position)
                    .build();
            mapPop.setPadding(0, 150, 0, 0);
            mapPop.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200));
            drawRoute(from_position, to_position);

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
                            Info distanceInfo = leg.getDistance();
                            String distance = distanceInfo.getText();
                            Toast.makeText(PopUpMap.this, distance, Toast.LENGTH_SHORT).show();
                            PolylineOptions polylineOptions = DirectionConverter
                                    .createPolyline(PopUpMap.this, pointList, 5,
                                            getResources().getColor(R.color.route));
                            mapPop.addPolyline(polylineOptions);
//                            display the route as line on the map
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
//fail to generate route between locations
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
