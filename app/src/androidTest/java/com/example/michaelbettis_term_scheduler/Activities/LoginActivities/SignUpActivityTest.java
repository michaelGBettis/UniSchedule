package com.example.michaelbettis_term_scheduler.Activities.LoginActivities;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.michaelbettis_term_scheduler.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@SuppressWarnings("ALL")
public class SignUpActivityTest {

    @Rule
    public final ActivityScenarioRule<SignUpActivity> mActivityScenarioRule = new ActivityScenarioRule<>(SignUpActivity.class);

    private ActivityScenario<SignUpActivity> mActivity = null;
    private View decorView;

    @Before
    public void setUp() {

        mActivity = mActivityScenarioRule.getScenario();

    }

    @Test
    public void empty_first_name_fails(){

        onView(withId(R.id.user_first_name)).perform(typeText(""));
        onView(withId(R.id.user_middle_name)).perform(typeText("Gregory"));
        onView(withId(R.id.user_last_name)).perform(typeText("Bettis"));
        onView(withId(R.id.user_address)).perform(typeText("5855 83rd PL NE Marysville WA 98270"));
        onView(withId(R.id.user_Phone)).perform(typeText("2068416495"));
        onView(withId(R.id.student_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Undergraduate"))).perform(click());
        onView(withId(R.id.college_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Information Technology"))).perform(click());
        onView(withId(R.id.user_sat_score)).perform(typeText("3.2"));
        onView(withId(R.id.user_email)).perform(closeSoftKeyboard()).perform(typeText("mbetti1@wgu.edu"));
        onView(withId(R.id.user_password)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.user_password_check)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.create_account)).perform(closeSoftKeyboard()).perform(click());
        onView(withText("Please enter a value in all fields")).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));

    }

    @Test
    public void empty_middle_name_fails(){

        onView(withId(R.id.user_first_name)).perform(typeText("Michael"));
        onView(withId(R.id.user_middle_name)).perform(typeText(""));
        onView(withId(R.id.user_last_name)).perform(typeText("Bettis"));
        onView(withId(R.id.user_address)).perform(typeText("5855 83rd PL NE Marysville WA 98270"));
        onView(withId(R.id.user_Phone)).perform(typeText("2068416495"));
        onView(withId(R.id.student_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Undergraduate"))).perform(click());
        onView(withId(R.id.college_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Information Technology"))).perform(click());
        onView(withId(R.id.user_sat_score)).perform(typeText("3.2"));
        onView(withId(R.id.user_email)).perform(closeSoftKeyboard()).perform(typeText("mbetti1@wgu.edu"));
        onView(withId(R.id.user_password)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.user_password_check)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.create_account)).perform(closeSoftKeyboard()).perform(click());
        onView(withText("Please enter a value in all fields")).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));

    }

    @Test
    public void empty_last_name_fails(){

        onView(withId(R.id.user_first_name)).perform(typeText("Michael"));
        onView(withId(R.id.user_middle_name)).perform(typeText("Gregory"));
        onView(withId(R.id.user_last_name)).perform(typeText(""));
        onView(withId(R.id.user_address)).perform(typeText("5855 83rd PL NE Marysville WA 98270"));
        onView(withId(R.id.user_Phone)).perform(typeText("2068416495"));
        onView(withId(R.id.student_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Undergraduate"))).perform(click());
        onView(withId(R.id.college_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Information Technology"))).perform(click());
        onView(withId(R.id.user_sat_score)).perform(typeText("3.2"));
        onView(withId(R.id.user_email)).perform(closeSoftKeyboard()).perform(typeText("mbetti1@wgu.edu"));
        onView(withId(R.id.user_password)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.user_password_check)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.create_account)).perform(closeSoftKeyboard()).perform(click());
        onView(withText("Please enter a value in all fields")).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));

    }

