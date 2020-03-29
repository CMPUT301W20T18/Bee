package com.example.bee;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class DriverPayActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private FirebaseUser user;
    private String userID;
    private DatabaseReference ref;
    private ZXingScannerView scannerView;
    private String currentRider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_pay);

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
        if (riderID == currentRider) {
            // Make transaction
            Payment payment = new Payment();

            // Start next activity.
            Intent intent = new Intent(DriverPayActivity.this, DriverConfirmActivity.class);
            intent.putExtra("RiderID", currentRider);
        }
        else {
            // Continue Scanning QR code until a legal code is scanned
            scannerView.startCamera();
        }
    }

    /**
     * Get user's profile from firebase
     */
    public UserProfile getProfile(String userID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref;
    }
}
