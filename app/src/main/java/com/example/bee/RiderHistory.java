package com.example.bee;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RiderHistory extends AppCompatActivity{
    private ArrayList<Request> requests;
    private ArrayAdapter<Request> rAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_history);

        Bundle bundle = getIntent().getExtras();
        userID = bundle.getString("riderID");

        ListView mList = findViewById(R.id.rList);
        requests = new ArrayList<>();
        rAdapter = new RequestAdapter(this, requests);
        mList.setAdapter(rAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("history").child(userID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Request request = dsp.getValue(Request.class);
                    if (request.getRiderID().equals(userID)) {
                        requests.add(request);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
