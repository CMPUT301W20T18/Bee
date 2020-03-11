package com.example.bee;

public class Offer {
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


    String getStartingPoint(){
        return this.startingPoint;
    }

    String getEndPoint(){
        return this.endPoint;
    }

    String getFare(){
        return this.fare;
    }

    String getLatlng(){
        return this.latlng;
    }

    String getRiderId() {return this.riderId;}
}
