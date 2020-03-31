package com.example.mareu.service;

import com.example.mareu.model.Guest;
import com.example.mareu.model.Reunion;
import com.example.mareu.model.Room;

import java.util.List;

public class DummyMaReuApiService implements MaReuApiService {

    private final List<Reunion> mReunions = DummyMaReuApiGenerator.generateReunions();
    private List<Guest> mGuestsList = DummyMaReuApiGenerator.generateGuestsList();
    private List<Room> mRooms = DummyMaReuApiGenerator.generateRoomsList();

    @Override
    public List<Reunion> getReunions() {
        return mReunions;
    }

    @Override
    public void deleteReunion(Reunion reunion) {
        this.mReunions.remove(reunion);
    }

    @Override
    public void addMeeting(Reunion reunion) {
        this.mReunions.add(reunion);
    }

    @Override
    public List<Guest> getGuestList() {
        return this.mGuestsList;
    }

    @Override
    public List<Guest> getGuestList(List<String> mEmailList) {
/*        List<Guest> mGuestList = null;
        for ( String n : mEmailList){
            for (Guest m : )
            Guest mGuest = null;
            mGuestList.add(mGuest);
    }*/
        return null;
    }

    @Override
    public List<Room> getRooms() {
        return this.mRooms;
    }

    @Override
    public List<String> getGuestsEmails(List<Guest> mGuestList) {
        List<String> mEmailList;
        //for ( Reunion n : mReunions ) {
        //for ( Guest m : mGuestList) mEmailList += (m.getEmail()) + ", ";
        for ( Guest m : mGuestList) mEmailList.add((m.getEmail()));
        //}
        return mEmailList;
    }
}
