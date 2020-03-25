package com.example.bee;

<<<<<<< HEAD
=======
/**
 * This java file is an Object of offer request
 */

>>>>>>> master
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
<<<<<<< HEAD
=======

    /**
     *  Returning starting point on call
     * @return
     * return Starting Point for this request
     */
>>>>>>> master
    String getStartingPoint(){
        return this.startingPoint;
    }
//  return offer's endPoint
<<<<<<< HEAD
=======

    /**
     * Returning end point on call
     * @return
     * return End Point for this request
     */
>>>>>>> master
    String getEndPoint(){
        return this.endPoint;
    }
//  return offer's cost
<<<<<<< HEAD
=======

    /**
     * Returning request's fare
     * @return
     * return current reqeust's fare the rider offered
     */
>>>>>>> master
    String getFare(){
        return this.fare;
    }

<<<<<<< HEAD
=======
    /**
     * Returning starting point latitude and longitude
     * @return
     * return request's geo location
     */
>>>>>>> master
    String getLatlng(){
        return this.latlng;
    }

<<<<<<< HEAD
=======
    /**
     * getRiderID
     * @return
     * return rider's Id of current request
     */
>>>>>>> master
    String getRiderId() {return this.riderId;}
}
