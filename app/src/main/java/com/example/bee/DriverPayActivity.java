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
        currentRider = getIntent().getExtras().getString("Rider", null);
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
            DatabaseReference ref = database.getReference("users").child(userID).child("Wallet");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    QRWallet driverWallet = (QRWallet) dataSnapshot.getValue(QRWallet.class);
                    String driverDescr = String.format("%s owes me $%.2f", currentRider, amount);
                    driverWallet.addTransaction(driverDescr, amount);
                    database.getReference("users").child(userID).setValue(driverWallet);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            // Second, add transaction to rider
            ref = database.getReference("users").child(riderID).child("Wallet");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    QRWallet riderWallet = (QRWallet) dataSnapshot.getValue(QRWallet.class);
                    String riderDescr = String.format("I owe %s $%.2f", riderWallet.getName(), amount);
                    riderWallet.addTransaction(riderDescr, -1 * amount);
                    database.getReference("users").child(riderID).setValue(riderWallet);
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
                    Request request = (Request) dataSnapshot.getValue(Request.class);
                    addRequestToHistory(riderID, request);
                    addRequestToHistory(driverID, request);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            ref.removeValue();
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
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Request> requests = (ArrayList) dataSnapshot.getValue(ArrayList.class);
                requests.add(request);
                ref.child(ID).setValue(requests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
