package com.example.ahmed.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by ahmed on 9/5/17.
 */

@RunWith(AndroidJUnit4.class)
public class EspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    private IdlingResource mIdlingResource;

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }



    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);

    }


    @Test
    public void test(){


        onView(withId(R.id.rv_recipes_list)).check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.rv_recipes_list).atPosition(0)).check(matches(hasDescendant(withText("Nutella Pie")))).perform(click());
        onView(withId(R.id.btn_recipe_ingredients)).check(matches(isDisplayed())).perform(click());
        onView(withRecyclerView(R.id.rv_ingredients_list).atPosition(0)).check(matches(isDisplayed())).perform(pressBack());
        onView(withRecyclerView(R.id.rv_steps_list).atPosition(0)).check(matches(withText("Recipe Introduction"))).perform(click());
        onView(withId(R.id.tv_step_description)).check(matches(isDisplayed()));

    }



    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
