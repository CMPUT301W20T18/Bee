package com.example.bee;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class RequestTest {
    /**
     * Set up a Request class before each test
     */
    public Request setup() {
        String origin = "West Edmonton Mall";
        String dest = "University of Alberta";
        String originLatlng = "53.522515,-113.624191";
        String destLatlng = "53.523219,-113.526319";
        ArrayList<String> pointList = null;
        String distance = "15 km";
        String time = "13 mins";
        double cost = 12.11;
        Request request = new Request("riderID", origin, dest, originLatlng, destLatlng, pointList, distance,
                time, cost);
        return request;
    }

    /**
     * Test if setDriverID is successful
     */
    @Test
    public void testSetDriver() {
        Request request = setup();
        assertNull(request.getDriverID());
        request.setDriverID("driverID");
        assertTrue(request.getDriverID().equals("driverID"));
    }

    /**
     * Test if setStatus is successful
     */
    @Test
    public void testSetStatus() {
        Request request = setup();
        assertFalse(request.getStatus());
        request.setStatus(true);
        assertTrue(request.getStatus());
    }

    /**
     * Test if setFinished is successful
     */
    @Test
    public void testSetFinished() {
        Request request = setup();
        assertFalse(request.getFinished());
        request.setFinished(true);
        assertTrue(request.getFinished());
    }
}
