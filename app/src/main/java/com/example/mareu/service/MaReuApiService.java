package com.example.mareu.service;

import com.example.mareu.model.Guest;
import com.example.mareu.model.Reunion;
import com.example.mareu.model.Room;


import java.util.List;

public interface MaReuApiService {

    List<Reunion> getReunions();

    void deleteReunion(Reunion reunion);

    void addMeeting(Reunion reunion);

    List<Guest> getGuestList();

    List<Guest> getGuestList(List<String> mEmailList);

    List<Room> getRooms();

    List<String> getGuestsEmails(List<Guest> mGuestList);


}
