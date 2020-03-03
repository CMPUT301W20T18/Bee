package com.example.bee;


import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

public class Pop_up_paired_driver_activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_paired_driver);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int window_width = (int) Math.floor((dm.widthPixels)* 0.8);
        int window_height = (int) Math.floor((dm.heightPixels)* 0.20);

        getWindow().setLayout(window_width,window_height);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.TOP;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);





    }
}
