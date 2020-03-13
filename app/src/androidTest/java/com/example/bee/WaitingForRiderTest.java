package com.example.bee;

import android.app.Activity;

import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class WaitingForRiderTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }
    public void checkFinishButton() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", WaitingForRider.class);
        assertTrue(solo.searchText("Confirmed rider offer"));
        solo.clickOnButton("FINISH");


    }




}
