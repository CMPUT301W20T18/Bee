
package com.example.bee;

import android.app.Activity;
import android.widget.ImageView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class TestDriverBasicInfomation {
    private Solo solo;

    @Rule
    public ActivityTestRule<DriverBasicInformation> rule =
            new ActivityTestRule<>(DriverBasicInformation.class, true, true);

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
        Activity activity = (Activity)rule.getActivity();
    }


    /**
     * Check back button works
     */
    @Test
    public void checkBack() {
        solo.assertCurrentActivity("Wrong Activity", DriverBasicInformation.class);
        ImageView back = (ImageView) solo.getView("back");
        solo.clickOnView(back);
        //solo.assertCurrentActivity("Wrong Activity", DriverBasicInformation.class);
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
