package com.example.bee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class WaitingForDriver extends AppCompatActivity {
    private static final String TAG = "TAG";
    private FirebaseDatabase database;
    private FirebaseUser user;
    private String userID;
    private String origin;
    private String dest;
    private double cost;
    TextView toText;
    TextView fromText;
    TextView costText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_driver);
        toText = findViewById(R.id.show_to);
        fromText = findViewById(R.id.show_from);
        costText = findViewById(R.id.show_cost);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("requests").child(userID).child("request");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*for (DataSnapshot s: dataSnapshot.getChildren()) {
                    origin = s.child("origin").getValue(String.class);
                    dest = s.child("dest").getValue(String.class);
                    cost = s.child("cost").getValue(Double.class);
                    Toast.makeText(WaitingForDriver.this, dest, Toast.LENGTH_SHORT).show();
                }*/
                origin = dataSnapshot.child("origin").getValue(String.class);
                dest = dataSnapshot.child("dest").getValue(String.class);
                cost = dataSnapshot.child("cost").getValue(Double.class);
                Toast.makeText(WaitingForDriver.this, dest, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
            }
        });

        try {
            Toast.makeText(WaitingForDriver.this, dest, Toast.LENGTH_SHORT).show();
            toText.setText(dest);
            fromText.setText(origin);
            costText.setText(String.format("%.2f", cost));
        } catch (Exception e) {
            Log.d("Tag", e.toString());
        }


    }


}
