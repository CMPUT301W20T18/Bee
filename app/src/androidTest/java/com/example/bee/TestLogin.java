package com.example.bee;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Intent test for Login
 */

public class TestLogin {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);


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
     * Checks if log in to the right activity
     * @throws Exception
     */
    @Test
    public void checkLogin(){
        solo.enterText((EditText) solo.getView(R.id.atvUsernameReg), "Sophie");
        solo.enterText((EditText) solo.getView(R.id.atvPasswordLog), "1234!Q");
        solo.clickOnButton("SIGN IN AS RIDER");
        solo.assertCurrentActivity("Current Activity", EnterAddressMap.class);

        solo.enterText((EditText) solo.getView(R.id.atvUsernameReg), "Sophie");
        solo.enterText((EditText) solo.getView(R.id.atvPasswordLog), "1234!Q");
        solo.clickOnButton("SIGN IN AS Driver");
        solo.assertCurrentActivity("Current Activity", EnterAddressMap.class);



    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }


}
