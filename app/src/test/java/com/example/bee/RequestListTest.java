package com.example.bee;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class RequestListTest {
    public ArrayList<Offer> init(){
        String startingPoint = "WEM";
        String endPoint = "UofA";
        String fare = "20";
        String latlng = "53.522515,-113.624191";
        String riderId = "ID";


        ArrayList<Offer> mList = new ArrayList<Offer>() ;

        mList.add(new Offer(startingPoint, endPoint, fare, latlng, riderId));
        return mList;
    }

    @Test
    public void testInfo(){
        ArrayList<Offer> mList = init();
        assertNotNull(mList);
        assertEquals(mList.size(),1);

        Offer offer = mList.get(0);
        assert(offer.getStartingPoint() == "WEM");
        assert(offer.getEndPoint() == "UofA");
        assertEquals(offer.getFare(),"20");
        assertEquals(offer.getLatlng(),"53.522515,-113.624191");
        assertEquals(offer.getRiderId(),"ID");
    }

    @Test
    public void testItem(){
        Offer newOffer = new Offer("UofA","WEM","20","0,0","NewID");

        ArrayList<Offer> mList = init();
        mList.add(newOffer);

        assertEquals(mList.size(),2);
    }

}
