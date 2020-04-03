package com.example.bee;

import android.app.Activity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.maps.SupportMapFragment;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
/**
 * Test for WaitingForDriver class
 */
public class WaitingForRiderTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<WaitingForRider> rule =
            new ActivityTestRule<>(WaitingForRider.class, true, true);
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * get the Activity and check whether the activity is correct or not
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        FragmentActivity fragmentActivity = (FragmentActivity)solo.getCurrentActivity();
        SupportMapFragment map = ((SupportMapFragment) fragmentActivity.getSupportFragmentManager().findFragmentById(R.id.map_requestAccepted));
        assertTrue(map != null);

    }
    /**
     * check the trip is whether finished or not
     * @throws Exception
     */
    @Test
    public void checkFinishTrip() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", WaitingForRider.class);
        assertTrue(solo.searchText("Confirmed rider offer"));
        solo.clickOnButton("FINISH");
        solo.assertCurrentActivity("Wrong Activity", DriverPayActivity.class);


    }
    /**
     * check the Back button can go back to request searching interface
     * @throws Exception
     */
    @Test
    public void checkBackToSearchRide() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", WaitingForRider.class);
        assertTrue(solo.searchText("Declined offer"));
        solo.clickOnButton("BACK");
        solo.assertCurrentActivity("Wrong activity",SearchRide.class);


    }
    /**
     * check the whether the click for rider name can show rider profile or not
     * @throws Exception
     */
    @Test
    public void checkRiderProfile() throws Exception{
        TextView rider_name = (TextView) solo.getView("rider_name");
        solo.clickOnView(rider_name);
        solo.assertCurrentActivity("Not in RiderProfile", RiderProfile.class);
    }
    /**
     * check the rider card click can work
     * @throws Exception
     */
    @Test
    public void checkRiderCard() throws Exception{
        RelativeLayout rider_card = (RelativeLayout) solo.getView("rider_card");
        solo.clickOnView(rider_card);
        solo.assertCurrentActivity("Not in PopUpMap", PopUpMap.class);
    }
    /**
     * Close the activity after every single test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }




}
