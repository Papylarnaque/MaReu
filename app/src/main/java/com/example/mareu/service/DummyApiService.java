package com.example.mareu.service;

import com.example.mareu.model.Guest;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.view.AddMeetingActivity;

import java.util.ArrayList;
import java.util.List;

public class DummyApiService implements ApiService {

    private final List<Meeting> mMeetings = DummyApiGenerator.generateMeetings();
    private final List<Guest> mGuests = DummyApiGenerator.generateGuests();
    private final List<Room> mRooms = DummyApiGenerator.generateRooms();

    @Override
    public List<Meeting> getMeetings() {
/*        Collections.sort(mMeetings, new Comparator<Meeting>() {
            public int compare(Meeting o1, Meeting o2) {
                if (o1.getDate() == null || o2.getDate() == null)
                    return 0;
                return o1.getDate().compareTo(o2.getDate());
            }
        });*/
        return mMeetings;
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        this.mMeetings.remove(meeting);
    }

    @Override
    public void addMeeting(Meeting meeting) {
        this.mMeetings.add(meeting);
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

    @Override
    public void getGuestsFromEmailsSelected(AddMeetingActivity addMeetingActivity) {
        for (String e : addMeetingActivity.guestsEmails.getText().toString().split(AddMeetingActivity.EMAILS_LIST_SEPARATOR)) {
            for (Guest eG : getGuests()) {
                // Avoid duplicated Guest in the Meeting
                if (e.equals(eG.getEmail()) && !addMeetingActivity.mGuests.contains(eG)) {
                    addMeetingActivity.mGuests.add(eG);
                }
            }
        }
    }
}
