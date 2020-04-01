package com.example.bee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DriverConfirmActivity extends AppCompatActivity {
    private double amount;
    private TextView textView;
    private FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_confirm);

        // get value from last activity
        amount = getIntent().getExtras().getDouble("amount");

        // initialize layout
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.floatingActionButton);

        textView.setText(String.valueOf(amount));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
