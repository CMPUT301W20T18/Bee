package com.example.bee;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 * Test for LocationConverter
 */
public class LocationConverterTest {
    private LocationConverter converter;

    /**
     * Set up a LocationConverter
     */
    @Before
    public void setUp() {
        EnterAddressMap map = new EnterAddressMap();
        converter = new LocationConverter(map.getApplicationContext());
    }

    /**
     * Test for addressToLatlng()
     */
    @Test
    public void testAddressToLatlng() {
        LatLng latLng = converter.addressToLatlng("University Of Alberta");
        assertEquals(latLng.latitude, 53.523220);
        assertEquals(latLng.longitude, -113.526321);
    }

    /**
     * Test for latlngToAddress
     */
    @Test
    public void testLatlngToAddress() {
        LatLng latLng = new LatLng(53.523220, -113.526321);
        String address = converter.latlngToAddress(latLng);
        assertTrue(address.equals("11405 87 Avenue Northwest, Edmonton, Division 11, AB T6G"));
    }
}
