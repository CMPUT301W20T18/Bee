package com.example.bee;

import android.app.Activity;

//import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Rule;
import org.junit.Test;

public class PopUpMapTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(PopUpMap.class, true, true);

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
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
    public void checkCancelButton() throws Exception{
        solo.assertCurrentActivity("Wrong Activity",PopUpMap.class);
        solo.clickOnButton("Cancel");
        solo.assertCurrentActivity("Not SearchRide activity",SearchRide.class);
    }
















}
