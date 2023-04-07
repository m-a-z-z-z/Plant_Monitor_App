package com.dmaziarek_tus.plant_monitor_app.EspressoTests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.activity.SignUpActivity;
import com.dmaziarek_tus.plant_monitor_app.model.User;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

    @Rule
    public ActivityScenarioRule<SignUpActivity> activityScenarioRule =
            new ActivityScenarioRule<>(SignUpActivity.class);

    @Test
    public void signUpWithInvalidEmail() {
        onView(withId(R.id.editText_UserName))
                .perform(typeText("abc"), closeSoftKeyboard());

        onView(withId(R.id.editText_EmailSignIn))
                .perform(typeText("example.com"), closeSoftKeyboard());

        onView(withId(R.id.editText_PasswordSignIn))
                .perform(typeText("123456"), closeSoftKeyboard());

        onView(withId(R.id.editText_ConfirmPassword))
                .perform(typeText("123456"), closeSoftKeyboard());

        onView(withId(R.id.button_signUp)).perform(click());

        onView(withId(R.id.editText_EmailSignIn)).check(matches(hasErrorText("Please enter a valid email")));
    }

    @Test
    public void signUpWithInvalidUsername() {
        onView(withId(R.id.editText_UserName))
                .perform(typeText(""), closeSoftKeyboard());

        onView(withId(R.id.editText_EmailSignIn))
                .perform(typeText("test@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.editText_PasswordSignIn))
                .perform(typeText("123456"), closeSoftKeyboard());

        onView(withId(R.id.editText_ConfirmPassword))
                .perform(typeText("123456"), closeSoftKeyboard());

        onView(withId(R.id.button_signUp)).perform(click());

        onView(withId(R.id.editText_UserName)).check(matches(hasErrorText("Username is required")));
    }

    @Test
    public void signUpPasswordsDoNotMatch() {
        onView(withId(R.id.editText_UserName)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.editText_EmailSignIn)).perform(typeText("test@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.editText_PasswordSignIn)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.editText_ConfirmPassword)).perform(typeText("password321"), closeSoftKeyboard());
        onView(withId(R.id.button_signUp)).perform(click());

        onView(withId(R.id.editText_ConfirmPassword)).check(matches(hasErrorText("Passwords do not match")));
    }

    @Test
    public void signUpUsernameOrEmailAlreadyTaken() {
        // Set up a user with the same username and email
        User user = new User("test@gmail.com", "test");
        FirebaseDatabase.getInstance().getReference("Users")
                .child("test").setValue(user);

        onView(withId(R.id.editText_UserName)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.editText_EmailSignIn)).perform(typeText("test@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.editText_PasswordSignIn)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.editText_ConfirmPassword)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.button_signUp)).perform(click());

        onView(withId(R.id.editText_EmailSignIn)).check(matches(hasErrorText("Email is already in use")));
        onView(withId(R.id.editText_UserName)).check(matches(hasErrorText("Username is already taken")));
    }

    @Test
    public void testSignUpSuccess() {

        // Enter valid sign up details
        onView(withId(R.id.editText_UserName)).perform(typeText("TestUser"), closeSoftKeyboard());
        onView(withId(R.id.editText_EmailSignIn)).perform(typeText("testuser2@example.com"), closeSoftKeyboard());
        onView(withId(R.id.editText_PasswordSignIn)).perform(typeText("testpassword"), closeSoftKeyboard());
        onView(withId(R.id.editText_ConfirmPassword)).perform(typeText("testpassword"), closeSoftKeyboard());

        // Click sign up button
        onView(withId(R.id.button_signUp)).perform(click());

        // Wait for sign up to complete
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())));
    }


}