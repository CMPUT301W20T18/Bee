package com.example.bee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class RiderRatingActivity extends AppCompatActivity {
    private TextView textView;
    private Button upButton;
    private Button downButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_rating);

        String driverID = getIntent().getExtras().getString("DriverID");
        textView.setText(driverID);
    }
}
