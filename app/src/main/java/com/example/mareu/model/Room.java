package com.example.mareu.model;

public class Room {

    private long mId;
    private String mRoomName;
    private long mSeats;

    public Room(long id, String roomName, long seats) {
        mId = id;
        mRoomName = roomName;
        mSeats = seats;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getRoomName() {
        return mRoomName;
    }

    public void setRoomName(String roomName) {
        mRoomName = roomName;
    }

    public long getSeats() {
        return mSeats;
    }

    public void setSeats(long seats) {
        mSeats = seats;
    }
}
