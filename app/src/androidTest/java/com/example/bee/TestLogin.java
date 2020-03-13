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
    @Test
    public void checkLogin(){
        solo.enterText((EditText) solo.getView(R.id.atvUsernameReg), "Sophie");
        solo.enterText((EditText) solo.getView(R.id.atvPasswordLog), "1234!Q");
        solo.clickOnButton("SIGN IN AS RIDER");
        solo.assertCurrentActivity("Current Activity", EnterAddressMap.class);

    }


}
