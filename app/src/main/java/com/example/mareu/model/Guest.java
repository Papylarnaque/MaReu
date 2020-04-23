package com.example.mareu.model;

public class Guest {

    // FIELDS --------------------------------------------------------------------------------------

    private long mId;
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mAvatarUrl;

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

    public String getEmail() {
        return mEmail;
    }

}
