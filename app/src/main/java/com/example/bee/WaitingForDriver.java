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
    private FirebaseUser user;
    private DatabaseReference ref;
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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("requests").child(userID).child("request");

        displayInfo();

    }

    private void displayInfo() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                origin = dataSnapshot.child("origin").getValue(String.class);
                dest = dataSnapshot.child("dest").getValue(String.class);
                cost = dataSnapshot.child("cost").getValue(Double.class);

                toText.setText(dest);
                fromText.setText(origin);
                costText.setText(String.format("%.2f", cost));

                /*Request request = dataSnapshot.getValue(Request.class);
                if (request != null) {
                    toText.setText(request.getDest());
                    fromText.setText(request.getOrigin());
                    costText.setText(String.format("%.2f", request.getCost()));
                } else {
                    Toast.makeText(WaitingForDriver.this, "null", Toast.LENGTH_SHORT).show();
                }*/

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
            }
        });
    }


}
