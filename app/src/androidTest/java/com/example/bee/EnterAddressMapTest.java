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
 * Test for EnterAddressMap class
 */

@RunWith(AndroidJUnit4.class)
public class EnterAddressMapTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<EnterAddressMap> rule =
            new ActivityTestRule<>(EnterAddressMap.class, true, true);

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
        solo.assertCurrentActivity("Wrong Activity", EnterAddressMap.class);
    }

    /**
     * Check if the class will invoke SetCost dialog
     */
    @Test
    public void checkShowSetCost() {
        solo.assertCurrentActivity("Wrong Activity", EnterAddressMap.class);
        solo.enterText((EditText) solo.getView(R.id.from_address), "West Edmonton Mall");
        solo.enterText((EditText) solo.getView(R.id.to_address), "Southgate Mall, Edmonton");
        solo.clickOnButton("SHOW ROUTE");
        solo.clickOnButton("CONFIRM");
        assertTrue("Dialog not shown", solo.searchText("Your estimated cost is"));
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}