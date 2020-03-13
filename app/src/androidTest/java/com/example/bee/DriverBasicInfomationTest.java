
package com.example.bee;

        import android.app.Activity;
        import android.content.Intent;
        import android.net.Uri;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.fragment.app.FragmentActivity;
        import androidx.test.platform.app.InstrumentationRegistry;
        import androidx.test.rule.ActivityTestRule;

        import com.google.android.gms.maps.SupportMapFragment;
        import com.robotium.solo.Solo;

        import org.junit.After;
        import org.junit.Before;
        import org.junit.Rule;
        import org.junit.Test;

        import static junit.framework.TestCase.assertTrue;

public class  DriverBasicInfomationTest {
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
     * check whether call can work
     */

    @Test
    public void checkCall() {
        solo.assertCurrentActivity("Wrong Activity", DriverBasicInformation.class);
        ImageView phone = (ImageView) solo.getView("phone");
        solo.clickOnView(phone);

    }
    /**
     * check click onDrivername can work
     */

    @Test
    public void checkMail() {
        solo.assertCurrentActivity("Wrong Activity", DriverBasicInformation.class);
        ImageView mail = (ImageView) solo.getView("mail");
        solo.clickOnView(mail);
    }

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
