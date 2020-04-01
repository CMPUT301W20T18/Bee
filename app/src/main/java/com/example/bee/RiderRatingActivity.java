package com.example.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Driver;
import java.util.HashMap;
import java.util.Map;

public class RiderRatingActivity extends AppCompatActivity {
    private TextView textView;
    private Button upButton;
    private Button downButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_rating);
        textView = findViewById(R.id.textView);
        upButton = findViewById(R.id.upButton);
        downButton = findViewById(R.id.downButton);

        String driverID = getIntent().getExtras().getString("DriverID");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users").child(driverID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstName = dataSnapshot.child("firstName").getValue(String.class);
                String lastName = dataSnapshot.child("lastName").getValue(String.class);
                textView.setText(firstName + " " + lastName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference nref = database.getReference("users").child(driverID).child("thumbUp");
                nref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int total = dataSnapshot.getValue(int.class);
                        Map<String, Object> map = new HashMap<>();
                        map.put("/users/" + driverID + "/thumbUp", total + 1);
                        database.getReference().updateChildren(map);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent intent = new Intent(RiderRatingActivity.this, EnterAddressMap.class);
                startActivity(intent);
            }
        });
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference nref = database.getReference("users").child(driverID).child("thumbDown");
                nref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int total = dataSnapshot.getValue(int.class);
                        Map<String, Object> map = new HashMap<>();
                        map.put("/users/" + driverID + "/thumbDown", total + 1);
                        database.getReference().updateChildren(map);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent intent = new Intent(RiderRatingActivity.this, EnterAddressMap.class);
                startActivity(intent);
            }
        });
    }
}
