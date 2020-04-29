package com.example.mareu;

import com.example.mareu.di.DI;
import com.example.mareu.model.Guest;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.service.ApiService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {

    private Meeting mMeetingOne;
    private Meeting mMeetingTwo;
    private Meeting mMeetingThree;
    private ApiService apiService;
    private Calendar calendar;

    @Before
    public void setup() {
        apiService = DI.getNewInstanceApiService();
        calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(2020, 5, 10, 21, 42);
        mMeetingOne = new Meeting(6, "Topic", new Date(calendar.getTimeInMillis()), new Date(calendar.getTimeInMillis() + 360000), new Room(10, "Bonaparte", 10), Arrays.asList(
                new Guest(6, "Sydney", "Turner", "sydney.turner@lamzone.com", "https://api.adorable.io/AVATARS/512/6.png")));

        calendar.set(2020, 5, 11, 23, 42);
        mMeetingTwo = new Meeting(7, "Topic", new Date(calendar.getTimeInMillis()), new Date(calendar.getTimeInMillis() + 360000), new Room(10, "Bonaparte", 10), Arrays.asList(
                new Guest(6, "Sydney", "Turner", "sydney.turner@lamzone.com", "https://api.adorable.io/AVATARS/512/6.png")));
        calendar.set(2020, 5, 12, 8, 42);
        mMeetingThree = new Meeting(8, "New app communication", new Date(calendar.getTimeInMillis()), new Date(calendar.getTimeInMillis() + 360000), new Room(9, "Buddha", 10), Arrays.asList(
                new Guest(153, "Patrick", "Cole", "patrick.cole@lamzone.com", "https://api.adorable.io/AVATARS/512/153.png"),
                new Guest(154, "Savana", "Gibson", "savana.gibson@lamzone.com", "https://api.adorable.io/AVATARS/512/154.png"),
                new Guest(155, "Michael", "Scott", "michael.scott@lamzone.com", "https://api.adorable.io/AVATARS/512/155.png")));
    }

    /**
     * Test thats the add meeting function creates a new meeting to the list
     */
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


    /**
     * Test that the delete meeting function delete a meeting from the list
     */
    @Test
    public void deleteMeetingWithSuccess() {
        List<Meeting> mMeetings = apiService.getMeetings();
        int startSize = mMeetings.size();
        apiService.deleteMeeting(mMeetings.get(2));
        int endSize = mMeetings.size();
        assertEquals(startSize - 1, endSize);
    }

    /**
     * Tests that the filter [apiService.filterMeetingsByDate(calendar)]
     * gives the List<Meeting> whose meetings have the same date as the calendar in argument
     */
    @Test
    public void filterByDateWithSuccess() {
        // We add the three meetings
        apiService.addMeeting(mMeetingOne); // 2020 May 10th
        apiService.addMeeting(mMeetingTwo); // 2020 May 11th
        apiService.addMeeting(mMeetingThree); // 2020 May 12th

        // We create a list for each meeting, to check the list returned just contain the linked meeting
        List<Meeting> listOne = new ArrayList<Meeting>(Collections.singleton(mMeetingOne));
        List<Meeting> listTwo = new ArrayList<Meeting>(Collections.singleton(mMeetingTwo));
        List<Meeting> listThree = new ArrayList<Meeting>(Collections.singleton(mMeetingThree));

        calendar.set(2020, 5, 10, 8, 0); // meetingOne
        // What is returned should just contain the meeting one
        Assert.assertEquals(apiService.filterMeetingsByDate(calendar), listOne);
        // and so meeting2 and meeting3 are not in the List
        Assert.assertFalse(apiService.filterMeetingsByDate(calendar).contains(mMeetingTwo));
        Assert.assertFalse(apiService.filterMeetingsByDate(calendar).contains(mMeetingThree));

        calendar.set(2020, 5, 11, 8, 0); // meetingTwo
        Assert.assertEquals(apiService.filterMeetingsByDate(calendar), listTwo);

        calendar.set(2020, 5, 12, 8, 0); // meetingThree
        Assert.assertEquals(apiService.filterMeetingsByDate(calendar), listThree);

        calendar.set(9999, 9, 9, 8, 0);
        // No Meeting should fit that date, so the list returned should be empty
        Assert.assertTrue(apiService.filterMeetingsByDate(calendar).isEmpty());
    }


    /**
     * Tests that the filter [apiService.filterMeetingsByDate(calendar)]
     * gives the List<Meeting> whose meetings have the same date as the calendar in argument
     */
    @Test
    public void filterByRoomWithSuccess() {
        final int numberRooms = apiService.getRooms().size();
        boolean[] checkedRooms = new boolean[numberRooms];
        // We add the three meetings
        apiService.addMeeting(mMeetingOne); // room Bonaparte
        apiService.addMeeting(mMeetingTwo); // room Bonaparte
        apiService.addMeeting(mMeetingThree); // room Buddha

        // We create a list for each meeting, to check the list returned just contain the linked meeting
        List<Meeting> listBonaparte = new ArrayList<>();
        listBonaparte.add(mMeetingOne);
        listBonaparte.add(mMeetingTwo);

        //***********TEST ONE ROOM CHECKED AT A TIME******//
        checkedRooms[9] = true; // Bonaparte
        // What is returned should just contain the meeting three with room BONAPARTE
        Assert.assertEquals(apiService.filterMeetingsByRoom(checkedRooms), listBonaparte);

        Arrays.fill(checkedRooms, Boolean.FALSE);
        checkedRooms[2] = true; // Da Vinci - no meeting present with that room => empty
        // What is returned should be EMPTY
        Assert.assertTrue(apiService.filterMeetingsByRoom(checkedRooms).isEmpty());

        //***********TEST FEW ROOMS CHECKED AT A TIME******//
        checkedRooms[9] = true; // Da Vinci + Bonaparte
        // What is returned should just contain the meeting three with room BONAPARTE
        Assert.assertEquals(apiService.filterMeetingsByRoom(checkedRooms), listBonaparte);

        checkedRooms[8] = true; // Da Vinci + Bonaparte + Buddha
        // What is returned should contain the meetings with BONAPARTE & BUDDHA ( no meeting for Da vinci )
        Assert.assertNotEquals(apiService.filterMeetingsByRoom(checkedRooms), listBonaparte);
        Assert.assertTrue(apiService.filterMeetingsByRoom(checkedRooms).contains(mMeetingOne));
        Assert.assertTrue(apiService.filterMeetingsByRoom(checkedRooms).contains(mMeetingTwo));
        // We create a list for each meeting, to check the list returned just contain the linked meeting
        List<Meeting> listBonaparteAndBuddha = new ArrayList<>();
        listBonaparteAndBuddha.add(mMeetingThree); // in the same order than the returned list
        listBonaparteAndBuddha.add(mMeetingOne);
        listBonaparteAndBuddha.add(mMeetingTwo);
        Assert.assertEquals(apiService.filterMeetingsByRoom(checkedRooms), listBonaparteAndBuddha);

    }


}