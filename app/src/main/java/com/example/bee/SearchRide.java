package com.example.bee;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/** This class is is used to show requests that get from firebase
 * into a list view, and allows user to click one of them to see detailed info
 */

public class SearchRide extends AppCompatActivity {


//    initializing local variables
    private static final String TAG = "TAG";
    private String driverId;
//    private FirebaseUser;
    private DatabaseReference ref;
    String passDistance;
    Request request;
    String passMoneyAmount;
    EditText searchNearby;
    ImageView searchButton;
    ImageView backButton;
    ArrayAdapter<Offer> offerAdapter;
    ArrayList<Offer> offerInfo;
    ListView offerList;

    private String searchAddress;
    private LatLng searchPos;
    private LatLng searchLatLng;
    private double searchingLat;
    private double searchingLng;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_search);

//        setup buttons in current view
        searchNearby = findViewById(R.id.searchNearBy);

//        setup the listview for current requests
        offerList = findViewById(R.id.offer_list);
        offerInfo = new ArrayList<>();

//        setup list adapter
        offerAdapter = new CustomList(this,offerInfo);
        offerList.setAdapter(offerAdapter);

//      connect to firebase realtime database & get current driver's uid
        driverId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("requests");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // empty the list array if it has requests in it before update from firebase
                if (!offerInfo.isEmpty()){
                    offerInfo.clear();
                }
                //                Using for loop to obtain all current reqeusts in database and add them into list view

                for(DataSnapshot dsp:dataSnapshot.getChildren()){
                    // Adding condition so that driver can't see request made by his/her own account
                    if (dsp.child("request").exists() && !dsp.getKey().equals(driverId)) {
                        request = dsp.child("request").getValue(Request.class);
                        // eliminate requests that already accepted by other driver
                        if (request.getDriverID()== null) {
                            System.out.println(request.getDriverID());
                            String[] latlng = request.getOriginLatlng().split(",");
                            offerInfo.add(new Offer(request.getOrigin(), request.getDest(), String.format("%.2f", request.getCost()), Double.valueOf(latlng[0]), Double.valueOf(latlng[1]), request.getRiderID()));
                            //   notify adapter to update listview
                            offerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



//        Going back to driver's main activity
        backButton = findViewById(R.id.backButtonDriver);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent driverMain = new Intent(SearchRide.this, DriverMain.class);
                startActivity(driverMain);
            }
        });

//        Xiutong's configuration: for passing value to pop up map
        offerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Offer tempOffer = offerInfo.get(position);

                Intent show = new Intent(SearchRide.this,PopUpMap.class);

                String passRiderID = tempOffer.getRiderId();
                String passOriginLatlng = tempOffer.getStartingPoint();
                String passDestLatlng = tempOffer.getEndPoint();
                passMoneyAmount = tempOffer.getFare();
                show.putExtra("passMoneyAmount",passMoneyAmount);
                show.putExtra("passOriginLatlng",passOriginLatlng);
                show.putExtra("passDestLatlng",passDestLatlng);
                show.putExtra("passRiderID",passRiderID);
                show.putExtra("passDriverID",driverId);

                startActivity(show);
            }
        });

        EditText searchText = findViewById(R.id.searchNearBy);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAddress = searchText.getText().toString();
                if(! searchAddress.isEmpty()){
                     searchLatLng  = getLatLng(searchAddress);
                    if(searchLatLng!=null){
                        sortOffer(searchLatLng);
                        String text = "Requests sorted";
                        Toast toast = Toast.makeText(SearchRide.this,text,Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                    else{
                        String text = "Searching Place Not Found";
                        Toast toast = Toast.makeText(SearchRide.this,text,Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }
                else{
                    String text = "Please Enter a location";
                    Toast toast = Toast.makeText(SearchRide.this,text,Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }



            }
        });

    }

    private LatLng getLatLng(String searchingAddress){
        GeocodingResult[] address;
        try{
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_maps_key))
                    .build();
            address = GeocodingApi.geocode(context,
                    searchingAddress).await();

            if (address != null){
                searchPos = new LatLng(address[0].geometry.location.lat,address[0].geometry.location.lng);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return searchPos;
    }

    private void sortOffer(LatLng target){
        searchingLat = target.latitude;
        searchingLng = target.longitude;

        for(Offer offer : offerInfo){
            float[] dist = new float[1];
            Location.distanceBetween(offer.getLat(),offer.getLng(),searchingLat,searchingLng,dist);
            offer.setDistance(dist[0]);
        }

        Collections.sort(offerInfo);

        Collections.reverse(offerInfo);

        offerAdapter.notifyDataSetChanged();

    }

}
