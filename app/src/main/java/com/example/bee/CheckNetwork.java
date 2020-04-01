package com.example.bee;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class checks for device's network connectivity
 */
public class CheckNetwork {
    Context context;

    public CheckNetwork(Context context) {
        this.context = context;
    }

    // From StackOverflow post https://stackoverflow.com/a/4239019 by https://stackoverflow.com/users/162407/alex-jasmin
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
