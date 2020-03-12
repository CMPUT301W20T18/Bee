package com.example.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class WaitingForDriver extends AppCompatActivity implements ConfirmOfferDialog.OnFragmentInteractionListener{
    private static final String TAG = "TAG";
    private FirebaseUser user;
    private DatabaseReference ref;
    private String userID;
    private Request request;
    private TextView toText;
    private TextView fromText;
    private TextView costText;
    private String driverID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_driver);
        toText = findViewById(R.id.show_to);
        fromText = findViewById(R.id.show_from);
        costText = findViewById(R.id.show_cost);
        Button cancelRequestBtn = findViewById(R.id.cancel_request);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("requests").child(userID).child("request");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                request = dataSnapshot.getValue(Request.class);
                if (toText.getText().toString().isEmpty()) {
                    toText.setText(request.getDest());
                    fromText.setText(request.getOrigin());
                    costText.setText(String.format("%.2f", request.getCost()));
                }
                if (request != null) {
                    driverID = request.getDriverID();
                    if (driverID != null) {
                        new ConfirmOfferDialog(driverID).show(getSupportFragmentManager(), "show_driver");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
            }
        });

        cancelRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toConfirmCancel();
            }
        });
    }

    private void displayOfferDialog() {
        final String[] info = new String[2];
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users").child(driverID);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue(String.class);
                String phone = dataSnapshot.child("phone").getValue(String.class);
                // rating
                info[0] = name;
                info[1] = phone;
                Toast.makeText(WaitingForDriver.this, info[0]+"1", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
            }
        });
        if (info[0] == null)
            Toast.makeText(WaitingForDriver.this, "null", Toast.LENGTH_SHORT).show();
        // new ConfirmOfferDialog(info[0], info[1]).show(getSupportFragmentManager(), "show_driver");
    }

    /**
     * Shows a confirm message that ask the user to confirm their cancel of request
     */
    private void toConfirmCancel() {
        Dialog dialog = new Dialog(WaitingForDriver.this, android.R.style.Theme_Dialog);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_cancel_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Button confirmBtn = dialog.findViewById(R.id.do_cancel_btn);
        Button cancelBtn = dialog.findViewById(R.id.not_cancel_btn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to EnterAddressMap activity
                ref.getParent().removeValue();
                startActivity(new Intent(WaitingForDriver.this, EnterAddressMap.class));
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Rider side will notify the driver that the offer has been accepted
     */
    public void acceptOffer() {
        request.setStatus(true);
        HashMap<String,Request> updatedRequest = new HashMap<>();
        updatedRequest.put("request", request);
        ref.getParent().setValue(updatedRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: rider accepted the offer");
                //startActivity(new Intent(WaitingForDriver.this, RiderAfter));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }

    public void declineOffer() {
        request.setDriverID(null);
        HashMap<String,Request> updatedRequest = new HashMap<>();
        updatedRequest.put("request", request);
        ref.getParent().setValue(updatedRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: rider declined the offer");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {} // Prevent activity from going back to the last activity

}
