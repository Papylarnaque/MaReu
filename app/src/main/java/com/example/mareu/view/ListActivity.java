package com.example.mareu.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.R;
import com.example.mareu.di.DI;
import com.example.mareu.service.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class ListActivity extends AppCompatActivity {

    public boolean[] KEEP_FILTER_ROOM;    // Keeps memory of the room filter selection
    public MyAdapter adapter;
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
                apiService.setRoomsFilter(this);
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

            apiService.filterMeetingsByDate(this, mCalendarPicker);
        });

        builder.setNeutralButton(R.string.filter_reset_text, (dialog, id) -> {
            adapter.setData(apiService.getMeetings());
        });

        builder.show();

    }


}
