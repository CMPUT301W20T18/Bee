package com.example.bee;

import android.app.Activity;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.maps.SupportMapFragment;
import com.robotium.solo.Solo;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class WaitingForRiderTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<WaitingForRider> rule =
            new ActivityTestRule<>(WaitingForRider.class, true, true);
    @Test
    public void start() throws Exception {
        FragmentActivity fragmentActivity = (FragmentActivity)solo.getCurrentActivity();
        SupportMapFragment map = ((SupportMapFragment) fragmentActivity.getSupportFragmentManager().findFragmentById(R.id.map_requestAccepted));
        assertTrue(map != null);

    }
    @Test
    public void checkFinishButton() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", WaitingForRider.class);
        assertTrue(solo.searchText("Confirmed rider offer"));
        solo.clickOnButton("FINISH");


    }
    @Test
    public void checkDriver() {
        TextView rider_name = (TextView) solo.getView("driver_name");
        solo.clickOnView(rider_name);
        solo.assertCurrentActivity("Not in DriverBasicInformation", DriverBasicInformation.class);
    }




}
