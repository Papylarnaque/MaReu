package com.example.mareu.model;

import java.util.Objects;

public class Reunion {

    // FIELDS --------------------------------------------------------------------------------------

    private int mId;
    private String mSubject;
    private String mHour;
    private String mRoom;
    private String mGuest;


    /**
     * Constructor
     * @param id id
     * @param subject subject of the meeting
     * @param hour hour that the meeting is starting
     * @param room room of the meeting
     * @param guest members of the meeting
     */
    public Reunion(int id, String subject, String hour, String room, String guest) {
        mId = id;
        mSubject = subject;
        mHour = hour;
        mRoom = room;
        mGuest = guest;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
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

    public String getGuest() {
        return mGuest;
    }

    public void setGuest(String guest) {
        mGuest = guest;
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
                Objects.equals(mGuest, reunion.mGuest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mSubject, mHour, mRoom, mGuest);
    }
}
