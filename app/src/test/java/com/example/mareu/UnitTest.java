package com.example.mareu;

import com.example.mareu.di.DI;
import com.example.mareu.model.Guest;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.service.ApiService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class UnitTest {

    private Meeting mMeetingOne;
    private Meeting mMeetingTwo;
    private Meeting mMeetingThree;
    private ApiService apiService;
    private Calendar calendar;

    @Before
    public void setup() {
        apiService = DI.getApiService();
        calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(2020, 5, 6, 21, 42);
        mMeetingOne = new Meeting(6, "Topic", new Date(calendar.getTimeInMillis()), new Date(calendar.getTimeInMillis() + 360000), new Room(5, "Shakespeare", 5), Arrays.asList(
                new Guest(6, "Sydney", "Turner", "sydney.turner@lamzone.com", "https://api.adorable.io/AVATARS/512/6.png")));

        calendar.set(2020, 5, 4, 23, 42);
        mMeetingTwo = new Meeting(7, "Topic", new Date(calendar.getTimeInMillis()), new Date(calendar.getTimeInMillis() + 360000), new Room(5, "Shakespeare", 5), Arrays.asList(
                new Guest(6, "Sydney", "Turner", "sydney.turner@lamzone.com", "https://api.adorable.io/AVATARS/512/6.png")));
        calendar.set(2020, 5, 5, 8, 42);
        mMeetingThree = new Meeting(8, "New app communication", new Date(calendar.getTimeInMillis()), new Date(calendar.getTimeInMillis() + 360000), new Room(8, "Tesla", 8), Arrays.asList(
                new Guest(153, "Patrick", "Cole", "patrick.cole@lamzone.com", "https://api.adorable.io/AVATARS/512/153.png"),
                new Guest(154, "Savana", "Gibson", "savana.gibson@lamzone.com", "https://api.adorable.io/AVATARS/512/154.png"),
                new Guest(155, "Michael", "Scott", "michael.scott@lamzone.com", "https://api.adorable.io/AVATARS/512/155.png")));
    }


    @Test
    public void deleteMeetingWithSuccess() {
        List<Meeting> mMeetings = apiService.getMeetings();
        int startSize = mMeetings.size();
        apiService.deleteMeeting(mMeetings.get(2));
        int endSize = mMeetings.size();
        assertEquals(startSize - 1, endSize);
    }

    @Test
    public void addMeetingWithSuccess() {
        List<Meeting> mMeetings = apiService.getMeetings();
        int startSize = mMeetings.size();
        // add 3 meetings from the set up
        apiService.addMeeting(mMeetingOne);
        assertTrue(apiService.getMeetings().contains(mMeetingOne));
        apiService.addMeeting(mMeetingTwo);
        assertTrue(apiService.getMeetings().contains(mMeetingTwo));
        apiService.addMeeting(mMeetingThree);
        assertTrue(apiService.getMeetings().contains(mMeetingThree));
        int endSize = mMeetings.size();
        assertEquals(startSize + 3, endSize);
        // add a new meeting by duplication
        apiService.addMeeting(mMeetings.get(2));
        endSize = mMeetings.size();
        assertEquals(startSize + 4, endSize);
    }


}