    @Test
    public void empty_address_fails(){

        onView(withId(R.id.user_first_name)).perform(typeText("Michael"));
        onView(withId(R.id.user_middle_name)).perform(typeText("Gregory"));
        onView(withId(R.id.user_last_name)).perform(typeText("Bettis"));
        onView(withId(R.id.user_address)).perform(typeText(""));
        onView(withId(R.id.user_Phone)).perform(typeText("2068416495"));
        onView(withId(R.id.student_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Undergraduate"))).perform(click());
        onView(withId(R.id.college_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Information Technology"))).perform(click());
        onView(withId(R.id.user_sat_score)).perform(typeText("3.2"));
        onView(withId(R.id.user_email)).perform(closeSoftKeyboard()).perform(typeText("mbetti1@wgu.edu"));
        onView(withId(R.id.user_password)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.user_password_check)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.create_account)).perform(closeSoftKeyboard()).perform(click());
        onView(withText("Please enter a value in all fields")).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));

    }

    @Test
    public void empty_phone_fails(){

        onView(withId(R.id.user_first_name)).perform(typeText("Michael"));
        onView(withId(R.id.user_middle_name)).perform(typeText("Gregory"));
        onView(withId(R.id.user_last_name)).perform(typeText("Bettis"));
        onView(withId(R.id.user_address)).perform(typeText("5855 83rd PL NE Marysville WA 98270"));
        onView(withId(R.id.user_Phone)).perform(typeText(""));
        onView(withId(R.id.student_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Undergraduate"))).perform(click());
        onView(withId(R.id.college_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Information Technology"))).perform(click());
        onView(withId(R.id.user_sat_score)).perform(closeSoftKeyboard()).perform(typeText("3.2"));
        onView(withId(R.id.user_email)).perform(closeSoftKeyboard()).perform(typeText("mbetti1@wgu.edu"));
        onView(withId(R.id.user_password)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.user_password_check)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.create_account)).perform(closeSoftKeyboard()).perform(click());
        onView(withText("Please enter a value in all fields")).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));

    }

    @Test
    public void empty_sat_score_fails(){

        onView(withId(R.id.user_first_name)).perform(typeText("Michael"));
        onView(withId(R.id.user_middle_name)).perform(typeText("Gregory"));
        onView(withId(R.id.user_last_name)).perform(typeText("Bettis"));
        onView(withId(R.id.user_address)).perform(typeText("5855 83rd PL NE Marysville WA 98270"));
        onView(withId(R.id.user_Phone)).perform(typeText("2068416495"));
        onView(withId(R.id.student_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Undergraduate"))).perform(click());
        onView(withId(R.id.college_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Information Technology"))).perform(click());
        onView(withId(R.id.user_sat_score)).perform(typeText(""));
        onView(withId(R.id.user_email)).perform(closeSoftKeyboard()).perform(typeText("mbetti1@wgu.edu"));
        onView(withId(R.id.user_password)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.user_password_check)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.create_account)).perform(closeSoftKeyboard()).perform(click());
        onView(withText("Please enter a valid value for you SAT score")).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));

    }

    @Test
    public void empty_minor_fails() {

        onView(withId(R.id.user_first_name)).perform(typeText("Michael"));
        onView(withId(R.id.user_middle_name)).perform(typeText("Gregory"));
        onView(withId(R.id.user_last_name)).perform(typeText("Bettis"));
        onView(withId(R.id.user_address)).perform(typeText("5855 83rd PL NE Marysville WA 98270"));
        onView(withId(R.id.user_Phone)).perform(typeText("2068416495"));
        onView(withId(R.id.student_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Graduate"))).perform(click());
        onView(withId(R.id.college_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Information Technology"))).perform(click());
        onView(withId(R.id.user_minor)).perform(typeText(""));
        onView(withId(R.id.user_email)).perform(closeSoftKeyboard()).perform(typeText("mbetti1@wgu.edu"));
        onView(withId(R.id.user_password)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.user_password_check)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.create_account)).perform(closeSoftKeyboard()).perform(click());
        onView(withText("Please enter a value in the Minor field")).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));

    }

