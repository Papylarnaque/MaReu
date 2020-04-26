package com.example.mareu.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.R;
import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private boolean[] KEEP_FILTER_ROOM;    // Keeps memory of the room filter selection
    private MyAdapter adapter;
    private ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpRecyclerView();

        createNewReunionAction();
    }

    //  ****************************************** INIT  *******************************************
    private void setUpRecyclerView() {
        final RecyclerView rv = findViewById(R.id.list_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter();
        rv.setAdapter(adapter);

        apiService = DI.getApiService();
        adapter.setData(apiService.getReunions());
    }

    //  ****************************************** MENU ********************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.menu_sort_date: {
                setDateSorter();

                return true;
            }

            case R.id.menu_filter_date: {
                setDateFilter();
                return true;
            }
            case R.id.menu_filter_room: {
                setRoomsFilter();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //  ****************************************** ACTIONS  ****************************************

    private void createNewReunionAction() {
        FloatingActionButton mButtonNewReunion = findViewById(R.id.button_add_meeting);
        mButtonNewReunion.setOnClickListener(v -> {
            Intent intent = new Intent(ListActivity.this, AddMeetingActivity.class);
            startActivity(intent);
        });
    }

    private void setDateSorter() {
        final List<Meeting> mMeetings = apiService.getReunions();
        Collections.sort(mMeetings, (o1, o2) -> {
            if (o1.getStartDate() == null || o2.getStartDate() == null)
                return 0;
            return o1.getStartDate().compareTo(o2.getStartDate());
        });
        adapter.setData(mMeetings);
    }

    private void setDateFilter() {
        final List<Meeting> mMeetings = apiService.getReunions();
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);

        final DatePicker datePicker = new DatePicker(ListActivity.this);
        datePicker.setCalendarViewShown(false);

        builder.setView(datePicker);

        builder.setPositiveButton(R.string.filter_ok_text, (dialog, id) -> {
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();
            Calendar mCalendarPicker = Calendar.getInstance();
            mCalendarPicker.set(year, month, day);

            List<Meeting> mReunionsFiltered = new ArrayList<>();
            int size = mMeetings.size();
            for (int e = 0; e < size; e++) {
                Calendar mReunionsCalendar = Calendar.getInstance();
                mReunionsCalendar.setTime(mMeetings.get(e).getStartDate());
                if (mCalendarPicker.get(Calendar.YEAR) == mReunionsCalendar.get(Calendar.YEAR)
                        && mCalendarPicker.get(Calendar.DAY_OF_YEAR) == mReunionsCalendar.get(Calendar.DAY_OF_YEAR)) {
                    mReunionsFiltered.add(mMeetings.get(e));
                }
            }
            adapter.setData(mReunionsFiltered);

        });

        builder.setNeutralButton(R.string.filter_reset_text, (dialog, id) -> adapter.setData(mMeetings));

        builder.show();
    }

    private void setRoomsFilter() {
        final List<Meeting> mMeetings = apiService.getReunions();
        // Build an AlertDialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
        // String array for alert dialog multi choice items
        final int numberRooms = apiService.getRooms().size();
        String[] mRooms = new String[numberRooms];
        for (int i = 0; i < numberRooms; i++) {
            mRooms[i] = apiService.getRooms().get(i).getRoomName();
        }
        // Boolean array for initial selected items
        final boolean[] checkedRooms = new boolean[numberRooms];
        // Convert the Rooms array to list
        final List<String> mRoomsList = Arrays.asList(mRooms);

        // Keep memory of the filter selection
        if (KEEP_FILTER_ROOM != null) {
            System.arraycopy(KEEP_FILTER_ROOM, 0, checkedRooms, 0, numberRooms);
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
            adapter.setData(mMeetings);
            KEEP_FILTER_ROOM = null;
        });

        final AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.setOnShowListener(dialogInterface -> {

            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                // Checks the checked rooms
                List<Meeting> mReunionsFiltered = new ArrayList<>();
                int sizeReunionsList = mMeetings.size();
                boolean atLeastOneChecked = false;
                for (int i = 0; i < checkedRooms.length; i++) {
                    boolean checked = checkedRooms[i];
                    if (checked) {
                        atLeastOneChecked = true;
                        String mRoomFiltered = mRoomsList.get(i);
                        for (int e = 0; e < sizeReunionsList; e++) {
                            if (mRoomFiltered.equals(mMeetings.get(e).getRoom().getRoomName())) {
                                mReunionsFiltered.add(mMeetings.get(e));
                            }
                        }
                    }
                }
                // if no rooms checked, Toast to alert user
                if (!atLeastOneChecked) {
                    Toast toastRoomNotSelected = Toast.makeText(getApplicationContext(), R.string.toast_room_not_selected, Toast.LENGTH_LONG);
                    toastRoomNotSelected.setGravity(Gravity.CENTER, 0, 0);
                    View toastViewCreateReunion = toastRoomNotSelected.getView();
                    TextView toastTextCreateReunion = toastViewCreateReunion.findViewById(android.R.id.message);
                    toastTextCreateReunion.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.toast_add_meeting_color));
                    toastRoomNotSelected.show();
                } // Otherwise, show the selection result
                else {
                    dialog.dismiss();
                    adapter.setData(mReunionsFiltered);
                    KEEP_FILTER_ROOM = checkedRooms;
                }
            });
        });
        dialog.show();

    }


}
