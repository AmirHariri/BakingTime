package com.example.android.bakingtime;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Created by Amir on 10/25/2017.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTest {
    @Rule public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    @Test
    public  void  clickGridViewItem_OpensRecepieDetailActivity(){

        onData(anything()).inAdapterView(withId(R.id.grid_view_recepies)).atPosition(2).perform(click());
        // Checks that the OrderActivity opens with the correct recepie name displayed
        onView(withId(R.id.tv_recepie_current)).check(matches(withText("Yellow Cake")));

    }
}
