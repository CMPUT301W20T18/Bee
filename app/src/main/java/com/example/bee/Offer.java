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

    Offer(String startingPoint, String endPoint, String fare, String latlng, String riderId){
        this.startingPoint = startingPoint;
        this.endPoint = endPoint;
        this.fare = fare;
        this.latlng = latlng;
        this.riderId = riderId;
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
}
