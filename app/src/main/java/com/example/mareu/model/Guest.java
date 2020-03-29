package com.example.mareu.model;

import java.util.Objects;

public class Guest {

    // FIELDS --------------------------------------------------------------------------------------

    private long mId;
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mAvatarUrl;
    private boolean isSelected;

    /**
     * Constructor
     * @param id integer correspond to the id
     * @param firstName a string that contains the first name
     * @param lastName a string that contains the last name
     * @param email a string that contains the email
     */
    public Guest(long id, String firstName, String lastName, String email, String avatarUrl) {
        mId = id;
        mFirstName = firstName;
        mLastName = lastName;
        mEmail = email;
        mAvatarUrl = avatarUrl;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        mAvatarUrl = avatarUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return mId == guest.mId &&
                Objects.equals(mFirstName, guest.mFirstName) &&
                Objects.equals(mLastName, guest.mLastName) &&
                mEmail.equals(guest.mEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mFirstName, mLastName, mEmail);
    }
}
