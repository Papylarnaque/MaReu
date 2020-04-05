package com.example.mareu.service;

import com.example.mareu.model.Guest;
import com.example.mareu.model.Reunion;
import com.example.mareu.model.Room;

import java.util.ArrayList;
import java.util.List;

public class DummyMaReuApiService implements MaReuApiService {

    private final List<Reunion> mReunions = DummyMaReuApiGenerator.generateReunions();
    private List<Guest> mGuests = DummyMaReuApiGenerator.generateGuests();
    private List<Room> mRooms = DummyMaReuApiGenerator.generateRooms();

    @Override
    public List<Reunion> getReunions() {
        return mReunions;
    }

    @Override
    public void deleteReunion(Reunion reunion) {
        this.mReunions.remove(reunion);
    }

    @Override
    public void addReunion(Reunion reunion) {
        this.mReunions.add(reunion);
    }

    @Override
    public List<Guest> getGuests() {
        return this.mGuests;
    }

    @Override
    public List<Room> getRooms() {
        return this.mRooms;
    }

    @Override
    public List<String> getGuestsEmails(List<Guest> mGuests) {
        ArrayList<String> mEmailsList = new ArrayList<String>();

        for (Guest m : mGuests) mEmailsList.add(m.getEmail());

        return mEmailsList;
    }
}
