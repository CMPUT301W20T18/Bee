package com.example.bee;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

    private static final String TAG = "TAG";
    private FirebaseUser fDriver;
    private String driverId;
    private DatabaseReference ref;
    private String from;
    private String to;

    Request request;

    EditText searchNearby;
    Button searchButton;
    Button backButton;
    ArrayAdapter<Offer> offerAdapter;
    ArrayList<Offer> offerInfo;
    ArrayList<Request> request_list;
    ListView offerList;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_search);

        searchNearby = findViewById(R.id.searchNearBy);
        searchButton = findViewById(R.id.searchButton);

        offerList = findViewById(R.id.offer_list);
        offerInfo = new ArrayList<>();

        offerAdapter = new CustomList(this,offerInfo);
        offerList.setAdapter(offerAdapter);


//        fDriver = FirebaseAuth.getInstance().getCurrentUser();
//        driverId = fDriver.getUid();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("requests").child("cGwYgfbxtjcMgFSWvGoZDbe6SSK2").child("request");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                request = dataSnapshot.getValue(Request.class);

                offerInfo.add(new Offer(request.getOrigin(),request.getDest(),String.valueOf(request.getCost()),request.getOriginLatlng(),request.getRiderID()));
                offerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
