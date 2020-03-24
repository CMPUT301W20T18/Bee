package com.example.bee;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * This class provides the conversion from input address to Latlng
 * and from Latlng to address string
 */
public class LocationConverter {
    private Context context;
    public LocationConverter(Context context) {
        this.context = context;
    }

    /**
     * Converts address to Latlng
     * @param address
     * @return
     */
    public LatLng addressToLatlng(String address) {
        GeocodingResult[] results;
        LatLng point;

        try {
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey("AIzaSyCJQG3FLwiMDfPA19fjP2KZr7PGQ6rSD60")
                    .build();

            results = GeocodingApi.geocode(context,
                    address).await();

            if (results == null) {
                return null;
            }

            point = new LatLng(results[0].geometry.location.lat, results[0].geometry.location.lng);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return point;
    }

    /**
     * Converts Latlng to address
     * @param latLng
     * @return
     */
    public String latlngToAddress(LatLng latLng) {
        String address = null;
        List<Address> addresses;

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null) {
                address = addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

}
