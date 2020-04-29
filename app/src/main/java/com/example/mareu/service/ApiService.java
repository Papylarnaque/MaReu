package com.example.mareu.service;

import com.example.mareu.model.Guest;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.view.AddMeetingActivity;

import java.util.Calendar;
import java.util.List;

public interface ApiService {

    List<Meeting> getMeetings();

    void deleteMeeting(Meeting meeting);

    void addMeeting(Meeting meeting);

    List<Guest> getGuests();

    List<Room> getRooms();

    String[] getRoomsAsStringList();

    List<String> getGuestsEmails(List<Guest> mGuestList);

    void getGuestsFromEmailsSelected(AddMeetingActivity addMeetingActivity);

    List<Meeting> filterMeetingsByDate(Calendar datePicked);

    List<Meeting> setDateSorter();

    List<Meeting> filterMeetingsByRoom(boolean[] checkedRooms);

}