    @Test
    public void empty_email_fails(){

        onView(withId(R.id.user_first_name)).perform(typeText("Michael"));
        onView(withId(R.id.user_middle_name)).perform(typeText("Gregory"));
        onView(withId(R.id.user_last_name)).perform(typeText("Bettis"));
        onView(withId(R.id.user_address)).perform(typeText("5855 83rd PL NE Marysville WA 98270"));
        onView(withId(R.id.user_Phone)).perform(typeText("2068416495"));
        onView(withId(R.id.student_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Undergraduate"))).perform(click());
        onView(withId(R.id.college_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Information Technology"))).perform(click());
        onView(withId(R.id.user_sat_score)).perform(typeText("3.2"));
        onView(withId(R.id.user_email)).perform(closeSoftKeyboard()).perform(typeText(""));
        onView(withId(R.id.user_password)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.user_password_check)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.create_account)).perform(closeSoftKeyboard()).perform(click());
        onView(withText("Please enter a value in all fields")).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));

    }

    @Test
    public void empty_password_fails(){

        onView(withId(R.id.user_first_name)).perform(typeText("Michael"));
        onView(withId(R.id.user_middle_name)).perform(typeText("Gregory"));
        onView(withId(R.id.user_last_name)).perform(typeText("Bettis"));
        onView(withId(R.id.user_address)).perform(typeText("5855 83rd PL NE Marysville WA 98270"));
        onView(withId(R.id.user_Phone)).perform(typeText("2068416495"));
        onView(withId(R.id.student_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Undergraduate"))).perform(click());
        onView(withId(R.id.college_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Information Technology"))).perform(click());
        onView(withId(R.id.user_sat_score)).perform(typeText("3.2"));
        onView(withId(R.id.user_email)).perform(closeSoftKeyboard()).perform(typeText("mbetti1@wgu.edu"));
        onView(withId(R.id.user_password)).perform(closeSoftKeyboard()).perform(typeText(""));
        onView(withId(R.id.user_password_check)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.create_account)).perform(closeSoftKeyboard()).perform(click());
        onView(withText("Please enter a value in all fields")).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));

    }

    @Test
    public void empty_password_check_fails(){

        onView(withId(R.id.user_first_name)).perform(typeText("Michael"));
        onView(withId(R.id.user_middle_name)).perform(typeText("Gregory"));
        onView(withId(R.id.user_last_name)).perform(typeText("Bettis"));
        onView(withId(R.id.user_address)).perform(typeText("5855 83rd PL NE Marysville WA 98270"));
        onView(withId(R.id.user_Phone)).perform(typeText("2068416495"));
        onView(withId(R.id.student_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Undergraduate"))).perform(click());
        onView(withId(R.id.college_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Information Technology"))).perform(click());
        onView(withId(R.id.user_sat_score)).perform(typeText("3.2"));
        onView(withId(R.id.user_email)).perform(closeSoftKeyboard()).perform(typeText("mbetti1@wgu.edu"));
        onView(withId(R.id.user_password)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.user_password_check)).perform(closeSoftKeyboard()).perform(typeText(""));
        onView(withId(R.id.create_account)).perform(closeSoftKeyboard()).perform(click());
        onView(withText("Please enter a value in all fields")).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));

    }

    @Test
    public void password_and_password_check_mismatch_fails(){

        onView(withId(R.id.user_first_name)).perform(typeText("Michael"));
        onView(withId(R.id.user_middle_name)).perform(typeText("Gregory"));
        onView(withId(R.id.user_last_name)).perform(typeText("Bettis"));
        onView(withId(R.id.user_address)).perform(typeText("5855 83rd PL NE Marysville WA 98270"));
        onView(withId(R.id.user_Phone)).perform(typeText("2068416495"));
        onView(withId(R.id.student_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Undergraduate"))).perform(click());
        onView(withId(R.id.college_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Information Technology"))).perform(click());
        onView(withId(R.id.user_sat_score)).perform(typeText("3.2"));
        onView(withId(R.id.user_email)).perform(closeSoftKeyboard()).perform(typeText("mbetti1@wgu.edu"));
        onView(withId(R.id.user_password)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.user_password_check)).perform(closeSoftKeyboard()).perform(typeText("admi"));
        onView(withId(R.id.create_account)).perform(closeSoftKeyboard()).perform(click());
        onView(withText("Password does not match")).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));

    }

    @Test
    public void email_matches_a_current_user_fails(){

        onView(withId(R.id.user_first_name)).perform(typeText("Michael"));
        onView(withId(R.id.user_middle_name)).perform(typeText("Gregory"));
        onView(withId(R.id.user_last_name)).perform(typeText("Bettis"));
        onView(withId(R.id.user_address)).perform(typeText("5855 83rd PL NE Marysville WA 98270"));
        onView(withId(R.id.user_Phone)).perform(typeText("2068416495"));
        onView(withId(R.id.student_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Undergraduate"))).perform(click());
        onView(withId(R.id.college_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Information Technology"))).perform(click());
        onView(withId(R.id.user_sat_score)).perform(typeText("3.2"));
        onView(withId(R.id.user_email)).perform(closeSoftKeyboard()).perform(typeText("bettisjr@gmail.com"));
        onView(withId(R.id.user_password)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.user_password_check)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.create_account)).perform(closeSoftKeyboard()).perform(click());
        onView(withText("This email address is already associated with an account.")).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));

    }

    @Test
    public void email_matches_a_current_user_cap_fails(){

        onView(withId(R.id.user_first_name)).perform(typeText("Michael"));
        onView(withId(R.id.user_middle_name)).perform(typeText("Gregory"));
        onView(withId(R.id.user_last_name)).perform(typeText("Bettis"));
        onView(withId(R.id.user_address)).perform(typeText("5855 83rd PL NE Marysville WA 98270"));
        onView(withId(R.id.user_Phone)).perform(typeText("2068416495"));
        onView(withId(R.id.student_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Undergraduate"))).perform(click());
        onView(withId(R.id.college_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Information Technology"))).perform(click());
        onView(withId(R.id.user_sat_score)).perform(typeText("3.2"));
        onView(withId(R.id.user_email)).perform(closeSoftKeyboard()).perform(typeText("BETTISJR@GMAIL.COM"));
        onView(withId(R.id.user_password)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.user_password_check)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.create_account)).perform(closeSoftKeyboard()).perform(click());
        onView(withText("This email address is already associated with an account.")).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));

    }

    @Test
    public void sat_score_too_big_fails() {

        onView(withId(R.id.user_first_name)).perform(typeText("Michael"));
        onView(withId(R.id.user_middle_name)).perform(typeText("Gregory"));
        onView(withId(R.id.user_last_name)).perform(typeText("Bettis"));
        onView(withId(R.id.user_address)).perform(typeText("5855 83rd PL NE Marysville WA 98270"));
        onView(withId(R.id.user_Phone)).perform(typeText("2068416495"));
        onView(withId(R.id.student_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Undergraduate"))).perform(click());
        onView(withId(R.id.college_type)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Information Technology"))).perform(click());
        onView(withId(R.id.user_sat_score)).perform(typeText("5"));
        onView(withId(R.id.user_email)).perform(closeSoftKeyboard()).perform(typeText("mbetti1@wgu.edu"));
        onView(withId(R.id.user_password)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.user_password_check)).perform(closeSoftKeyboard()).perform(typeText("admin"));
        onView(withId(R.id.create_account)).perform(closeSoftKeyboard()).perform(click());
        onView(withText("Please enter a valid value for you SAT score")).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));

    }

    @After
    public void tearDown() {

        mActivity = null;

    }
}