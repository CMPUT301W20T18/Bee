package com.example.bee;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchRide extends AppCompatActivity {


//    initializing local variables
    private static final String TAG = "TAG";
    private FirebaseUser fDriver;
    private String driverId;
    private DatabaseReference ref;
    private String from;
    private String to;
    Request request;

    EditText searchNearby;
    ImageView searchButton;
    ImageView backButton;
    ArrayAdapter<Offer> offerAdapter;
    ArrayList<Offer> offerInfo;
    ArrayList<Request> request_list;
    ListView offerList;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_search);

//        setup buttons in current view
        searchNearby = findViewById(R.id.searchNearBy);

//        setup the listview for current requests
        offerList = findViewById(R.id.offer_list);
        offerInfo = new ArrayList<>();

//        setup list adapter
        offerAdapter = new CustomList(this,offerInfo);
        offerList.setAdapter(offerAdapter);

//      connect to firebase realtime database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("requests");


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Using for loop to obtain all current reqeusts in database and add them into list view
                for(DataSnapshot dsp:dataSnapshot.getChildren()){
                    request = dsp.child("request").getValue(Request.class);

                    offerInfo.add(new Offer(request.getOrigin(),request.getDest(),String.format("%.2f", request.getCost()),request.getOriginLatlng(),request.getRiderID()));
//                  notify adapter to update listview
                    offerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        Going back to driver's main activity
        backButton = findViewById(R.id.backButtonDriver);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent driverMain = new Intent(SearchRide.this, DriverMain.class);
                startActivity(driverMain);
            }
        });
    }

}
