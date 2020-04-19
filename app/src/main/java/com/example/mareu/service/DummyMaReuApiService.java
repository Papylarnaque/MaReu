package com.example.mareu.service;

import com.example.mareu.model.Guest;
import com.example.mareu.model.Reunion;
import com.example.mareu.model.Room;

import java.util.ArrayList;
import java.util.List;

public class DummyMaReuApiService implements MaReuApiService {

    private final List<Reunion> mReunions = DummyMaReuApiGenerator.generateReunions();
    private final List<Guest> mGuests = DummyMaReuApiGenerator.generateGuests();
    private final List<Room> mRooms = DummyMaReuApiGenerator.generateRooms();

    @Override
    public List<Reunion> getReunions() {
/*        Collections.sort(mReunions, new Comparator<Reunion>() {
            public int compare(Reunion o1, Reunion o2) {
                if (o1.getDate() == null || o2.getDate() == null)
                    return 0;
                return o1.getDate().compareTo(o2.getDate());
            }
        });*/
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
        ArrayList<String> mEmailsList = new ArrayList<>();
        for (Guest m : mGuests) mEmailsList.add(m.getEmail());
        return mEmailsList;
    }

}
