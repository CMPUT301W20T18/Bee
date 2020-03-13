package com.example.bee;

import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;

/**
 * Test for WaitingForDriver class
 */

@RunWith(AndroidJUnit4.class)
public class TestWaitingForDriver{

    private Solo solo;

    @Rule
    public ActivityTestRule<WaitingForDriver> rule =
            new ActivityTestRule<>(WaitingForDriver.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the Activity and check if it is the correct activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
        solo.assertCurrentActivity("Wrong Activity", WaitingForDriver.class);
    }

    /**
     * Check if cancel request will return to EnterAddressMap activity
     */
    @Test
    public void checkCancelRequest() {
        solo.assertCurrentActivity("Wrong Activity", WaitingForDriver.class);
        solo.clickOnButton("Cancel Request");
        solo.clickOnButton("Confirm");
        solo.assertCurrentActivity("Wrong Activity", EnterAddressMap.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}