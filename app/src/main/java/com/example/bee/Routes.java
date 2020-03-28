package com.example.bee;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * This class implements the drawing of the routes on the map,
 * and can return ride info such as distance, duration and cost.
 */
public class Routes {
    Activity activity;
    GoogleMap map;
    private LatLng p1;
    private LatLng p2;
    private String distance;
    private String time;
    private ArrayList<LatLng> pointList;

    public Routes(Activity activity, GoogleMap map, LatLng p1, LatLng p2) {
        this.activity = activity;
        this.map = map;
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
    public void drawRoute() {

        GoogleDirection.withServerKey(activity.getString(R.string.google_maps_key))
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
                            PolylineOptions polylineOptions = DirectionConverter
                                    .createPolyline(activity, pointList, 5,
                                            activity.getResources().getColor(R.color.route));
                            map.addPolyline(polylineOptions);
                        }
                    }
                    @Override
                    public void onDirectionFailure(Throwable t) {
                        String text = "Failed to get direction";
                        Toast toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });

    }

    /**
     * Get the ArrayList of latlng points of the route in in String
     * @return points
     */
    public ArrayList<String> getPointList() {
        ArrayList<String> points = new ArrayList<>();
        for (LatLng latLng : pointList ){
            points.add(latLng.latitude+","+latLng.longitude);
        }
        return points;
    }

    public String getDistance() {
        return distance;
    }

    public String getTime() {
        return time;
    }

    public double getCost() {
        // distance in double, remove comma if there's any, remove km
        String temp = distance.replaceAll(",", "");
        return Double.parseDouble(temp.substring(0, temp.length() - 3)) * 2.3;
    }

}
