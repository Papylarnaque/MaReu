package com.example.mareu;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.ApiService;
import com.example.mareu.utils.DeleteViewAction;
import com.example.mareu.view.ListActivity;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.doubleClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {

    // This is fixed
    private static final ApiService apiService = DI.getNewInstanceApiService();
    private static final int INITIAL_LIST_SIZE = apiService.getMeetings().size();
    @Rule
    public final ActivityTestRule<ListActivity> mActivityRule =
            new ActivityTestRule<>(ListActivity.class);

    @Before
    public void setUp() {
        ListActivity mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void MaReuList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(withId(R.id.list_recycler_view)).check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void add_meeting() {
        // Given : We check that the count of items is equal to INITIAL_LIST_SIZE
        onView(withId(R.id.list_recycler_view)).check(withItemCount(INITIAL_LIST_SIZE));
        // Click on the creation button for a new meeting
        onView(withId(R.id.button_add_meeting))
                .perform(click());
        // SUBJECT FILLING
        onView(withId(R.id.edit_text_add_meeting_subject))
                .perform(click());
        onView(withId(R.id.edit_text_add_meeting_subject))
                .perform(typeText("New meeting"));
        // START DATE FILLING
        onView(withId(R.id.text_add_meeting_datepicker))
                .perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 6, 6));
        onView(withText("OK")).perform(click());
        // START TIME FILLING
        onView(withId(R.id.text_add_meeting_timepicker))
                .perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15, 0));
        onView(withText("OK")).perform(click());
        // DURATION FILLING
        onView(withId(R.id.numberpicker_add_meeting_duration_hours_))
                .perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.TOP_CENTER, Press.FINGER, 1, 0));
        onView(withId(R.id.numberpicker_add_meeting_duration_minutes_))
                .perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.TOP_CENTER, Press.FINGER, 1, 0));
        // ROOM FILLING
        onView(withId(R.id.spinner_add_meeting_room))
                .perform(click());
        onData(anything()).atPosition(1).perform(click());
        // GUESTS FILLING
        onView(withId(R.id.autocomplete_text_add_meeting_guests))
                .perform(typeText("f"));
        onData(anything()).atPosition(1).perform(click());

        // Click on the creation button for a new meeting
        onView(withId(R.id.menu_overflow_button_create_meeting))
                .perform(click());
        // Click on the item menu filter by date
        onView(withText(R.string.menu_creation_meeting))
                .perform(click());
        // Result : We check that the count of items is equal to INITIAL_LIST_SIZE+1
        onView(withId(R.id.list_recycler_view)).check(withItemCount(INITIAL_LIST_SIZE + 1));
    }

    @Test
    public void add_meeting_missing_subject() {
        // Click on the creation button for a new meeting
        onView(withId(R.id.button_add_meeting))
                .perform(click());

        // Click on the creation button for a new meeting
        onView(withId(R.id.menu_overflow_button_create_meeting))
                .perform(doubleClick());

        // Result : We check that the Toast is displayed on screen
        onView(withText(R.string.toast_subject_empty)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void delete_meeting() {
        // Given : We check that the count of items is equal to INITIAL_LIST_SIZE
        onView(withId(R.id.list_recycler_view)).check(withItemCount(INITIAL_LIST_SIZE));
        // Push on delete button for meeting at index = 1
        onView(withId(R.id.list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // We check that the count of items is equal to INITIAL_LIST_SIZE
        onView(withId(R.id.list_recycler_view)).check(withItemCount(INITIAL_LIST_SIZE - 1));
    }

    @Test
    public void filter_meeting_by_date() {
        // Open the overflow menu
        onView(withId(R.id.menu_overflow_button_create_meeting))
                .perform(click());
        // Click on the item menu filter by date
        onView(withText(R.string.menu_filter_date))
                .perform(click());
        // Pick a date, example 6th may 2020 ( 2 meetings hardcoded in DummyMaReuApiGenerator for this date )
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 5, 6));
        onView(withText(R.string.filter_ok_text)).perform(click());
        // We check that the count of items is 2 <-> Because 2 meetings hardcoded in DummyMaReuApiGenerator
        onView(withId(R.id.list_recycler_view)).check(withItemCount(2));
        // #################### REPEAT for another date picked ! #####################
        // Open the overflow menu
        onView(withId(R.id.menu_overflow_button_create_meeting))
                .perform(click());
        // Click on the item menu filter by date
        onView(withText(R.string.menu_filter_date))
                .perform(click());

        // Pick another date, example 6th may 2019 ( 0 meetings ! )
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 5, 6));
        onView(withText(R.string.filter_ok_text)).perform(click());
        // We check that the count of items is 0
        onView(withId(R.id.list_recycler_view)).check(withItemCount(0));
    }

    @Test
    public void filter_meeting_by_room() {
        // Before setting the filter => INITIAL_LIST_SIZE
        onView(withId(R.id.list_recycler_view)).check(withItemCount(INITIAL_LIST_SIZE));
        // Open the overflow menu
        onView(withId(R.id.menu_overflow_button_create_meeting))
                .perform(click());
        // Click on the item menu filter by date
        onView(withText(R.string.menu_filter_room))
                .perform(click());
        // Pick a room ("Shakespeare" = 2 meetings hardcoded)
        String room = "Shakespeare";
        onView(withText(room)).perform(click());
        onView(withText(R.string.filter_ok_text)).perform(click());
        onView(withId(R.id.list_recycler_view)).check(withItemCount(getNumberMeetingsWithRoomText(room)));
        // ##################### RESET FILTER #####################
        // Open the overflow menu
        onView(withId(R.id.menu_overflow_button_create_meeting))
                .perform(click());
        // Click on the item menu filter by date
        onView(withText(R.string.menu_filter_room))
                .perform(click());
        // Reset the filter => INITIAL_LIST_SIZE
        onView(withText(R.string.filter_reset_text)).perform(click());
        onView(withId(R.id.list_recycler_view)).check(withItemCount(INITIAL_LIST_SIZE));
    }

    private int getNumberMeetingsWithRoomText(String room) {
        int numberMeetingsWithRoomText = 0;
        for (Meeting r : apiService.getMeetings()) {
            if (r.getRoom().getRoomName().equals(room)) numberMeetingsWithRoomText += 1;
        }
        return numberMeetingsWithRoomText;
    }

}
