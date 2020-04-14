package com.example.mareu.service;

import com.example.mareu.model.Guest;
import com.example.mareu.model.Reunion;
import com.example.mareu.model.Room;

import java.util.List;

public interface MaReuApiService {

    List<Reunion> getReunions();

    void deleteReunion(Reunion reunion);

    void addReunion(Reunion reunion);

    List<Guest> getGuests();

    List<Room> getRooms();

    List<String> getGuestsEmails(List<Guest> mGuestList);

}
