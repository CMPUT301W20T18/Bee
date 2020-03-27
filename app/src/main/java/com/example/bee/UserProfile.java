package com.example.bee;


import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This is a class creates a User Profile object
 */
public class UserProfile {
    private static final String TAG = "TAG";
    private DatabaseReference ref;
    private String userID;
    private String username;
    private String phone;
    private String email, firstName,lastName;


    public UserProfile(String userID) {
        this.userID = userID;
        extractInfo();
    }

    private void extractInfo() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("users").child(userID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("Name").getValue(String.class);
                firstName = dataSnapshot.child("firstName").getValue(String.class);
                lastName = dataSnapshot.child("lastName").getValue(String.class);
                email = dataSnapshot.child("email").getValue(String.class);
                phone = dataSnapshot.child("phone").getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
            }
        });
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }
}
