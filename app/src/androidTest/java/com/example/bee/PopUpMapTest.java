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
 * Test for PopUpMap class
 */
public class PopUpMapTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<PopUpMap> rule =
            new ActivityTestRule<>(PopUpMap.class,true,true);
    /**
     * set up the activity
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Inititialize the activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        FragmentActivity fragmentActivity = (FragmentActivity)solo.getCurrentActivity();
        SupportMapFragment map = ((SupportMapFragment) fragmentActivity.getSupportFragmentManager().findFragmentById(R.id.map_requestAccepted));
        assertTrue(map != null);

    }
    /**
     * check whether current activity is PopUpMap
     * @throws Exception
     */
    @Test
    public void checkPopUpMap() throws Exception{
        solo.assertCurrentActivity("Wrong Activity",PopUpMap.class);
    }
    /**
     * check the Accept clicking whether direct the user to rider waiting interface or not
     * @throws Exception
     */
    @Test
    public void checkAccept() throws Exception{
        solo.assertCurrentActivity("Not PopUpMap",PopUpMap.class);
        solo.clickOnButton("Accept");
        solo.assertCurrentActivity("Not WaitingForRider activity",WaitingForRider.class);
    }
    /**
     * check the Cancel clicking whether direct the user back to request serach interface or not
     * @throws Exception
     */
    @Test
    public void checkCancel() throws Exception{
        solo.assertCurrentActivity("Wrong Activity",PopUpMap.class);
        solo.clickOnButton("Cancel");
        solo.assertCurrentActivity("Not SearchRide activity",SearchRide.class);
    }
    /**
     * check the rider card whether direct the user to rider profile or not
     * @throws Exception
     */
    @Test
    public void checkRiderCard() {
        TextView rider_name = (TextView) solo.getView("rider_name");
        solo.clickOnView(rider_name);
        solo.assertCurrentActivity("Not in RiderProfile", RiderProfile.class);
    }
    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }














}
