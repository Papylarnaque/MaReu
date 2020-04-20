package com.example.mareu.service;

import com.example.mareu.model.Guest;
import com.example.mareu.model.Reunion;
import com.example.mareu.model.Room;
import com.example.mareu.view.AddReunionActivity;

import java.util.List;

public interface MaReuApiService {

    List<Reunion> getReunions();

    void deleteReunion(Reunion reunion);

    void addReunion(Reunion reunion);

    List<Guest> getGuests();

    List<Room> getRooms();

    List<String> getGuestsEmails(List<Guest> mGuestList);

    // ROOMS SPINNER - Sets the spinner for the Room selection
    void setRoomsArrayAdapter(AddReunionActivity addReunionActivity);

    // GUESTS LIST MENU - Sets the autocompletion for the Guests selection
    void setGuestsArrayAdapter(AddReunionActivity addReunionActivity);

    // Gets the Guests from the emails chosen in the form
    void getGuestsFromEmailsSelected(AddReunionActivity addReunionActivity);
}
