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
 * Intent test for DrawerActivity
 */

public class TestDrawerActivity {
    private Solo solo;

    @Rule
    public ActivityTestRule<DrawerActivity> rule =
            new ActivityTestRule<>(DrawerActivity.class, true, true);


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
        solo.assertCurrentActivity("Wrong Activity", DrawerActivity.class);
    }
    /**
     * Checks if correctly linked to Edit Profile
     * @throws Exception
     */
    @Test
    public void checkEditProfileLink(){

        solo.clickOnMenuItem("Profile");
        solo.assertCurrentActivity("Wrong Activity", EditProfileActivity.class);



    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }


}