package com.example.mareu;

import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.ApiService;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {

    private final ApiService apiService = DI.getNewInstanceApiService();


    @Test
    public void delete_meeting() {
        List<Meeting> mMeetings = apiService.getReunions();
        int startSize = mMeetings.size();
        apiService.deleteReunion(mMeetings.get(2));
        int endSize = mMeetings.size();
        assertEquals(startSize - 1, endSize);
    }

    @Test
    public void add_meeting() {
        List<Meeting> mMeetings = apiService.getReunions();
        int startSize = mMeetings.size();
        apiService.addReunion(mMeetings.get(2)); // duplicates meeting in position index = 2 in mReunions
        int endSize = mMeetings.size();
        assertEquals(startSize + 1, endSize);
    }

}