package com.example.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class WaitingForDriver extends AppCompatActivity {
    private static final String TAG = "TAG";
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String originAddress;
    private String destAddress;
    private String userID;
    private Request request;
    TextView toText;
    TextView fromText;
    TextView costText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_driver);
        toText = findViewById(R.id.show_to);
        fromText = findViewById(R.id.show_from);
        costText = findViewById(R.id.show_cost);

        user = LoginActivity.getUser();
        userID = user.getUid();

        DocumentReference docRef = db.collection("requests").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        request = task.getResult().toObject(Request.class);
                    } else {
                        Log.d(TAG, "can't access");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        toText.setText(request.getOrigin());
        fromText.setText(request.getDest());
        costText.setText(Double.toString(request.getCost()));

    }


}
