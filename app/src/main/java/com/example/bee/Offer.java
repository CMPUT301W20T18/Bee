package com.example.bee;

/**
 * This java file is an Object of offer request
 */

public class Offer implements Comparable<Offer> {
//    setup local variables and store them later
    private String startingPoint;
    private String endPoint;
    private String fare;
    private double lat;
    private double lng;
    private String riderId;
    private float distance;

    Offer(String startingPoint, String endPoint, String fare, double lat,double lng, String riderId){
        this.startingPoint = startingPoint;
        this.endPoint = endPoint;
        this.fare = fare;
        this.lat = lat;
        this.lng = lng;
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
    double getLat(){
        return this.lat;
    }

    double getLng(){return this.lng;}

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

    @Override
    public int compareTo(Offer f){
        return Float.compare(f.getDistance(), getDistance());
    }

}
