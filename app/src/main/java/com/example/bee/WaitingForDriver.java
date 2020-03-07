package com.example.bee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WaitingForDriver extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_driver);

        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

    }
}
