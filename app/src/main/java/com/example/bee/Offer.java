package com.example.bee;

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
    String getStartingPoint(){
        return this.startingPoint;
    }
//  return offer's endPoint
    String getEndPoint(){
        return this.endPoint;
    }
//  return offer's cost
    String getFare(){
        return this.fare;
    }

    String getLatlng(){
        return this.latlng;
    }

    String getRiderId() {return this.riderId;}
}
