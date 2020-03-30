package com.example.bee;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DriverHistory extends AppCompatActivity{
    static ArrayList<Request> requests;
    private RequestAdapter rAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_history);


        ListView mList = findViewById(R.id.hList);
        rAdapter = new RequestAdapter(this, requests);
        mList.setAdapter(rAdapter);


    }
}
