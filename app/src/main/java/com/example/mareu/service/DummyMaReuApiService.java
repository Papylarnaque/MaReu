package com.example.mareu.service;

import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import com.example.mareu.R;
import com.example.mareu.model.Guest;
import com.example.mareu.model.Reunion;
import com.example.mareu.model.Room;
import com.example.mareu.view.AddReunionActivity;

import java.util.ArrayList;
import java.util.List;

public class DummyMaReuApiService implements MaReuApiService {

    private final List<Reunion> mReunions = DummyMaReuApiGenerator.generateReunions();
    private final List<Guest> mGuests = DummyMaReuApiGenerator.generateGuests();
    private final List<Room> mRooms = DummyMaReuApiGenerator.generateRooms();

    @Override
    public List<Reunion> getReunions() {
/*        Collections.sort(mReunions, new Comparator<Reunion>() {
            public int compare(Reunion o1, Reunion o2) {
                if (o1.getDate() == null || o2.getDate() == null)
                    return 0;
                return o1.getDate().compareTo(o2.getDate());
            }
        });*/
        return mReunions;
    }

    @Override
    public void deleteReunion(Reunion reunion) {
        this.mReunions.remove(reunion);
    }

    @Override
    public void addReunion(Reunion reunion) {
        this.mReunions.add(reunion);
    }

    @Override
    public List<Guest> getGuests() {
        return this.mGuests;
    }

    @Override
    public List<Room> getRooms() {
        return this.mRooms;
    }

    @Override
    public List<String> getGuestsEmails(List<Guest> mGuests) {
        ArrayList<String> mEmailsList = new ArrayList<>();
        for (Guest m : mGuests) mEmailsList.add(m.getEmail());
        return mEmailsList;
    }

    // ROOMS SPINNER - Sets the spinner for the Room selection
    @Override
    public void setRoomsArrayAdapter(AddReunionActivity addReunionActivity) {
        ArrayList<String> mRoomsList = new ArrayList<>();
        mRoomsList.add(0, addReunionActivity.getResources().getString(R.string.room_add_reunion_text));
        for (Room roomIterator : getRooms()) {
            String roomIteratorString = roomIterator.getRoomName();
            if (!mRoomsList.contains(roomIteratorString)) {
                mRoomsList.add(roomIteratorString);
                String[] mRoomsArray = mRoomsList.toArray(new String[0]);
                ArrayAdapter<String> adapterRooms
                        = new ArrayAdapter<>(addReunionActivity, android.R.layout.simple_list_item_1, mRoomsArray);
                addReunionActivity.mRoom.setAdapter(adapterRooms);
            }
        }
    }

    // GUESTS LIST MENU - Sets the autocompletion for the Guests selection
    @Override
    public void setGuestsArrayAdapter(AddReunionActivity addReunionActivity) {
        String[] guestsEmailsList = getGuestsEmails(getGuests()).toArray(new String[0]);
        ArrayAdapter<String> adapterGuests
                = new ArrayAdapter<>(addReunionActivity, android.R.layout.simple_list_item_1, guestsEmailsList);
        addReunionActivity.guestsEmails.setAdapter(adapterGuests);
        addReunionActivity.guestsEmails.setThreshold(1);                                                  // Sets the minimum number of characters, to show suggestions
        addReunionActivity.guestsEmails.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());     // then separates them with a comma
    }

    // Gets the Guests from the emails chosen in the form
    @Override
    public void getGuestsFromEmailsSelected(AddReunionActivity addReunionActivity) {
        for (String e : addReunionActivity.guestsEmails.getText().toString().split(AddReunionActivity.SEPARATOR)) {
            for (Guest eG : getGuests()) {
                // Avoid duplicated Guest in the Reunion
                if (e.equals(eG.getEmail()) && !addReunionActivity.mGuests.contains(eG)) {
                    addReunionActivity.mGuests.add(eG);
                }
            }
        }
    }
}
