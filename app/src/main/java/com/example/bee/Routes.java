package com.example.bee;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.google.android.gms.maps.model.LatLng;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class Routes {
    private Context context;
    private LatLng p1;
    private LatLng p2;
    private String distance;
    private String time;
    private ArrayList<LatLng> pointList;
    public Routes(Context context, LatLng p1, LatLng p2) {
        this.context = context;
        this.p1 = p1;
        this.p2 = p2;
    }

    /**
     * Draw route on the map from the given p1 and p2. They are the latitude and longitude
     * for the start location and end location respectively.
     */
    /*
    Github libray by Akexorcist https://github.com/akexorcist
    Library page: https://github.com/akexorcist/Android-GoogleDirectionLibrary
     */
    public ArrayList<LatLng> getRoutes() {

        GoogleDirection.withServerKey(context.getString(R.string.google_maps_key))
                .from(p1)
                .to(p2)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction) {
                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            pointList = leg.getDirectionPoint();
                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            distance = distanceInfo.getText();
                            time = durationInfo.getText();

                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        String text = "Failed to get direction";
                        Toast toast = Toast.makeText(EnterAddressMap.this, text, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    }
                });
        return pointList;
    }

    public String getDistance() {
        return distance;
    }

    public String getTime() {
        return time;
    }

}
