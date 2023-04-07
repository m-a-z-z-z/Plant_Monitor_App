package com.dmaziarek_tus.plant_monitor_app.EspressoTests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.activity.PlantHealthActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PlantHealthActivityTest {

    @Rule
    public ActivityScenarioRule<PlantHealthActivity> activityRule = new ActivityScenarioRule<>(PlantHealthActivity.class);

    @Test
    public void testPlantNameDisplayed() {
        onView(withId(R.id.textView_plantName)).check(matches(withText("plant_name")));
    }
}
