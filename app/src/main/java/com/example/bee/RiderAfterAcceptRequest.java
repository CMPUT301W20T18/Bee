package com.example.bee;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.util.DirectionConverter;
import com.github.clans.fab.FloatingActionButton;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
/**
 * This is a class that shows the situation after the rider confirms the driver's acceptance
 */

public class RiderAfterAcceptRequest extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "accept_request_activity";

    GoogleMap request_accepted_map;
    TextView driver_name, telephone, t_up, t_down;

    ArrayList<String> points = new ArrayList<>();
    MarkerOptions ori, dest;

    private FirebaseUser user;
    FirebaseDatabase db;

    FloatingActionButton fabConfirm, fabCancel;

    //private static final String rq_id = "PAvxlWke8KfOtRbuXuqo6TheIrw1";

    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_after_accept_request);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        //db.setPersistenceEnabled(true);
        initMap();
        driver_name = (TextView)findViewById(R.id.driver_name);
        driver_name = (TextView)findViewById(R.id.driver_name);
        telephone = findViewById(R.id.telephone);
        t_up = findViewById(R.id.up_num);
        t_down = findViewById(R.id.down_num);

        driver_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiderAfterAcceptRequest.this, DriverBasicInformation.class);
                startActivity(intent);
            }
        });

        fabCancel = findViewById(R.id.my_cancel);
        fabConfirm = findViewById(R.id.my_confirm);
        fabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog();
            }
        });
        fabConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });

    }

    private void showDriver(String dr){
        DatabaseReference ref = db.getReference("users");
        ref.child(dr)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);
                        String phone = dataSnapshot.child("phone").getValue(String.class);
                        long up = (long)dataSnapshot.child("thumbUp").getValue();
                        long down = (long)dataSnapshot.child("thumbDown").getValue();
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String r_drName = firstName+lastName;
                        driver_name.setText(r_drName);
                        telephone.setText(phone);
                        t_up.setText(String.valueOf(up));
                        t_down.setText(String.valueOf(down));
                        saveDriver(r_drName,phone,email,String.valueOf(up),String.valueOf(down));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void getDriver(){
        String userID = user.getUid();
        DatabaseReference ref = db.getReference("requests");
        ref.child(userID)
                .child("request")
                .child("driverID")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String drID = dataSnapshot.getValue(String.class);
                        if(drID == null){
                            Log.d(TAG, "Data error");
                        }else{
                            showDriver(drID);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void saveDriver(String name,String phone,String mail,String up,String down){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dr_name",name);
        editor.putString("phone",phone);
        editor.putString("mail",mail);
        editor.putString("up",up);
        editor.putString("down",down);
        Toast.makeText(this, "Driver saved", Toast.LENGTH_SHORT).show();
        editor.apply();
    }

    private void loadDriver(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String name = sharedPreferences.getString("dr_name", "");
        String phone = sharedPreferences.getString("phone", "");
        String mail = sharedPreferences.getString("mail", "");
        String up = sharedPreferences.getString("up", "");
        String down = sharedPreferences.getString("down", "");
        if(!name.equals("") && !phone.equals("") && !mail.equals("") && !up.equals("")&&!down.equals("")){
            driver_name.setText(name);
            telephone.setText(phone);
            t_up.setText(up);
            t_down.setText(down);
        }else{
            Toast.makeText(this, "data error", Toast.LENGTH_SHORT).show();
        }
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

        boolean result = isNetworkAvailable();
        if (!result){
            Toast toast = Toast.makeText(RiderAfterAcceptRequest.this, "You are offline", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            loadOriDest();
            loadRoute();
            loadDriver();
        }else{
            setOriDest();
            drawPointsList();
            getDriver();
        }

        //removeRequest();
    }


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

//    /**
//     * This is a method to draw route between two positions
//     * @param p1
//     * first place
//     * @param p2
//     * second place
//     */

    private void drawRoute(ArrayList<LatLng> pointList) {
        PolylineOptions polylineOptions = DirectionConverter
                .createPolyline(RiderAfterAcceptRequest.this, pointList, 5,
                        getResources().getColor(R.color.route));
        request_accepted_map.addPolyline(polylineOptions);
    }

    private void drawPointsList(){
        String userID = user.getUid();
        DatabaseReference ref = db.getReference("requests");
        //ref.child(rq_id)
        ref.child(userID)
                .child("request")
                .child("points")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> items = dataSnapshot.getChildren();
                        for(DataSnapshot child: items){
                            String latlng_str = child.getValue(String.class);
                            points.add(latlng_str);
                        }

                        ArrayList<LatLng> formal_points = new ArrayList<>();
                        for(String point : points){
                            String[] parts = point.split(",");
                            double lat = Double.parseDouble(parts[0]);
                            double lng = Double.parseDouble(parts[1]);
                            formal_points.add(new LatLng(lat,lng));
                        }
                        drawRoute(formal_points);
                        saveRoute(formal_points);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void showCancelDialog(){
        final String userID = user.getUid();

        AlertDialog.Builder builder = new AlertDialog.Builder(RiderAfterAcceptRequest.this);
        View view = getLayoutInflater().inflate(R.layout.activity_rider_after_accept_request_dialog,null);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        title.setText("Are you sure you want to cancel the ride? ");
        Button not_cancelBtn = view.findViewById(R.id.not_cancel_btn);
        Button cancelBtn = view.findViewById(R.id.do_cancel_btn);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(RiderAfterAcceptRequest.this, "hohoho", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                DatabaseReference ref = db.getReference("requests").child(userID).child("request").child("cancel");
                ref.setValue(true);
                startActivity(new Intent(RiderAfterAcceptRequest.this, EnterAddressMap.class));
            }
        });
        not_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    private void showConfirmDialog(){
        Context mcontext = RiderAfterAcceptRequest.this;
        final String userID = user.getUid();

        AlertDialog.Builder builder = new AlertDialog.Builder(RiderAfterAcceptRequest.this);
        View view = getLayoutInflater().inflate(R.layout.activity_rider_after_accept_request_dialog,null);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        title.setText("Are you sure you reach the destination? ");
        Button cancelBtn = view.findViewById(R.id.not_cancel_btn);
        Button confirmBtn = view.findViewById(R.id.do_cancel_btn);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = db.getReference("requests");
                //ref.child(rq_id)
                ref.child(userID)
                        .child("request").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Request r = dataSnapshot.getValue(Request.class);
                        if (r == null){
                            return;
                        }
                        boolean is_reach = r.getReached();
                        if (!is_reach){
                            Toast.makeText(mcontext, "Driver has not reach the destination!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else{
                            DatabaseReference ref2 = db.getReference("requests").child(userID).child("request").child("finished");
                            ref2.setValue(true);
                            Intent intent = new Intent(RiderAfterAcceptRequest.this, RiderPayActivity.class);
                            intent.putExtra("DriverID", r.getDriverID());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    //https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setOriDest(){
        String userID = user.getUid();
        //Context mcontext = RiderAfterAcceptRequest.this;
        DatabaseReference ref = db.getReference("requests");
        ref.child(userID)
                .child("request")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Request r = dataSnapshot.getValue(Request.class);
                        if (r == null){
                            Toast.makeText(RiderAfterAcceptRequest.this, "Request is cancelled", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String ori_list = r.getOriginLatlng();
                        String dest_list = r.getDestLatlng();
                        String ori_name = r.getOrigin();
                        String dest_name = r.getDest();
                        addSign(ori_list,dest_list,ori_name,dest_name);
                        saveOriDest(ori_list,dest_list,ori_name,dest_name);
                        if (r.getReached()){
                            Toast.makeText(RiderAfterAcceptRequest.this, "Driver has arrived the destination, please confirm", Toast.LENGTH_SHORT).show();

                        }

//                    ArrayList<String> points = r.getPoints();
//                    if(r.getPoints() == null){
//                        Toast.makeText(RiderAfterAcceptRequest.this, "NO!!!!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
//                    }else{
//                        for(String i : points){
//                            Toast.makeText(RiderAfterAcceptRequest.this, i, Toast.LENGTH_SHORT).show();
//                        }
//                    }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void addSign(String ori_list, String dest_list,String ori_name, String dest_name){
        Context mcontext = RiderAfterAcceptRequest.this;
        String[]ori_parts = ori_list.split(",");
        double ori_lat = Double.parseDouble(ori_parts[0]);
        double ori_lng = Double.parseDouble(ori_parts[1]);
        ori = new MarkerOptions().position(new LatLng(ori_lat,ori_lng)).title(ori_name);
        request_accepted_map.addMarker(ori
                .position(ori.getPosition())
                .icon(bitmapDescriptorFromVector(mcontext, R.drawable.ic_green_placeholder)));

        String[]parts = dest_list.split(",");
        double dest_lat = Double.parseDouble(parts[0]);
        double dest_lng = Double.parseDouble(parts[1]);
        dest = new MarkerOptions().position(new LatLng(dest_lat,dest_lng)).title(dest_name);
        request_accepted_map.addMarker(dest
                .position(dest.getPosition())
                .icon(bitmapDescriptorFromVector(mcontext, R.drawable.ic_red_placeholder)));
        if(dest != null && ori != null){
            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(ori.getPosition())
                    .include(dest.getPosition())
                    .build();
            //Move camera to include both points
            request_accepted_map.setPadding(     0,      350,      0,     0);
            request_accepted_map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200));
        }
    }

    private void saveOriDest(String ori_list, String dest_list, String ori_name,String dest_name){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ori_list",ori_list);
        editor.putString("dest_list",dest_list);
        editor.putString("ori_name",ori_name);
        editor.putString("dest_name",dest_name);
        editor.apply();

    }


    private void loadOriDest(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String ori_list = sharedPreferences.getString("ori_list", "");
        String dest_list = sharedPreferences.getString("dest_list", "");
        String ori_name = sharedPreferences.getString("ori_name", "");
        String dest_name = sharedPreferences.getString("dest_name", "");
        if(!ori_list.equals("") && !dest_list.equals("") && !ori_name.equals("") && !dest_name.equals("")){
            addSign(ori_list,dest_list,ori_name,dest_name);
        }else{
            Toast.makeText(this, "Data error", Toast.LENGTH_SHORT).show();
        }

    }



    private void saveRoute(ArrayList<LatLng> points){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(points);
        editor.putString("route",json);
        editor.apply();
        Toast.makeText(RiderAfterAcceptRequest.this, "save route", Toast.LENGTH_SHORT).show();
    }

    private void loadRoute(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("route",null);
        Type type = new TypeToken<ArrayList<LatLng>>() {}.getType();
        ArrayList<LatLng> store_points = gson.fromJson(json,type);

        if(store_points == null){
            Toast.makeText(RiderAfterAcceptRequest.this, "no data!", Toast.LENGTH_SHORT).show();
        }else{
            drawRoute(store_points);
        }

    }

    private void removeRequest(){
        DatabaseReference ref = db.getReference("requests").child("0bEdwmBMMpSuzycdNfJn0EAvWiw1");

        ref.removeValue();

    }
}