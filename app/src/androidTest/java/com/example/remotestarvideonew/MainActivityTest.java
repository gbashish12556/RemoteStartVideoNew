package com.example.remotestarvideonew;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static com.example.remotestarvideonew.TestHelper.withRecyclerView;

import static org.hamcrest.Matchers.not;



@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final String TAG = "MainActivityScreenTest";
    private IdlingResource mIdlingResource;

    @Rule
    public AnimationsRule rule = new AnimationsRule();

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class) {

        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
        }

    };

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void check_if_view_click_is_working_fine(){

        //Click on recycler view first position
        onView(withRecyclerView(R.id.recyclerView)
                .atPositionOnView(0, R.id.holder)).perform(click());

        //Check if description section is visible
        onView(withRecyclerView(R.id.recyclerView)
                .atPositionOnView(0, R.id.description)).check(matches(isDisplayed()));

        //Click on recycler view first position again
        onView(withRecyclerView(R.id.recyclerView)
                .atPositionOnView(0, R.id.holder)).perform(click());

        //Check if description section is invisible
        onView(withRecyclerView(R.id.recyclerView)
                .atPositionOnView(0, R.id.description)).check(matches(not(isDisplayed())));

    }

    @Test
    public void check_if_error_layout_is_visible(){
        mActivityTestRule.getActivity().runOnUiThread(new Runnable() {
                  public void run() {
                      mActivityTestRule.getActivity().apiFailed();
                  }
              }
        );
        onView(withId(R.id.errorOuterLayout)).check(matches(isDisplayed()));

        //Click on retry button and check if recycler view is visible
        onView(withId(R.id.retryButton)).perform(click());

        //Check if description section is visible
        onView(withRecyclerView(R.id.recyclerView)
                .atPositionOnView(0, R.id.holder)).check(matches(isDisplayed()));
    }

}