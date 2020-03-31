package com.example.bee;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/**
 *  This class determines if currently has a user signed in 
 */
public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        if (user == null){
            // if no user is signed in

            startActivity(new Intent(MainActivity.this, LoginActivity.class));}
        else{
            // if a user signed in, start drawerActivity
            startActivity(new Intent(MainActivity.this, EnterAddressMap.class));
        }
        finish();

    }


}
