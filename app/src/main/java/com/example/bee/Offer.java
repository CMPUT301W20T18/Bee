package com.example.bee;

/**
 * This java file is an Object of offer request
 */

public class Offer {
//    setup local variables and store them later
    private String startingPoint;
    private String endPoint;
    private String fare;
    private String latlng;
    private String riderId;
    private float distance;

    Offer(String startingPoint, String endPoint, String fare, String latlng, String riderId){
        this.startingPoint = startingPoint;
        this.endPoint = endPoint;
        this.fare = fare;
        this.latlng = latlng;
        this.riderId = riderId;
        this.distance = 0;
    }

//  return offer's starting point

    /**
     *  Returning starting point on call
     * @return
     * return Starting Point for this request
     */
    String getStartingPoint(){
        return this.startingPoint;
    }
//  return offer's endPoint

    /**
     * Returning end point on call
     * @return
     * return End Point for this request
     */
    String getEndPoint(){
        return this.endPoint;
    }
//  return offer's cost

    /**
     * Returning request's fare
     * @return
     * return current reqeust's fare the rider offered
     */
    String getFare(){
        return this.fare;
    }

    /**
     * Returning starting point latitude and longitude
     * @return
     * return request's geo location
     */
    String getLatlng(){
        return this.latlng;
    }

    /**
     * getRiderID
     * @return
     * return rider's Id of current request
     */
    String getRiderId() {return this.riderId;}

    /**
     * Set up the distance between current starting point and searching point
     * Default value is 0
     */
    void setDistance(float distance){
        this.distance = distance;
    }

    /**
     * Get distance of of this offer request from searching point
     * @return
     * return floating type distance
     */
    float getDistance(){return this.distance;}
}
