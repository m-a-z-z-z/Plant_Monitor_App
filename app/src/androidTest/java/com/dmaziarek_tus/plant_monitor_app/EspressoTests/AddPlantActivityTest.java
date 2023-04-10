package com.dmaziarek_tus.plant_monitor_app.EspressoTests;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.activity.AddPlantActivity;
import com.dmaziarek_tus.plant_monitor_app.util.PlantUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AddPlantActivityTest {

    @Rule
    public ActivityScenarioRule<AddPlantActivity> activityRule = new ActivityScenarioRule<>(AddPlantActivity.class);



    @Test
    public void plantNameIsEmpty() {
        onView(withId(R.id.button_AddPlant)).perform(click());
        onView(withId(R.id.editText_PlantName)).check(matches(hasErrorText("Plant name is required")));
    }

    @Test
    public void testAddPlantToDB() {

        onView(withId(R.id.editText_PlantName)).perform(typeText("TestPlant"), closeSoftKeyboard());
        onView(withId(R.id.spinner_PlantType)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withId(R.id.button_AddPlant)).perform(click());


        PlantUtils.retrieveUserPlants();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
//                    ArrayList<String> plantNameList = PlantNamesSingleton.getInstance().getPlantNames();
//                    assertEquals("TestPlant", plantNameList.get(plantNameList.size() - 1));

                    // Verify that a log message was printed
                    onView(withId(android.R.id.content)).perform(swipeUp()); // Scroll to the bottom of the screen
                    onView(withId(android.R.id.list)).check(matches(hasDescendant(withText("onButtonAddPlantClicked - Plant added to database"))));
                }
            }

        };
        thread.start();

    }


}
