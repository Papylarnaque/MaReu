package com.example.mareu.service;

import com.example.mareu.model.Guest;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.view.AddMeetingActivity;

import java.util.List;

public interface ApiService {

    List<Meeting> getReunions();

    void deleteReunion(Meeting meeting);

    void addReunion(Meeting meeting);

    List<Guest> getGuests();

    List<Room> getRooms();

    List<String> getGuestsEmails(List<Guest> mGuestList);

    void getGuestsFromEmailsSelected(AddMeetingActivity addReunionActivity);
}
