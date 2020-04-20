package com.example.mareu;

import com.example.mareu.di.DI;
import com.example.mareu.model.Reunion;
import com.example.mareu.service.MaReuApiService;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MaReuUnitTest {

    private final MaReuApiService mMaReuApiService = DI.getNewInstanceApiService();


    @Test
    public void delete_reunion() {
        List<Reunion> mReunions = mMaReuApiService.getReunions();
        int startSize = mReunions.size();
        mMaReuApiService.deleteReunion(mReunions.get(2));
        int endSize = mReunions.size();
        assertEquals(startSize - 1, endSize);
    }

    @Test
    public void add_reunion() {
        List<Reunion> mReunions = mMaReuApiService.getReunions();
        int startSize = mReunions.size();
        mMaReuApiService.addReunion(mReunions.get(2)); // duplicates reunion in position index = 2 in mReunions
        int endSize = mReunions.size();
        assertEquals(startSize + 1, endSize);
    }

}