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

    public String getRoomName() {
        return mRoomName;
    }

}
