package com.example.mareu.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.R;
import com.example.mareu.di.DI;
import com.example.mareu.model.Reunion;
import com.example.mareu.service.MaReuApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListReunionActivity extends AppCompatActivity {

    private MyAdapter adapter;
    private MaReuApiService mMaReuApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpRecyclerView();

        mMaReuApiService = DI.getMaReuApiService();
        adapter.setData(mMaReuApiService.getReunions());

        createNewReunionAction();
    }

    //  ****************************************** INIT  *******************************************
    private void setUpRecyclerView() {
        final RecyclerView rv = findViewById(R.id.maReu_list_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter();
        rv.setAdapter(adapter);
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

            case R.id.action_sort_date: {
                setDateSorter();

                return true;}

            case R.id.action_filter_date: {
                // TODO Datepicker pour sélectionner la date à laquelle faire les réunions
                setDateFilter();
                return true;
            }
            case R.id.action_filter_room: {
                setRoomsFilter();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //  ****************************************** ACTIONS  ****************************************

    private void createNewReunionAction() {
        FloatingActionButton mButtonNewReunion = findViewById(R.id.button_add_reunion);
        mButtonNewReunion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListReunionActivity.this, AddReunionActivity.class);
                startActivity(intent);
            }
        });
    }


    private void setDateSorter() {
        final List<Reunion> mReunions = mMaReuApiService.getReunions();
        Collections.sort(mReunions, new Comparator<Reunion>() {
            public int compare(Reunion o1, Reunion o2) {
                if (o1.getDate() == null || o2.getDate() == null)
                    return 0;
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        adapter.setData(mReunions);
    }

    private void setDateFilter() {
        final List<Reunion> mReunions = mMaReuApiService.getReunions();
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ListReunionActivity.this);

        final DatePicker mDatePicker = new DatePicker(ListReunionActivity.this);
        mDatePicker.setCalendarViewShown(false);

        builder.setTitle(R.string.filter_date_text);
        builder.setView(mDatePicker);

        builder.setPositiveButton(R.string.filter_ok_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDayOfMonth();
                Calendar mCalendarPicker = Calendar.getInstance();
                mCalendarPicker.set(year, month, day);

                List<Reunion> mReunionsFiltered = new ArrayList<>();
                int size = mReunions.size();
                for (int e = 0; e < size; e++) {
                    Calendar mReunionsCalendar = Calendar.getInstance();
                    mReunionsCalendar.setTime(mReunions.get(e).getDate());
                    if (mCalendarPicker.get(Calendar.YEAR) == mReunionsCalendar.get(Calendar.YEAR)
                            && mCalendarPicker.get(Calendar.DAY_OF_YEAR) == mReunionsCalendar.get(Calendar.DAY_OF_YEAR)) {
                        mReunionsFiltered.add(mReunions.get(e));
                    }
                }
                adapter.setData(mReunionsFiltered);

            }
        });

        builder.setNeutralButton(R.string.filter_reset_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                adapter.setData(mReunions);
            }
        });

        builder.show();
    }

    private void setRoomsFilter() {
        final List<Reunion> mReunions = mMaReuApiService.getReunions();
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ListReunionActivity.this);

        // String array for alert dialog multi choice items
        int size = mMaReuApiService.getRooms().size();
        String[] mRooms = new String[size];
        for (int i = 0; i < size; i++) {
            mRooms[i] = mMaReuApiService.getRooms().get(i).getRoomName();
        }

        // Boolean array for initial selected items
        final boolean[] checkedRooms = new boolean[size];

        // Convert the Rooms array to list
        final List<String> mRoomsList = Arrays.asList(mRooms);

        // Set multiple choice items for alert dialog
        builder.setMultiChoiceItems(mRooms, checkedRooms, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                // Update the current focused item's checked status
                checkedRooms[which] = isChecked;

                // Get the current focused item
                String currentItem = mRoomsList.get(which);
            }
        });

        // Specify the dialog is not cancelable
        builder.setCancelable(false);

        // Set a title for alert dialog
        builder.setTitle(R.string.filter_rooms_text);

        // Set the positive/yes button click listener
        builder.setPositiveButton(R.string.filter_ok_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int size = mReunions.size();
                List<Reunion> mReunionsFiltered = new ArrayList<>();
                for (int i = 0; i < checkedRooms.length; i++) {
                    boolean checked = checkedRooms[i];
                    if (checked) {
                        String mRoomFiltered = mRoomsList.get(i);
                        for (int e = 0; e < size; e++) {
                            if (mRoomFiltered.equals(mReunions.get(e).getRoom().getRoomName())) {
                                mReunionsFiltered.add(mReunions.get(e));
                            }
                        }
                    }
                }
                adapter.setData(mReunionsFiltered);
            }
        });

        // Set the negative/no button click listener
        builder.setNeutralButton(R.string.filter_reset_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.setData(mReunions);
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }


}
