package com.example.mareu.Model;

import java.util.Objects;

public class Reunion {

    // FIELDS --------------------------------------------------------------------------------------

    private int mId;
    private String mSubject;
    private String mHour;
    private String mRoom;
    private String mMember;


    /**
     * Constructor
     * @param id id
     * @param topic topic of the meeting
     * @param hour hour that the meeting is starting
     * @param room room of the meeting
     * @param member members of the meeting
     */
    public Reunion(int id, String topic, String hour, String room, String member) {
        mId = id;
        mSubject = topic;
        mHour = hour;
        mRoom = room;
        mMember = member;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTopic() {
        return mSubject;
    }

    public void setTopic(String topic) {
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

    public String getMember() {
        return mMember;
    }

    public void setMember(String member) {
        mMember = member;
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
                Objects.equals(mMember, reunion.mMember);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mSubject, mHour, mRoom, mMember);
    }
}
