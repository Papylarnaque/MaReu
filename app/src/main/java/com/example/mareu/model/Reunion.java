package com.example.mareu.model;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Reunion {

    // FIELDS --------------------------------------------------------------------------------------

    private long mId;
    private String mSubject;
    private Date mDate;
    private Date mDuration;
    private String mRoom;
    private List<Guest> mGuests;

    /**
     * Constructor
     *
     * @param id       id
     * @param subject  subject of the meeting
     * @param date     date of the meeting is starting
     * @param duration duration of the meeting
     * @param room     room of the meeting
     * @param guests   members of the meeting
     */
    public Reunion(long id, String subject, Date date, Date duration, String room, List<Guest> guests) {
        mId = id;
        mSubject = subject;
        mDate = date;
        mDuration = duration;
        mRoom = room;
        mGuests = guests;
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

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Date getDuration() {
        return mDuration;
    }

    public void setDuration(Date duration) {
        mDuration = duration;
    }

    public String getRoom() {
        return mRoom;
    }

    public void setRoom(String room) {
        mRoom = room;
    }

    public List<Guest> getGuests() {
        return mGuests;
    }

    public void setGuests(List<Guest> guests) {
        mGuests = guests;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reunion reunion = (Reunion) o;
        return mId == reunion.mId &&
                Objects.equals(mSubject, reunion.mSubject) &&
                //Objects.equals(mDate, reunion.mDate) &&
                Objects.equals(mRoom, reunion.mRoom) &&
                Objects.equals(mGuests, reunion.mGuests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mSubject, /*mDate,*/ mRoom, mGuests);
    }

}
