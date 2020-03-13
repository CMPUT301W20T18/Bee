package com.example.bee;

import android.app.Activity;
import android.widget.TextView;


import androidx.fragment.app.FragmentActivity;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class RiderAfterAcceptRequestTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<RiderAfterAcceptRequest> rule =
            new ActivityTestRule<>(RiderAfterAcceptRequest.class, true, true);

    /**
     * Runs before all tests
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        FragmentActivity fragmentActivity = (FragmentActivity)solo.getCurrentActivity();
        SupportMapFragment map = ((SupportMapFragment) fragmentActivity.getSupportFragmentManager().findFragmentById(R.id.map_requestAccepted));
        assertTrue(map != null);
    }

    /**
     * check maps can work
     */

    //https://stackoverflow.com/questions/14227027
    // /android-unit-testing-framework-that-can-interact-with-google-maps-api-does-one
    @Test
    public void checkMap() {
        solo.assertCurrentActivity("Wrong Activity", RiderAfterAcceptRequest.class);
        Boolean initialID = solo.waitForFragmentById(R.id.map_requestAccepted);
        //save the map type, leave
        //and do other awsome stuff
        //before coming back to the map
        Boolean finalID = solo.waitForFragmentById(R.id.map_requestAccepted);
        //capture the map fragment that is now displayed,
        // again would time out if this specific fragment was not visible

        //assert that the fragments are equal
        assertTrue(initialID.equals(finalID));

    }
    /**
     * check click onDrivername can work
     */

    @Test
    public void checkDriver() {
        TextView driver_name = (TextView) solo.getView("driver_name");
        solo.clickOnView(driver_name);
        solo.assertCurrentActivity("Not in DriverBasicInformation", DriverBasicInformation.class);
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
