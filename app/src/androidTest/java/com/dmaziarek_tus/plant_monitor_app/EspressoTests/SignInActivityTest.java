package com.dmaziarek_tus.plant_monitor_app.EspressoTests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.activity.SignInActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignInActivityTest {

    @Rule
    public ActivityScenarioRule<SignInActivity> activityRule = new ActivityScenarioRule<>(SignInActivity.class);

    @Test
    public void testSignInWithEmailAndPassword() {
        // Enter valid email and password
        onView(withId(R.id.editText_EmailSignIn))
                .perform(typeText("test2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.editText_PasswordSignIn))
                .perform(typeText("test123"), closeSoftKeyboard());

        // Click sign in button
        onView(withId(R.id.button_signIn)).perform(click());
    }

    @Test
    public void testInvalidEmail() {
        // Enter invalid email and valid password
        onView(withId(R.id.editText_EmailSignIn))
                .perform(typeText("invalid_email"), closeSoftKeyboard());
        onView(withId(R.id.editText_PasswordSignIn))
                .perform(typeText("password123"), closeSoftKeyboard());

        // Click sign in button
        onView(withId(R.id.button_signIn)).perform(click());

        // Check if error message is displayed for email field
        onView(withId(R.id.editText_EmailSignIn)).check(matches(hasErrorText("Please enter a valid email")));
    }

    @Test
    public void testInvalidPassword() {
        // Enter valid email and invalid password
        onView(withId(R.id.editText_EmailSignIn))
                .perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.editText_PasswordSignIn))
                .perform(typeText("pass"), closeSoftKeyboard());

        // Click sign in button
        onView(withId(R.id.button_signIn)).perform(click());

        // Check if error message is displayed for password field
        onView(withId(R.id.editText_PasswordSignIn)).check(matches(hasErrorText("Password is required")));
    }

    @Test
    public void testEmptyEmailAndPassword() {
        // Test email field is empty first
        onView(withId(R.id.button_signIn)).perform(click());
        onView(withId(R.id.editText_EmailSignIn))
                .check(matches(hasErrorText("Please enter a valid email")));

        // Enter valid password and click sign in button
        onView(withId(R.id.editText_EmailSignIn))
                .perform(typeText("test@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.button_signIn)).perform(click());

        onView(withId(R.id.editText_PasswordSignIn)).check(matches(hasErrorText("Password is required")));
    }

}
