package com.example.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is a class that shows the basic information about driver
 */


public class DriverBasicInformation extends AppCompatActivity {
    ImageView back;
    ImageView phone_call;
    ImageView email_send;
    TextView tv_name,tv_up,tv_down;

    private static final int REQUEST_CALL = 1;
    private static final String CALL_PERMISSION = Manifest.permission.CALL_PHONE;

    public static final String SHARED_PREFS = "sharedPrefs";


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
        tv_name = findViewById(R.id.driver_name);
        tv_up = findViewById(R.id.t_up);
        tv_down = findViewById(R.id.t_down);
        loadDriver();
    }
    /**
     * This is a method enable rider to make a phone call to driver
     */

    private void makePhoneCall(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String phone_num = sharedPreferences.getString("phone", "");

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
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String mail = sharedPreferences.getString("mail", "");
        if(mail.equals("")){
            Toast.makeText(this, "data error", Toast.LENGTH_SHORT).show();
        }else{
            String[] recipient = {mail};
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, recipient);

            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "Choose an email client"));
        }


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

    private void loadDriver(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String name = sharedPreferences.getString("dr_name", "");
        String up = sharedPreferences.getString("up", "");
        String down = sharedPreferences.getString("down", "");
        if(!name.equals("") && !up.equals("")&&!down.equals("")){
            tv_name.setText(name);
            String formal_up = up+ " people";
            String formal_down = down+ " people";
            tv_up.setText(formal_up);
            tv_down.setText(formal_down);
        }else{
            Toast.makeText(this, "data error", Toast.LENGTH_SHORT).show();
        }
    }


}
