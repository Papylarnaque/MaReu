package com.example.mareu.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Reunion {

    // FIELDS --------------------------------------------------------------------------------------

    private long mId;
    private String mSubject;
    private String mHour;
    private String mRoom;
    private List<Guest> mGuestList;


    /**
     * Constructor
     * @param id id
     * @param subject subject of the meeting
     * @param hour hour that the meeting is starting
     * @param room room of the meeting
     * @param guestList members of the meeting
     */
    public Reunion(long id, String subject, String hour, String room, List<Guest> guestList) {
        mId = id;
        mSubject = subject;
        mHour = hour;
        mRoom = room;
        mGuestList = guestList;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String topic) {
        mSubject = topic;
    }

    public String getHour() {
        return mHour;
    }

    public void setHour(String hour) {
        mHour = hour;
    }

    public String getRoom() {
        return mRoom;
    }

    public void setRoom(String room) {
        mRoom = room;
    }

    public List<Guest> getGuestList() {
        return mGuestList;
    }

    public void setGuestList(List<Guest> guestList) {
        mGuestList = guestList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reunion reunion = (Reunion) o;
        return mId == reunion.mId &&
                Objects.equals(mSubject, reunion.mSubject) &&
                Objects.equals(mHour, reunion.mHour) &&
                Objects.equals(mRoom, reunion.mRoom) &&
                Objects.equals(mGuestList, reunion.mGuestList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mSubject, mHour, mRoom, mGuestList);
    }
}
