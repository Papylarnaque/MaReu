package com.example.mareu.service;

import com.example.mareu.model.Guest;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.view.AddMeetingActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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
    public String[] getRoomsAsStringList() {
        int numberRooms = getRooms().size();
        String[] mRooms = new String[numberRooms];
        for (int i = 0; i < numberRooms; i++) {
            mRooms[i] = getRooms().get(i).getRoomName();
        }
        return mRooms;
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

    @Override
    public List<Meeting> filterMeetingsByDate(Calendar datePicked) {
        final List<Meeting> meetings = getMeetings();

        List<Meeting> mMeetingsFiltered = new ArrayList<>();
        int size = meetings.size();
        for (int e = 0; e < size; e++) {
            Calendar mMeetingsCalendar = Calendar.getInstance();
            mMeetingsCalendar.setTime(meetings.get(e).getStartDate());
            if (datePicked.get(Calendar.YEAR) == mMeetingsCalendar.get(Calendar.YEAR)
                    && datePicked.get(Calendar.DAY_OF_YEAR) == mMeetingsCalendar.get(Calendar.DAY_OF_YEAR)) {
                mMeetingsFiltered.add(meetings.get(e));
            }
        }

        return mMeetingsFiltered;
    }


    @Override
    public List<Meeting> setDateSorter() {
        final List<Meeting> meetings = getMeetings();
        Collections.sort(meetings, (o1, o2) -> {
            if (o1.getStartDate() == null || o2.getStartDate() == null)
                return 0;
            return o1.getStartDate().compareTo(o2.getStartDate());
        });
        return meetings;
    }

    @Override
    public List<Meeting> filterMeetingsByRoom(boolean[] checkedRooms) {

        List<Meeting> mMeetingsFiltered = new ArrayList<>();
        String[] mRooms = getRoomsAsStringList();
        // Convert the Rooms array to list
        final List<String> mRoomsList = Arrays.asList(mRooms);
        int sizeMeetingsList = getMeetings().size();

        for (int i = 0; i < checkedRooms.length; i++) {
            boolean checked = checkedRooms[i];
            if (checked) {

                String mRoomFiltered = mRoomsList.get(i);
                for (int e = 0; e < sizeMeetingsList; e++) {
                    if (mRoomFiltered.equals(getMeetings().get(e).getRoom().getRoomName())) {
                        mMeetingsFiltered.add(getMeetings().get(e));
                    }
                }
            }
        }
        return mMeetingsFiltered;
    }

}
