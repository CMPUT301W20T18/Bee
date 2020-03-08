package com.example.bee;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * This class represents a request. It contains information needed from both driver and rider.
 */

public class Request {
    private String riderID;
    private String driverID;
    private String origin;
    private String dest;
    private LatLng originLatlng;
    private LatLng destLatlng;
    private ArrayList<LatLng> pointList;
    private double cost;
    private boolean status;

    public Request(String riderID, String origin, String dest, LatLng originLatlng, LatLng destLatlng,
                   ArrayList<LatLng> pointList, double cost) {
        this.riderID = riderID;
        this.origin = origin;
        this.dest = dest;
        this.originLatlng = originLatlng;
        this.destLatlng = destLatlng;
        this.pointList = pointList;
        this.cost = cost;
        this.driverID = null;
        this.status = false;
    }

    /**
     * Returns rider's userID
     * @return riderID
     */
    public String getRiderID() {
        return riderID;
    }

    /**
     * Returns driver's userID
     * @return driverID
     */
    public String getDriverID() {
        return driverID;
    }

    /**
     * Returns pick up address
     * @return origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Returns destination address
     * @return dest
     */
    public String getDest() {
        return dest;
    }

    /**
     * Returns the pick up address's latitude and longitude
     * @return originLatlng
     */
    public LatLng getOriginLatlng() {
        return originLatlng;
    }

    /**
     * Returns destination latitude and longitude
     * @return destLatlng
     */
    public LatLng getDestLatlng() {
        return destLatlng;
    }

    /**
     * Returns the polylineOptions to draw the routes
     * @return routes
     */
    public ArrayList<LatLng> getPoints() {
        return pointList;
    }

    /**
     * Returns the cost of the ride
     * @return cost
     */
    public double getCost(){
        return cost;
    }

    /**
     * Returns true if the rider accepted the driver's offer
     * @return status
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * Sets the driverID of the request
     * @param driverID
     */
    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    /**
     * Sets the status of the request
     * @param status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }
}
