package com.example.bee;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * This class represents a request. It contains information needed from both driver and rider.
 */

public class Request {
    private String riderID;
    private String driverID;
    private String origin;
    private String dest;
    private String originLatlng;
    private String destLatlng;
    private ArrayList<String> pointList;
    private String distance;
    private String time;
    private double cost;
    private boolean status;
    private boolean cancel;
    private boolean reached;
    private boolean finished;

    public Request() {}

    public Request(String riderID, String origin, String dest, String originLatlng, String destLatlng,
                   ArrayList<String> pointList, String distance, String time, double cost) {
        this.riderID = riderID;
        this.origin = origin;
        this.dest = dest;
        this.originLatlng = originLatlng;
        this.destLatlng = destLatlng;
        this.pointList = pointList;
        this.distance = distance;
        this.time = time;
        this.cost = cost;
        this.driverID = null;
        this.status = false;
        this.cancel = false;
        this.reached = false;
        this.finished = false;
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
    public String getOriginLatlng() {
        return originLatlng;
    }

    /**
     * Returns destination latitude and longitude
     * @return destLatlng
     */
    public String getDestLatlng() {
        return destLatlng;
    }

    /**
     * Returns the polylineOptions to draw the routes
     * @return routes
     */
    public ArrayList<String> getPoints() {
        return pointList;
    }

    /**
     * Returns the total distance of the ride
     * @return distance
     */
    public String getDistance() {
        return distance;
    }

    /**
     * Returns the duration of the ride
     * @return time
     */
    public String getTime() {
        return time;
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
     * Returns true if rider cancel the request before the driver picks up the rider
     * @return cancel
     */
    public boolean getCancel() {
        return cancel;
    }

    /**
     * Returns true if the driver clicks on the button indicating destination is reached
     * @return reached
     */
    public boolean getReached() {
        return reached;
    }

    /**
     * Returns true if the driver pressed the finished button
     * @return finished
     */
    public boolean getFinished() {
        return finished;
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

    /**
     * Sets whether or not the request is cancelled
     * @param cancel
     */
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Sets whether or not the destination is reached
     * @param reached
     */
    public void setReached(boolean reached) {
        this.reached = reached;
    }

    /**
     * Sets whether or not the ride is finished
     * @param finished
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
