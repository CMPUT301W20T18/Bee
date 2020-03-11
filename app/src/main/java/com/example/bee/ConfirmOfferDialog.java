package com.example.bee;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConfirmOfferDialog extends DialogFragment {
    private static final String TAG = "TAG";
    private FirebaseUser user;
    private DatabaseReference ref;
    private String userID;
    private String driverID;
    private Request request;
    private TextView driverName;
    private TextView phoneNum;
    private TextView rateUp;
    private TextView rateDown;
    private Button startBtn;
    private Button rejectBtn;

    ConfirmOfferDialog(String driverID) {
        this.driverID = driverID;
    }

    private void displayInfo(Dialog dialog) {
        driverName = dialog.findViewById(R.id.driver_name1);
        phoneNum = dialog.findViewById(R.id.phone_number);
        rateUp = dialog.findViewById(R.id.rate_up);
        rateDown = dialog.findViewById(R.id.rate_down);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("users").child(driverID);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue(String.class);
                driverName.setText(name);
                String phone = dataSnapshot.child("phone").getValue(String.class);
                phoneNum.setText(phone);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
            }
        });

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Dialog);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_offer_fragment);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(false);
        displayInfo(dialog);
        startBtn = dialog.findViewById(R.id.start_btn);
        rejectBtn = dialog.findViewById(R.id.reject_btn);


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(ConfirmOfferDialog.this, RiderAfter...));
            }
        });

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }



}
