package com.example.bee;

import android.app.Activity;
import android.app.Instrumentation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TestSearchRide {
    private Solo solo;
    @Rule
    public ActivityTestRule<SearchRide> rule =
            new ActivityTestRule<>(SearchRide.class,true,true);

    /**
     * Run before all tests check exception
     * @throws
     * Exception
     */

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void initialize() throws Exception{
        Activity activity = rule.getActivity();
        solo.assertCurrentActivity("Wrong Activity", SearchRide.class);
    }

    @Test
    public void checkRequestList(){
        solo.enterText((EditText)solo.getView(R.id.searchNearBy),"university of alberta");
        solo.clickOnImage(1);
        solo.searchText("Requests sorted");
    }
    @Test
    public void checkEmptySearch(){
        solo.clearEditText((EditText) solo.getView(R.id.searchNearBy));
        solo.clickOnImage(1);
        solo.searchText("Please Enter a location");
    }

    @Test
    public void checkBackButton(){
        solo.clickOnImage(0);
        solo.assertCurrentActivity("Wrong Activity", DriverMain.class);

    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
