package com.example.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * This is for rider to get the cost from database and write it into a QRcode
 */
public class RiderPayActivity extends AppCompatActivity {
    private static final String TAG = "TAG";

    private ImageView imageView;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;

    private DatabaseReference ref;
    private String userID;
    private FirebaseUser user;
    private Request mRequest;
    private double cost = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_pay);

        //This part is to get the request from the database
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("requests").child(userID).child("request");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Once the functionality of multiple 
                cost = dataSnapshot.child("cost").getValue(double.class);
                // This part is to write String on textView
                textView1 = findViewById(R.id.textView1);
                textView2 = findViewById(R.id.textView2);
                textView3 = findViewById(R.id.textView3);
                textView1.setText("You have reached your destination");
                textView2.setText(String.format("$ %.2f", cost));
                textView3.setText("Show the QRcode to driver");

                // This part is to write QRcode
                imageView = findViewById(R.id.imageView);
                QRCodeWriter qrCodeWriter = new QRCodeWriter();

                try {
                    BitMatrix bitMatrix = qrCodeWriter.encode(String.valueOf(cost), BarcodeFormat.QR_CODE, 200, 200);
                    Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
                    for (int x = 0; x < 200; x++) {
                        for (int y = 0; y < 200; y++) {
                            bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    imageView.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
            }
        });
    }
}
