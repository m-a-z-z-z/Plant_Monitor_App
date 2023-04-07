package com.dmaziarek_tus.plant_monitor_app.EspressoTests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.activity.SelectPlantActivity;
import com.dmaziarek_tus.plant_monitor_app.activity.PlantHealthActivity;
import com.dmaziarek_tus.plant_monitor_app.util.PlantNamesSingleton;
import com.dmaziarek_tus.plant_monitor_app.util.PlantUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class SelecPlantActivityTest {

    @Rule
    public ActivityScenarioRule<SelectPlantActivity> activityRule = new ActivityScenarioRule<>(SelectPlantActivity.class);

    @Test
    public void clickOnFirstCardView_opensPlantDetails() {

        onView(withId(R.id.card_view1)).perform(click());
        // Check if the PlantDetailsActivity is opened
        intended(hasComponent(PlantHealthActivity.class.getName()));
    }
}
