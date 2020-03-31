package com.example.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * This is a class that shows the basic information about driver
 */


public class DriverBasicInformation extends AppCompatActivity {
    ImageView back;
    ImageView phone_call;
    ImageView email_send;

    private static final int REQUEST_CALL = 1;
    private static final String TEST_PHONE = "5879375305";
    private static final String CALL_PERMISSION = Manifest.permission.CALL_PHONE;

    private static final String[] TEST_EMAIL = {"yixian@ualberta.ca"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_basic_information);
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
     * This is a method enable rider to make a phone call to driver
     */

    private void makePhoneCall(){
        String phone_num = TEST_PHONE;

        if(phone_num.trim().length()> 0){
            if (ContextCompat.checkSelfPermission(DriverBasicInformation.this,
                    CALL_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DriverBasicInformation.this,
                        new String[]{CALL_PERMISSION}, REQUEST_CALL);
            } else {
                String dial = "tel:" + phone_num;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        }else{
            Toast.makeText(DriverBasicInformation.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This is a method enable rider to send an email to driver
     */

    private void sendMail(){
        String[] recipient = TEST_EMAIL;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipient);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));

    }

    /**
     * This is a method to get permission for making a phone call
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
