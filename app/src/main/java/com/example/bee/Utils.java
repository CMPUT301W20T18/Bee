package com.example.bee;

import com.google.firebase.database.FirebaseDatabase;

/**
 * This class wraps FirebaseDatabase in a singleton
 */
public class Utils {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            //mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}