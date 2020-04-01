package com.example.bee;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RiderProfile extends AppCompatActivity {
    ImageView phone_call;
    ImageView email_send;
    ImageView back;
    private static final String CALL_PERMISSION = Manifest.permission.CALL_PHONE;

    String passRiderID, riderPhone, riderEmail;
    private static final int REQUEST_CALL = 1;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_profile);
        Bundle bundle = getIntent().getExtras();


        passRiderID = bundle.getString("passRiderID");


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("users");
        ref.child(passRiderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    riderPhone = dataSnapshot.child("phone").getValue().toString();
                    riderEmail = dataSnapshot.child("email").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        phone_call = findViewById(R.id.phone);
        phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        email_send = findViewById(R.id.mail);
        email_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });




    }

    /**
     * This method make a phone call to rider
     */

    private void makePhoneCall(){
        String phone_num = riderPhone;

        if(phone_num.trim().length()> 0){
            if (ContextCompat.checkSelfPermission(RiderProfile.this,
                    CALL_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(RiderProfile.this,
                        new String[]{CALL_PERMISSION}, REQUEST_CALL);
            } else {
                String dial = "tel:" + phone_num;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        }else{
            Toast.makeText(RiderProfile.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method send an email to rider
     */

    private void sendMail(){
        String recipient = riderEmail;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipient);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));

    }

    /**
     * This method ask permission to call
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

}