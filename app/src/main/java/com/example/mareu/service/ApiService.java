package com.example.mareu.service;

import android.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.mareu.R;
import com.example.mareu.model.Guest;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.view.AddMeetingActivity;
import com.example.mareu.view.ListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public interface ApiService {

    List<Meeting> getMeetings();

    void deleteMeeting(Meeting meeting);

    void addMeeting(Meeting meeting);

    List<Guest> getGuests();

    List<Room> getRooms();

    List<String> getGuestsEmails(List<Guest> mGuestList);

    void getGuestsFromEmailsSelected(AddMeetingActivity addMeetingActivity);

    default void filterMeetingsByDate(ListActivity listActivity, Calendar datePicked) {
        final List<Meeting> meetings = getMeetings();

        List<Meeting> mMeetingsFiltered = new ArrayList<>();
        int size = meetings.size();
        for (int e = 0; e < size; e++) {
            Calendar mMeetingsCalendar = Calendar.getInstance();
            mMeetingsCalendar.setTime(meetings.get(e).getStartDate());
            if (datePicked.get(Calendar.YEAR) == mMeetingsCalendar.get(Calendar.YEAR)
                    && datePicked.get(Calendar.DAY_OF_YEAR) == mMeetingsCalendar.get(Calendar.DAY_OF_YEAR)) {
                mMeetingsFiltered.add(meetings.get(e));
            }
        }

        listActivity.adapter.setData(mMeetingsFiltered);
    }

    default void setRoomsFilter(ListActivity listActivity) {
        final List<Meeting> meetings = getMeetings();
        // Build an AlertDialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(listActivity);
        // String array for alert dialog multi choice items
        final int numberRooms = getRooms().size();
        String[] mRooms = new String[numberRooms];
        for (int i = 0; i < numberRooms; i++) {
            mRooms[i] = getRooms().get(i).getRoomName();
        }
        // Boolean array for initial selected items
        final boolean[] checkedRooms = new boolean[numberRooms];
        // Convert the Rooms array to list
        final List<String> mRoomsList = Arrays.asList(mRooms);

        // Keep memory of the filter selection
        if (listActivity.KEEP_FILTER_ROOM != null) {
            System.arraycopy(listActivity.KEEP_FILTER_ROOM, 0, checkedRooms, 0, numberRooms);
        }

        // Set multiple choice items for alert dialog
        builder.setMultiChoiceItems(mRooms, checkedRooms, (dialog, which, isChecked) -> {
            // Update the current focused item's checked status
            checkedRooms[which] = isChecked;
        });

        // Specify the dialog is not cancelable
        builder.setCancelable(false);

        // Set a title for alert dialog
        builder.setTitle(R.string.filter_rooms_text);


        // Set the positive/yes button click listener
        builder.setPositiveButton(R.string.filter_ok_text, (dialog, which) -> {
        });

        // Set the negative/no button click listener
        builder.setNeutralButton(R.string.filter_reset_text, (dialog, which) -> {
            listActivity.adapter.setData(meetings);
            listActivity.KEEP_FILTER_ROOM = null;
        });

        final AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.setOnShowListener(dialogInterface -> {

            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                // Checks the checked rooms
                List<Meeting> mMeetingsFiltered = new ArrayList<>();
                int sizeMeetingsList = meetings.size();
                boolean atLeastOneChecked = false;
                for (int i = 0; i < checkedRooms.length; i++) {
                    boolean checked = checkedRooms[i];
                    if (checked) {
                        atLeastOneChecked = true;
                        String mRoomFiltered = mRoomsList.get(i);
                        for (int e = 0; e < sizeMeetingsList; e++) {
                            if (mRoomFiltered.equals(meetings.get(e).getRoom().getRoomName())) {
                                mMeetingsFiltered.add(meetings.get(e));
                            }
                        }
                    }
                }
                // if no rooms checked, Toast to alert user
                if (!atLeastOneChecked) {
                    Toast toastRoomNotSelected = Toast.makeText(listActivity.getApplicationContext(), R.string.toast_room_not_selected, Toast.LENGTH_LONG);
                    toastRoomNotSelected.setGravity(Gravity.CENTER, 0, 0);
                    View toastViewCreateMeeting = toastRoomNotSelected.getView();
                    TextView toastTextCreateMeeting = toastViewCreateMeeting.findViewById(android.R.id.message);
                    toastTextCreateMeeting.setTextColor(ContextCompat.getColor(listActivity.getApplicationContext(), R.color.toast_add_meeting_color));
                    toastRoomNotSelected.show();
                } // Otherwise, show the selection result
                else {
                    dialog.dismiss();
                    listActivity.adapter.setData(mMeetingsFiltered);
                    listActivity.KEEP_FILTER_ROOM = checkedRooms;
                }
            });
        });
        dialog.show();

    }

    default List<Meeting> setDateSorter() {
        final List<Meeting> meetings = getMeetings();
        Collections.sort(meetings, (o1, o2) -> {
            if (o1.getStartDate() == null || o2.getStartDate() == null)
                return 0;
            return o1.getStartDate().compareTo(o2.getStartDate());
        });
        return meetings;
    }
}
