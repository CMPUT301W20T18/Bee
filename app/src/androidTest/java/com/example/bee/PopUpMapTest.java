package com.example.bee;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.maps.SupportMapFragment;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;


public class PopUpMapTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<PopUpMap> rule =
            new ActivityTestRule<>(PopUpMap.class,true,true);
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
    @Test
    public void checkPopUpMap() throws Exception{
        solo.assertCurrentActivity("Wrong Activity",PopUpMap.class);
    }
    @Test
    public void checkAcceptButton() throws Exception{
        solo.assertCurrentActivity("Not PopUpMap",PopUpMap.class);
        solo.clickOnButton("Accept");
        solo.assertCurrentActivity("Not WaitingForRider activity",WaitingForRider.class);
    }
    @Test
    public void checkCancelButton() throws Exception{
        solo.assertCurrentActivity("Wrong Activity",PopUpMap.class);
        solo.clickOnButton("Cancel");
        solo.assertCurrentActivity("Not SearchRide activity",SearchRide.class);
    }
















}
