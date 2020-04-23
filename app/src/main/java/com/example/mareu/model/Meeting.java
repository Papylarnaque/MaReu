package com.example.mareu.model;

import java.util.Date;
import java.util.List;

public class Meeting {

    // FIELDS --------------------------------------------------------------------------------------
    private long mId;
    private String mSubject;
    private Date mStartDate;
    private Date mEndDate;
    private Room mRoom;
    private List<Guest> mGuests;

    /**
     * Constructor
     * @param id            id
     * @param subject       subject of the meeting
     * @param start_date    start_date when the meeting is starting
     * @param end_date      end_date of the meeting // when the meeting is finishing
     * @param room          room of the meeting
     * @param guests        members of the meeting
     */
    public Meeting(long id, String subject, Date start_date, Date end_date, Room room, List<Guest> guests) {
        mId = id;
        mSubject = subject;
        mStartDate = start_date;
        mEndDate = end_date;
        mRoom = room;
        mGuests = guests;
    }

    public String getSubject() {
        return mSubject;
    }
    public Date getStartDate() {
        return mStartDate;
    }
    public Date getEndDate() {
        return mEndDate;
    }
    public Room getRoom() {
        return mRoom;
    }
    public List<Guest> getGuests() {
        return mGuests;
    }

}
