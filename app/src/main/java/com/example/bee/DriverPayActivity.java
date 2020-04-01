package com.example.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class DriverPayActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private FirebaseUser user;
    private String userID;
    private DatabaseReference ref;
    private ZXingScannerView scannerView;
    private String currentRiderID;
    private String currentRider;
    private double amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_pay);

        // Get Rider's information from last activity
        currentRider = getIntent().getExtras().getString("Rider", "[Rider Name pass fail]");
        currentRiderID = getIntent().getExtras().getString("RiderID", null);
        amount = getIntent().getExtras().getDouble("amount", 0);

        // This is to get the driver's current request from the database
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        //Initialize
        scannerView = (ZXingScannerView) findViewById(R.id.zxscan);
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                scannerView.setResultHandler(DriverPayActivity.this);
                scannerView.startCamera();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(DriverPayActivity.this, "Accept the permission to continue scanning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();
    }

    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    public void handleResult(Result rawResult) {
        String riderID = rawResult.getText();
        String driverID = userID;
        if (riderID.equals(currentRiderID)) {
            // Make transaction
            // First add money to driver's wallet
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("users").child(userID).child("amount");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double balance = dataSnapshot.getValue(double.class);
                    String driverDescr = String.format("%s owes me $%.2f", currentRider, amount);

                    // Update Amount and transaction
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/users/" + driverID + "/amount", balance + amount);
                    String key = database.getReference().child("users").child(driverID).push().getKey();
                    childUpdates.put("/users/" + driverID + "/transaction/" + key, driverDescr);
                    database.getReference().updateChildren(childUpdates);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            // Second, add transaction to rider
            ref = database.getReference("users").child(riderID).child("amount");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double balance = dataSnapshot.getValue(double.class);
                    String riderDescr = String.format("I owe %s $%.2f", driverID, amount);

                    // Update Amount and transaction
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/users/" + riderID + "/amount", balance - amount);
                    String key = database.getReference().child("users").child(riderID).push().getKey();
                    childUpdates.put("/users/" + riderID + "/transaction/" + key, riderDescr);
                    database.getReference().updateChildren(childUpdates);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            // Thirdly, delete the request under requests branch
            // And add it to history branch
            ref = database.getReference("requests").child(riderID);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Request request = (Request) dataSnapshot.child("request").getValue(Request.class);
                    addRequestToHistory(riderID, request);
                    addRequestToHistory(driverID, request);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //ref.removeValue();
            // Then, Start next activity -- RiderConfirmActivity
            Intent intent = new Intent(DriverPayActivity.this, DriverConfirmActivity.class);
            intent.putExtra("amount", amount);
            startActivity(intent);
        }
        else {
            // Continue Scanning QR code until a legal code is scanned
            scannerView.startCamera();
        }
    }

    /**
     * This method is to add a request under user's history
     * @param ID -- userID
     * @param request
     */
    private void addRequestToHistory(String ID, Request request) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("history").child(ID);
        String key = ref.push().getKey();
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, request);
        ref.updateChildren(map);
    }

}
