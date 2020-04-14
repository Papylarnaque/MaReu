package com.example.mareu.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Reunion {

    // FIELDS --------------------------------------------------------------------------------------

    private long mId;
    private String mSubject;
    private Date mStartDate;
    private Date mEndDate;
    private Room mRoom;
    private List<Guest> mGuests;

    /**
     * Constructor
     *
     * @param id            id
     * @param subject       subject of the meeting
     * @param start_date    start_date when the meeting is starting
     * @param end_date      end_date of the meeting // when the meeting is finishing
     * @param room          room of the meeting
     * @param guests        members of the meeting
     */
    public Reunion(long id, String subject, Date start_date, Date end_date, Room room, List<Guest> guests) {
        mId = id;
        mSubject = subject;
        mStartDate = start_date;
        mEndDate = end_date;
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
        return mStartDate;
    }

    public void setDate(Date date) {
        mStartDate = date;
    }

    public Date getDuration() {
        return mEndDate;
    }

    public void setDuration(Date duration) {
        mEndDate = duration;
    }

    public Room getRoom() {
        return mRoom;
    }

    public void setRoom(Room room) {
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
                Objects.equals(mStartDate, reunion.mStartDate) &&
                Objects.equals(mEndDate, reunion.mEndDate) &&
                Objects.equals(mRoom, reunion.mRoom) &&
                Objects.equals(mGuests, reunion.mGuests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mSubject, mStartDate, mEndDate, mRoom, mGuests);
    }

}
