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

import java.util.Calendar;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    public boolean[] KEEP_FILTER_ROOM;    // Keeps memory of the room filter selection
    private MyAdapter adapter;
    private ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpRecyclerView();

        createNewMeetingAction();
    }

    //  ****************************************** INIT  *******************************************
    private void setUpRecyclerView() {
        final RecyclerView rv = findViewById(R.id.list_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter();
        rv.setAdapter(adapter);

        apiService = DI.getApiService();
        adapter.setData(apiService.getMeetings());
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
                adapter.setData(apiService.setDateSorter());
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

    private void createNewMeetingAction() {
        FloatingActionButton mButtonNewMeeting = findViewById(R.id.button_add_meeting);
        mButtonNewMeeting.setOnClickListener(v -> {
            Intent intent = new Intent(ListActivity.this, AddMeetingActivity.class);
            startActivity(intent);
        });
    }

    private void setDateFilter() {
        Calendar mCalendarPicker = Calendar.getInstance();
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final DatePicker datePicker = new DatePicker(this);
        datePicker.setCalendarViewShown(false);

        builder.setView(datePicker);

        builder.setPositiveButton(R.string.filter_ok_text, (dialog, id) -> {
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();

            mCalendarPicker.set(year, month, day);

            adapter.setData(apiService.filterMeetingsByDate(mCalendarPicker));

        });

        builder.setNeutralButton(R.string.filter_reset_text, (dialog, id) -> {
            adapter.setData(apiService.getMeetings());
        });

        builder.show();

    }

    public void setRoomsFilter() {
        // Build an AlertDialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // String array for alert dialog multi choice items
        final int numberRooms = apiService.getRooms().size();
        String[] mRooms = apiService.getRoomsAsStringList();
        // Boolean array for initial selected items
        final boolean[] checkedRooms = new boolean[numberRooms];
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
            adapter.setData(apiService.getMeetings());
            KEEP_FILTER_ROOM = null;
        });

        final AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.setOnShowListener(dialogInterface -> {

            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                // Checks the checked rooms
                boolean atLeastOneChecked = false;
                for (boolean checked : checkedRooms) {
                    if (checked) {
                        atLeastOneChecked = true;
                        break;
                    }
                }

                // if no rooms checked, Toast to alert user
                if (!atLeastOneChecked) {
                    Toast toastRoomNotSelected = Toast.makeText(getApplicationContext(), R.string.toast_room_not_selected, Toast.LENGTH_SHORT);
                    toastRoomNotSelected.setGravity(Gravity.CENTER, 0, 0);
                    View toastViewCreateMeeting = toastRoomNotSelected.getView();
                    TextView toastTextCreateMeeting = toastViewCreateMeeting.findViewById(android.R.id.message);
                    toastTextCreateMeeting.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.toast_add_meeting_color));
                    toastRoomNotSelected.show();
                } else {
                    List<Meeting> mMeetingsFiltered = apiService.filterMeetingsByRoom(checkedRooms);
                    dialog.dismiss();
                    adapter.setData(mMeetingsFiltered);
                    KEEP_FILTER_ROOM = checkedRooms;
                }

            });
        });
        dialog.show();

    }
}
