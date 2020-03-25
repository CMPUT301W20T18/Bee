package com.example.bee;

import android.app.Dialog;
import android.content.Context;
<<<<<<< HEAD
=======
import android.content.Intent;
>>>>>>> master
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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

/**
 * This class displays a dialog that shows information of the driver who accepted the request.
 * The user chooses to accept or decline this offer in this dialog.
 */
public class ConfirmOfferDialog extends DialogFragment {
    private OnFragmentInteractionListener listener;
    private static final String TAG = "TAG";
    private FirebaseUser user;
    private DatabaseReference ref;
    private String driverID;
    private String name;
    private String phone;
    private String rateUp;
    private String rateDown;

    ConfirmOfferDialog(String driverID) {
        this.driverID = driverID;
        // Toast.makeText(getActivity(), name+"2", Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        void acceptOffer();
        void declineOffer();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Dialog);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_offer_fragment);
        TextView driverName = dialog.findViewById(R.id.driver_name1);
        TextView phoneNum = dialog.findViewById(R.id.phone_number);
        TextView rateUpText = dialog.findViewById(R.id.rate_up);
        TextView rateDownText = dialog.findViewById(R.id.rate_down);
        Button startBtn = dialog.findViewById(R.id.start_btn);
        Button rejectBtn = dialog.findViewById(R.id.reject_btn);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(false);
        //if (name.isEmpty())
        //driverName.setText(name);
        //phoneNum.setText(phone);

<<<<<<< HEAD
=======
        /*driverName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmOfferDialog.this, DriverBasicInformation.class);
                startActivity(intent);
            }
        });*/

>>>>>>> master
        user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("users").child(driverID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (driverName.getText().toString().isEmpty()) {
                    // Initialize the dialog with driver's info
                    String name = dataSnapshot.child("Name").getValue(String.class);
                    driverName.setText(name);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    phoneNum.setText(phone);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rider accepted the offer
                listener.acceptOffer();
            }
        });

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rider declined the offer
                listener.declineOffer();
            }
        });

        dialog.show();
        return dialog;
    }



}
